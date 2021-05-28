/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.shiro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import com.supermap.gaf.authority.client.AuthUserInfoDetailsClient;
import com.supermap.gaf.authority.service.impl.AuthUserInfoDetailsDbImpl;
import com.supermap.gaf.authority.service.impl.AuthUsernameJwtImpl;
import com.supermap.gaf.shiro.handler.TenantInfoImpl;
import com.supermap.gaf.shiro.realms.CustomTokenRealm;
import com.supermap.gaf.storage.spi.TenantInfoI;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.keycloak.authorization.client.AuthzClient;
import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.cas.logout.DefaultCasLogoutHandler;
import org.pac4j.core.authorization.generator.AuthorizationGenerator;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.http.url.DefaultUrlResolver;
import org.pac4j.core.http.url.UrlResolver;
import org.pac4j.oidc.client.KeycloakOidcClient;
import org.pac4j.oidc.profile.OidcProfile;
import org.slf4j.Logger;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

import com.nimbusds.jose.JWSAlgorithm;
import com.supermap.gaf.shiro.config.KeycloakConfig;
import com.supermap.gaf.shiro.config.ShiroConfig;
import com.supermap.gaf.utils.LogUtil;

import io.buji.pac4j.filter.CallbackFilter;
import io.buji.pac4j.filter.LogoutFilter;
import io.buji.pac4j.filter.SecurityFilter;

/**
 * shiro全局配置类
 * @author h_sha
 * @date:2021/3/25
 *
 */
@Configuration
@ConditionalOnProperty(name = "shiro.enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({ ShiroConfig.class })
public class ShiroConfiguration {

    public static final String DEFAULT_CALLBACK_URL = "callback";
    public static final String URL_PATH_SEPARATOR = "/";
    public static final String DEFAULT_KEYCLOAK_LOGIN_URL = "keycloak";
    public static final String DEFAULT_CAS_LOGIN_URL = "cas";
    public static final String DEFAULT_LOGIN_URL = "login";
    public static final String DEFAULT_LOGOUT_URL = "logout";
    public static final String DEFAULT_DEBUG_LOGIN_URL = "logind";
    public static final String SHIRO_PUBLIC_FILTER = "anon";

    private static Logger logger = LogUtil.getLocLogger(ShiroConfiguration.class);

    @Autowired
    private ShiroConfig shiroConfig;

    @Autowired
    private AuthUserInfoDetailsDbImpl authUserInfoDetailsDb;
    @Autowired
    private AuthUsernameJwtImpl authUsernameJwt;

    @Bean
    public CustomTokenRealm customTokenRealmDb() {
        CustomTokenRealm tokenRealm = new CustomTokenRealm(authUsernameJwt,authUserInfoDetailsDb);
        return tokenRealm;
    }

    @Bean
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        CustomShiroFilterFactoryBean shiroFilterFactoryBean = new CustomShiroFilterFactoryBean();
        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 默认跳转到登录页面
        shiroFilterFactoryBean.setLoginUrl(shiroConfig.getLoginUrl());
        // 默认跳转到登录页面
        shiroFilterFactoryBean.setSuccessUrl(shiroConfig.getSuccessUrl());
        // 自定义过滤器
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        // 权限控制map
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // 定义DevRealm登陆过滤器
        if (shiroConfig.getDev() != null && shiroConfig.getDev().isEnable()) {
            filterMap.put("loginDebugFilter", new DevLoginFilter());
            filterChainDefinitionMap.put("/logind", "loginDebugFilter");
        }

        Config config = new Config();
        List<Client> clientList = new ArrayList<>();
//         定义cas过滤器
        if (shiroConfig.getCas() != null && shiroConfig.getCas().isEnable()) {
            CasClient client = casClient();
            clientList.add(client);
            SecurityFilter casSecurityFilter = new SecurityFilter();
            casSecurityFilter.setClients(client.getName());
            casSecurityFilter.setConfig(config);
            filterMap.put("casSecurityFilter", casSecurityFilter);
            filterChainDefinitionMap.put(URL_PATH_SEPARATOR + DEFAULT_CAS_LOGIN_URL, "casSecurityFilter");
        }

        // 定义keycloak过滤器
        if (shiroConfig.getKeycloak() != null && shiroConfig.getKeycloak().isEnable()) {
            KeycloakOidcClient client = keycloakOidcClient();
            RoleGenerator roleGenerator = new RoleGenerator(shiroConfig.getKeycloak());
            client.addAuthorizationGenerator(roleGenerator);
            GroupGenerator groupGenerator = new GroupGenerator(shiroConfig.getKeycloak());
            client.addAuthorizationGenerator(groupGenerator);
            PermissionGenerator permissionGenerator = new PermissionGenerator();
            client.addAuthorizationGenerator(permissionGenerator);
            clientList.add(client);

            SecurityFilter keycloakSecurityFilter = new SecurityFilter();
            keycloakSecurityFilter.setClients(client.getName());
            keycloakSecurityFilter.setConfig(config);
            filterMap.put("keycloakSecurityFilter", keycloakSecurityFilter);
            filterChainDefinitionMap.put(URL_PATH_SEPARATOR + DEFAULT_KEYCLOAK_LOGIN_URL, "keycloakSecurityFilter");
        }

        // 设置回调处理
        if (CollectionUtils.isNotEmpty(clientList)) {
        	config.setClients(new Clients(clientList));
//            config.setClients(new Clients(clientList));
            config.getClients().setCallbackUrl(calculateCallBackUrl(shiroConfig.getServiceUrl()));
            // cas 认证后回调拦截器
            CallbackFilter callbackFilter = new CallbackFilter();
            callbackFilter.setDefaultUrl(shiroConfig.getSuccessUrl());
            callbackFilter.setCallbackLogic(new RedirectShiroCallbackLogic<>());
            callbackFilter.setConfig(config);
            callbackFilter.setMultiProfile(true);
            filterMap.put("callbackFilter", callbackFilter);
            filterChainDefinitionMap.put(URL_PATH_SEPARATOR + DEFAULT_CALLBACK_URL, "callbackFilter");
        }

        // 设置登出
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setConfig(config);
        logoutFilter.setDefaultUrl(shiroConfig.getSuccessUrl());
        logoutFilter.setCentralLogout(true);
        logoutFilter.setLocalLogout(true);
        filterMap.put("logout", logoutFilter);
        filterChainDefinitionMap.put(URL_PATH_SEPARATOR + DEFAULT_LOGOUT_URL, "logout");
        //追加公开地址权限
        if(CollectionUtils.isNotEmpty(shiroConfig.getPublicUrls())) {
            for (String str : shiroConfig.getPublicUrls()) {
                filterChainDefinitionMap.put(str, SHIRO_PUBLIC_FILTER);
            }    
        }
        // 加载配置文件中自定义权限设置
        if (CollectionUtils.isNotEmpty(shiroConfig.getUrlFilters())){
            for (String str : shiroConfig.getUrlFilters()) {
                int index = str.indexOf(",");
                if (index != -1) {
                    filterChainDefinitionMap.put(str.substring(0, index), str.substring(index + 1));
                }
            }
        }

        
        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 核心的安全事务管理器 设置realm、cacheManager等
     * 
     * @return
     */
    @Bean
    public SecurityManager securityManager(@Autowired(required = false) List<Realm> shiroRealms) throws IllegalArgumentException {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm
        if (CollectionUtils.isEmpty(shiroRealms)) {
            logger.error("未定义有效的shiro realm，请检查！");
            throw new IllegalArgumentException("gaf shiro初始化异常！");
        }
        securityManager.setRealms(shiroRealms);
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean
    @ConditionalOnProperty(name = "shiro.keycloak.enable", havingValue = "true")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public GAFJWTTokenParser jwtTokenParser() {
        GAFJWTTokenParser tokenParser = new GAFJWTTokenParser();
        tokenParser.setConfiguration(shiroConfig.getKeycloak());
        RoleGenerator<OidcProfile> roleGenerator = new RoleGenerator<>(shiroConfig.getKeycloak());
        GroupGenerator<OidcProfile> groupGenerator = new GroupGenerator<>(shiroConfig.getKeycloak());
        PermissionGenerator permissionGenerator = new PermissionGenerator();
        tokenParser.setAuthorizationGenerators(Arrays.asList(new AuthorizationGenerator[] { roleGenerator, groupGenerator, permissionGenerator }));
        return tokenParser;
    }

    @Bean
    @ConditionalOnProperty(name = "shiro.keycloak.enable", havingValue = "true")
    public AuthzClient authzClient() {
        org.keycloak.authorization.client.Configuration config = new org.keycloak.authorization.client.Configuration();
        KeycloakConfig keycloakConfig = shiroConfig.getKeycloak();
        config.setAuthServerUrl(keycloakConfig.getBaseUri());
        config.setRealm(keycloakConfig.getRealm());
        config.setResource(keycloakConfig.getClientId());
        Map<String, Object> cres = new HashMap<>();
        cres.put("secret", keycloakConfig.getSecret());
        config.setCredentials(cres);
        return AuthzClient.create(config);
    }

    /**
     * 请求cas服务端配置
     */
    public CasClient casClient() {
        CasConfiguration configuration = new CasConfiguration();
        // CAS server登录地址
        configuration.setLoginUrl(shiroConfig.getCas().getServer());
        configuration.setLogoutHandler(new DefaultCasLogoutHandler<WebContext>());
        UrlResolver urlResolver = new DefaultUrlResolver(true);
        CasClient casClient = new CasClient(configuration);
        casClient.setUrlResolver(urlResolver);
        casClient.setName("cas");
        return casClient;
    }

    public KeycloakOidcClient keycloakOidcClient() {
        KeycloakOidcClient client = new KeycloakOidcClient();
        client.setName("keycloak");
        client.setConfiguration(shiroConfig.getKeycloak());
        UrlResolver urlResolver = new DefaultUrlResolver(true);
        client.setUrlResolver(urlResolver);
        shiroConfig.getKeycloak().setPreferredJwsAlgorithm(JWSAlgorithm.RS256);
        return client;
    }

    /**
     *  pac4j配置
     * @param casClient
     * @return
     */
    public Config config(CasClient casClient) {
        Config config = new Config(casClient);
        return config;
    }
    
    /**
     * 记住我管理器
     * 
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // rememberMe cookie加密的密钥 默认AES算法
//         cookieRememberMeManager.setCipherKey();
        return cookieRememberMeManager;
    }

    /**
     * cookie对象
     * 
     * @return
     */
    @Bean
    public Cookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setMaxAge(3600);
        return simpleCookie;
    }

    /**
     * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;否则@RequiresRoles等注解无法生效
     * 
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Shiro生命周期处理器
     * 
     * @return
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 自动创建代理
     * 
     * @return
     */
    @Bean
    @DependsOn({ "lifecycleBeanPostProcessor" })
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    private String calculateCallBackUrl(String serviceUrl) {
        String callbackUrl = DEFAULT_CALLBACK_URL;
        if (StringUtils.isEmpty(serviceUrl)) {
            return callbackUrl;
        } else {
            callbackUrl = serviceUrl.endsWith("/") ? serviceUrl + callbackUrl : serviceUrl + URL_PATH_SEPARATOR + callbackUrl;
        }
        return callbackUrl;
    }
    
    @Bean
    @Lazy
    @ConditionalOnProperty(name = "shiro.keycloak.enable", havingValue = "true")
    public GAFAuthzClientService authzClientService() {
        GAFAuthzClientService service = new GAFAuthzClientService();
        return service;
    }

    @Bean
    @ConditionalOnMissingBean(TenantInfoI.class)
    public TenantInfoI tenantInfoI(){
        return new TenantInfoImpl();
    }

}

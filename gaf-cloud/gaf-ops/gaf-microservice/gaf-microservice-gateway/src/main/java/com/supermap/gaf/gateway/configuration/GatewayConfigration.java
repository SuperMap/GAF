/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.configuration;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.supermap.gaf.gateway.dynamicroute.DefaultAbstractDynamicRouteServiceImpl;
import com.supermap.gaf.gateway.filters.*;
import com.supermap.gaf.gateway.listener.DynamicRouteLoadListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: gaf-service-manager-modules
 * @description:
 * @author: lidong、duke
 * @date:2021/3/25
 * @create: 2019/07/22
 */
@Configuration
public class GatewayConfigration {

    @Autowired
    private RouteDefinitionRepository routeDefinitionWriter;


    @Bean
    public DynamicRouteLoadListener dynamicRouteLoadListener() {
        return new DynamicRouteLoadListener();
    }

    @Bean
    public DefaultAbstractDynamicRouteServiceImpl defaultDynamicRouteService() {
        return new DefaultAbstractDynamicRouteServiceImpl(routeDefinitionWriter);
    }

    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        return new FastJsonHttpMessageConverter();
    }


    @Bean
    public JwtTokenFilter tokenFilter() {
        return new JwtTokenFilter();
    }

    @Bean
    public AccessValidFilter permissionAccessFilter() {
        return new AccessValidFilter();
    }
//
//    @Bean
//    public ReactiveOAuth2UserService<OidcUserRequest, OidcUser> userService() {
//        return new GafOidcUserService();
//    }
//
//
//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    @Bean
//    @ConditionalOnProperty(name = "gateway.security.enable", havingValue = "true", matchIfMissing = true)
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http){
//        GatewaySecurityProperties securityConfig = gatewayConfig.getSecurity();
//        String publicUrlStrs = GatewaySecurityProperties.DEFAULT_PUBLIC_URLS;
//        if(StringUtils.isNotEmpty(securityConfig.getPublicUrls())) {
//            publicUrlStrs = securityConfig.getPublicUrls();
//        }
//        //避免参数设置中空格引起的错误
//        String[] publicUrls = publicUrlStrs.split(",");
//        for (int i = 0; i < publicUrls.length; i++) {
//            publicUrls[i] = publicUrls[i].trim();
//        }
//
//        CenterLogoutHandler centerLogoutHandler =  new CenterLogoutHandler(
//                securityConfig.getCenterLogoutUrl(),
//                securityConfig.getIndexUrl());
//        http.authorizeExchange()
//                .pathMatchers(publicUrls).permitAll()
//                .pathMatchers("/**").authenticated().and().oauth2Login().and()
//                .exceptionHandling().authenticationEntryPoint(new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/okta")).and()
//                .headers().contentTypeOptions().disable().frameOptions().disable().cache().disable()
//                .and()
//                .logout().requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/logout"))
//                .logoutSuccessHandler(centerLogoutHandler)
//                .and()
//                .formLogin().disable()
//                .csrf().disable()
//                .cors().disable()
//                .oauth2ResourceServer().jwt();
//        //设置security拦截器，保证第三方授权登录后重定向到统一的路径下
//        http.addFilterAt(
//                new LoginFilter(gatewayConfig.getSecurity().getLoginUrl(), gatewayConfig.getSecurity().getIndexUrl(), gatewayConfig.getSecurity().getHomeUrl(), publicUrls),
//                SecurityWebFiltersOrder.FIRST);
//        return http.build();
//    }
}

package com.supermap.gaf.boot.configuration;

import com.supermap.gaf.authentication.client.ValidateClient;
import com.supermap.gaf.authority.client.AuthUserClient;
import com.supermap.gaf.boot.filter.*;
import com.supermap.gaf.common.storage.servlet.ProxyServlet;
import com.supermap.gaf.gateway.commontypes.properties.GatewaySecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({GatewaySecurityProperties.class})
public class FilterConfig {
    @Value("${GAF_STORAGE_URL:http://gaf-storage:8080/storage}")
    private String storageUrl;
    @Bean
    public FilterRegistrationBean<SecurityHeaderFilter> securityHeaderFilterRegistrationBean() {
        FilterRegistrationBean<SecurityHeaderFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new SecurityHeaderFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(Integer.MIN_VALUE);
        bean.setName("securityHeaderFilter");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<XwebRedirectFilter> xWebRedirectFilterRegistrationBean(@Autowired GatewaySecurityProperties gatewaySecurityProperties) {
        FilterRegistrationBean<XwebRedirectFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new XwebRedirectFilter(gatewaySecurityProperties));
        bean.addUrlPatterns("/*");
        bean.setOrder(Integer.MIN_VALUE + 1);
        bean.setName("xWebRedirectFilter");
        return bean;
    }


    @Bean
    public FilterRegistrationBean<XgatewayAuthenticationQueryFilter> xGatewayAuthenticationQueryFilterRegistrationBean(@Autowired ValidateClient validateClient) {
        FilterRegistrationBean<XgatewayAuthenticationQueryFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new XgatewayAuthenticationQueryFilter(validateClient));
        bean.addUrlPatterns("/*");
        bean.setOrder(100);
        bean.setName("xGatewayAuthenticationQueryFilter");
        return bean;
    }


    @Bean
    public FilterRegistrationBean<XgatewayAuthenticationValidateFilter> xGatewayAuthenticationValidateFilterRegistrationBean() {
        FilterRegistrationBean<XgatewayAuthenticationValidateFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new XgatewayAuthenticationValidateFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(150);
        bean.setName("xGatewayAuthenticationValidateFilter");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<XgatewayAuthorizationValidateFilter> xGatewayAuthorizationValidateFilterRegistrationBean(@Autowired ValidateClient validateClient, @Autowired AuthUserClient authUserClient) {
        FilterRegistrationBean<XgatewayAuthorizationValidateFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new XgatewayAuthorizationValidateFilter(validateClient,authUserClient));
        bean.addUrlPatterns("/*");
        bean.setOrder(175);
        bean.setName("xGatewayAuthorizationValidateFilter");
        return bean;
    }


    @Bean
    public FilterRegistrationBean<XgatewayRequestTokenFilter> xGatewayRequestTokenFilterRegistrationBean() {
        FilterRegistrationBean<XgatewayRequestTokenFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new XgatewayRequestTokenFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(200);
        bean.setName("xGatewayRequestTokenFilter");
        return bean;
    }

    @Bean
    public ServletRegistrationBean storageProxyServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new ProxyServlet(), "/storage/*");
        bean.addInitParameter(ProxyServlet.P_TARGET_URI, storageUrl);
        bean.addInitParameter(ProxyServlet.P_LOG, "true");
        bean.setLoadOnStartup(1);
        return bean;
    }

}

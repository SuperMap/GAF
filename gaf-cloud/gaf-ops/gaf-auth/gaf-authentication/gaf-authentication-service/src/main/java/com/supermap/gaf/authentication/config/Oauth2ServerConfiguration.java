/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.config;

import com.supermap.gaf.authentication.config.authc.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @date:2021/3/25
 * @author dqc
 */
@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfiguration extends AuthorizationServerConfigurerAdapter {

    /**
     * 支持 password 模式
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 支持 refresh_token 模式
     */
    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

    @Autowired
    @Qualifier("jwtTokenStore")
    private TokenStore tokenStore;
    @Autowired
    @Qualifier("jwtAccessTokenConverter")
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired
    @Qualifier("jwtTokenEnhancer")
    private TokenEnhancer jwtTokenEnhancer;


    @Autowired
    private DataSource dataSource;



    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancers = new ArrayList<>();
        enhancers.add(jwtTokenEnhancer);
        enhancers.add(jwtAccessTokenConverter);
        enhancerChain.setTokenEnhancers(enhancers);

        /**
         * //配置令牌的存储
         * //jwt
         * //不重复使用refresh_token,如果为true则refresh_token接口返回的refresh_token无法刷新，只能用第一次的
         * //password模式需要authenticationManager
         * //refresh——token模式需要userdetails
         */
        endpoints
                .tokenStore(tokenStore)
                .tokenEnhancer(enhancerChain)
                .reuseRefreshTokens(false)
                .authenticationManager(authenticationManager)
                .userDetailsService(customUserDetailsServiceImpl);
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

}

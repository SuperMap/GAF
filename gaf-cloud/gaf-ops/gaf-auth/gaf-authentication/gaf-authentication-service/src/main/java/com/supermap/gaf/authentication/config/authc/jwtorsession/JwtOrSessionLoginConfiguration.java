/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.config.authc.jwtorsession;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/18 10:57 AM
 */
public class JwtOrSessionLoginConfiguration<T extends JwtOrSessionLoginConfiguration<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    private JwtOrSessionAuthenticationFilter jwtOrSessionAuthenticationFilter;

    public JwtOrSessionLoginConfiguration() {
        this.jwtOrSessionAuthenticationFilter = new JwtOrSessionAuthenticationFilter();
    }

    @Override
    public void configure(B http) throws Exception {
        //设置Filter使用的AuthenticationManager,这里取公共的即可
        jwtOrSessionAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

        JwtOrSessionAuthenticationFilter filter = postProcess(jwtOrSessionAuthenticationFilter);

        http.addFilterAfter(filter, LogoutFilter.class);
    }


    /**
     * 设置成功的Handler，这个handler定义成Bean，所以从外面set进来
     * @param successHandler
     * @return
     */
    public JwtOrSessionLoginConfiguration<T,B> loginSuccessHandler(AuthenticationSuccessHandler successHandler){
        jwtOrSessionAuthenticationFilter.setSuccessHandler(successHandler);
        return this;
    }


    /**
     * 设置失败的Handler，这个handler定义成Bean，所以从外面set进来
     * @param failureHandler
     * @return
     */
    public JwtOrSessionLoginConfiguration<T,B> loginFailureHandler(AuthenticationFailureHandler failureHandler){
        jwtOrSessionAuthenticationFilter.setFailureHandler(failureHandler);
        return this;
    }

}

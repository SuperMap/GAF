/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.config.authc.usernamepassword;

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
public class UsernamePasswordLoginConfiguration<T extends UsernamePasswordLoginConfiguration<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    private UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter;

    public UsernamePasswordLoginConfiguration() {
        this.usernamePasswordAuthenticationFilter = new UsernamePasswordAuthenticationFilter();
    }

    @Override
    public void configure(B http) throws Exception {
        //设置Filter使用的AuthenticationManager,这里取公共的即可
        usernamePasswordAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        //不将认证后的context放入session

        UsernamePasswordAuthenticationFilter filter = postProcess(usernamePasswordAuthenticationFilter);

        http.addFilterAfter(filter, LogoutFilter.class);
    }


    /**
     * 设置成功的Handler，这个handler定义成Bean，所以从外面set进来
     * @param successHandler
     * @return
     */
    public UsernamePasswordLoginConfiguration<T,B> loginSuccessHandler(AuthenticationSuccessHandler successHandler){
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        return this;
    }


    /**
     * 设置失败的Handler，这个handler定义成Bean，所以从外面set进来
     * @param failureHandler
     * @return
     */
    public UsernamePasswordLoginConfiguration<T,B> loginFailureHandler(AuthenticationFailureHandler failureHandler){
        usernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);
        return this;
    }

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.config.authc.usernamepassword;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.supermap.gaf.authentication.entity.constant.LoginConstant.LOGIN_WITH_USERNAME_PASSWORD;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/18 10:30 AM
 */

public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public UsernamePasswordAuthenticationFilter() {
        //拦截登录请求
        super(new AntPathRequestMatcher(LOGIN_WITH_USERNAME_PASSWORD, HttpMethod.POST.name()));
    }

    @Override
        public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}

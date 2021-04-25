/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.config.authc.jwtorsession;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.supermap.gaf.authentication.entity.constant.LoginConstant.CUSTOM_LOGIN_SESSION_NAME;
import static com.supermap.gaf.authentication.entity.constant.LoginConstant.LOGIN_CENTER_URL;
import static com.supermap.gaf.authentication.entity.constant.LoginConstant.LOGIN_WITH_USERNAME_PASSWORD;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/17 10:10 AM
 */
public class JwtOrSessionAuthenticationFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        Cookie cookie = new Cookie(CUSTOM_LOGIN_SESSION_NAME,null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        if (httpServletRequest.getRequestURL().toString().contains(LOGIN_WITH_USERNAME_PASSWORD)){
            httpServletResponse.sendRedirect(LOGIN_WITH_USERNAME_PASSWORD + "?straight=true");
        }else {
            httpServletResponse.sendRedirect(LOGIN_CENTER_URL + "?straight=true");
        }
    }
}

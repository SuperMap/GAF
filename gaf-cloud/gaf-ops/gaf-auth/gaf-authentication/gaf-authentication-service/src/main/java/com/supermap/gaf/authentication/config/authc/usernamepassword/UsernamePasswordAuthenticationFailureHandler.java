/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authentication.config.authc.usernamepassword;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermap.gaf.commontypes.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.supermap.gaf.authentication.entity.constant.LoginConstant.CUSTOM_LOGIN_SESSION_NAME;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/17 10:10 AM
 */
public class UsernamePasswordAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        Cookie cookie = new Cookie(CUSTOM_LOGIN_SESSION_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        MessageResult result = MessageResult.successe(Void.class).status(HttpStatus.UNAUTHORIZED.value()).build();
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(result));
    }
}

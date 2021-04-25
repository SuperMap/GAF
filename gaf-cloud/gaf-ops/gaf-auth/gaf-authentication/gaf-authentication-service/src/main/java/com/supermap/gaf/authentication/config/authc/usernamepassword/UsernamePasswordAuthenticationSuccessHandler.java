/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.config.authc.usernamepassword;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermap.gaf.authentication.dao.UserLoginMapper;
import com.supermap.gaf.authentication.service.CustomLoginService;
import com.supermap.gaf.commontypes.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.supermap.gaf.authentication.entity.constant.LoginConstant.CUSTOM_LOGIN_SESSION_NAME;
import static com.supermap.gaf.authentication.entity.constant.LoginConstant.LOGIN_CALLBACK_WITH_OIDC_VALIDATED;
import static com.supermap.gaf.authentication.entity.constant.LoginConstant.LOGIN_SUCCESS_REDIRECT;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/17 10:10 AM
 */
public class UsernamePasswordAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private CustomLoginService customLoginService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserLoginMapper userLoginMapper;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String customSessionId = httpServletRequest.getParameter(CUSTOM_LOGIN_SESSION_NAME);
        String username = authentication.getName();
        if (StringUtils.isEmpty(customSessionId)){
            String sessionId = UUID.randomUUID().toString().replace("-","");
            OAuth2AccessToken oAuth2AccessToken = customLoginService.createOauth2AccessTokenWithoutPassword(username);
            userLoginMapper.updateLoginTime(username);
            customLoginService.storeLoginSession(sessionId,username,oAuth2AccessToken,null,null,null);
            Cookie cookie = new Cookie(CUSTOM_LOGIN_SESSION_NAME,sessionId);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
            //下面防止axios 拦截不到302请求
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            MessageResult result = MessageResult.successe(String.class).status(HttpStatus.FOUND.value()).data(LOGIN_SUCCESS_REDIRECT).build();
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(result));
        }else {
            //TODO checksession
            httpServletResponse.sendRedirect(String.format(LOGIN_CALLBACK_WITH_OIDC_VALIDATED + "?username=%s&%s=%s",username,CUSTOM_LOGIN_SESSION_NAME,customSessionId));
        }
    }
}

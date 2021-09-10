/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authentication.resources;

import com.supermap.gaf.authentication.config.authc.CustomUserDetailsServiceImpl;
import com.supermap.gaf.authentication.service.CustomLoginService;
import com.supermap.gaf.commontypes.MessageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author dqc
 * @date:2021/3/25 /authentication/token
 */
@Path("/")
@Api("token接口")
@Component
public class TokenResource {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
    @Autowired
    private CustomLoginService customLoginService;

    @ApiOperation(value = "账号密码获取token", notes = "账号密码获取token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query", dataType = "string", required = true)
    })
    @POST
    @Produces(APPLICATION_JSON)
    public MessageResult<OAuth2AccessToken> getToken(@Context HttpServletRequest request) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        MessageResult<OAuth2AccessToken> result = MessageResult.successe(OAuth2AccessToken.class).build();

        UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            result.setMessage("password is incorrect");
            result.setSuccessed(false);
            return result;
        }

        OAuth2AccessToken oAuth2AccessToken = customLoginService.createOauth2AccessTokenWithoutPassword(username);
        result.setData(oAuth2AccessToken);
        return result;
    }

    @ApiOperation(value = "请求新token", notes = "通过刷新token请求新的token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "refresh_token", value = "请求token时返回的刷新token值", paramType = "query", dataType = "string", required = true)
    })
    @POST
    @Produces(APPLICATION_JSON)
    @Path("/refresh")
    public MessageResult<OAuth2AccessToken> refreshToken(@Context HttpServletRequest request) throws Exception {
        String refreshToken = request.getParameter("refresh_token");
        OAuth2AccessToken oAuth2AccessToken = null;
        try {
            oAuth2AccessToken = customLoginService.refreshOauth2AccessToken(refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResult.failed(OAuth2AccessToken.class).message("刷新token失败").build();
        }
        return MessageResult.successe(OAuth2AccessToken.class).data(oAuth2AccessToken).build();
    }


    @ApiOperation(value = "校验token", notes = "接收access_token，并进行是否在登录状态的校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token", value = "token值", paramType = "query", dataType = "string", required = true)
    })
    @POST
    @Produces(APPLICATION_JSON)
    @Path("/check")
    public MessageResult<Map> checkToken(@Context HttpServletRequest request) throws Exception {
        String refreshToken = request.getParameter("access_token");
        Map data = null;
        try {
            data = customLoginService.checkJwtToken(refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
            return MessageResult.failed(Map.class).message("检查token失败").build();
        }
        return MessageResult.successe(Map.class).data(data).build();
    }


}

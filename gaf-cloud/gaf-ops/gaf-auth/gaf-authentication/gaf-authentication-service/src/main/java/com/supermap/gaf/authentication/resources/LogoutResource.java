/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.resources;

import com.supermap.gaf.authentication.entity.entity.AuthenticationParam;
import com.supermap.gaf.authentication.service.CustomLogoutService;
import com.supermap.gaf.authentication.utils.HttpRequestUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import static com.supermap.gaf.authentication.entity.constant.LoginConstant.*;

/**
 * @author dqc
 * @date:2021/3/25
 * /authentication/logout
 */
@Path("/")
@Api("登出接口")
@Component
public class LogoutResource {


    @Autowired
    private CustomLogoutService logoutService;

    /**
     * 清除session，退出登录状态接口
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "退出登录状态", notes = "清除session，退出登录状态接口")
    @GET
    @Path("/")
    public void logout(@Context HttpServletRequest request,
                       @Context HttpServletResponse response) throws Exception{
        AuthenticationParam authenticationParam = HttpRequestUtils.getJwtOrSession(request);

        Cookie cookie = new Cookie(CUSTOM_LOGIN_SESSION_NAME,null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.sendRedirect(LOGIN_SUCCESS_REDIRECT);


        logoutService.logout(authenticationParam.getCustomSessionId());
    }







}

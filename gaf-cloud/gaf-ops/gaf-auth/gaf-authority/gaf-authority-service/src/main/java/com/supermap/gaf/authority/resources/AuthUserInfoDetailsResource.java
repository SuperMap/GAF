/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.client.AuthUserInfoDetailsClient;
import com.supermap.gaf.authority.commontype.*;
import com.supermap.gaf.authority.service.impl.AuthUserInfoDetailsDbImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * 用户详情接口
 *
 * @author dqc
 * /authority/auth-user-details
 */
@Component
@Api(value = "用户详情接口")
public class AuthUserInfoDetailsResource implements AuthUserInfoDetailsClient {
    @Autowired
    private AuthUserInfoDetailsDbImpl authUserInfoDetailsDb;

    @ApiOperation(value = "用户详情接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "string"),
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Override
    public AuthUserInfoDetails getAuthUserInfoDetails(@QueryParam("username") String username) {
        return authUserInfoDetailsDb.getAuthUserInfoDetails(username);
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.resources;


import com.supermap.gaf.authentication.client.ValidateClient;
import com.supermap.gaf.authentication.entity.entity.AuthenticationParam;
import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.authentication.service.ValidateAuthenticationService;
import com.supermap.gaf.commontypes.MessageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author dqc
 * @date:2021/3/25
 * /authentication/validate
 */
@Component
@Api(value = "认证校验接口")
public class ValidateResource implements ValidateClient {
    @Autowired
    private ValidateAuthenticationService validateAuthenticationService;


    @ApiOperation(value = "token认证", notes = "接收session或token对登录状态进行校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticationParam",value = "请求校验认证状态的请求参数对象",paramType = "body",dataTypeClass = AuthenticationParam.class)
    })
    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Override
    public MessageResult<AuthenticationResult> authentication(AuthenticationParam authenticationParam) {
        AuthenticationResult authenticationResult = validateAuthenticationService.authentication(authenticationParam);
        return MessageResult.successe(AuthenticationResult.class).data(authenticationResult).build();
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.client.AuthUserInfoDetailsClient;
import com.supermap.gaf.authority.commontype.*;
import com.supermap.gaf.authority.service.AuthAuthorizationQueryService;
import com.supermap.gaf.authority.service.AuthUserQueryService;
import com.supermap.gaf.authority.vo.AuthUserParttimeSelectVo;
import com.supermap.gaf.commontypes.MessageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * 用户详情接口
 * @author dqc
 * /authority/auth-user-details
 */
@Component
@Api(value = "用户详情接口")
public class AuthUserInfoDetailsResource implements AuthUserInfoDetailsClient {
    @Autowired
    private AuthUserQueryService userQueryService;
    @Autowired
    private AuthAuthorizationQueryService authAuthorizationQueryService;


    @ApiOperation(value = "用户详情接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "string"),
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Override
    public MessageResult<AuthUserInfoDetails> getAuthUserInfoDetails(@QueryParam("username") String username) {
        AuthUser authUser = userQueryService.getByUserName(username);
        String userId = authUser.getUserId();
        List<AuthResourceApi> authResourceApis = authAuthorizationQueryService.listAuthorizationApi(userId);
        List<AuthResourceModule> authResourceModules = authAuthorizationQueryService.listAuthorizationModule(userId);
        List<AuthRole> authRoles = authAuthorizationQueryService.listAuthorizationRole(userId);
        AuthUserInfoDetails userInfoDetails = new AuthUserInfoDetails(authUser,authResourceApis,authResourceModules,authRoles);
        return MessageResult.successe(AuthUserInfoDetails.class).data(userInfoDetails).build();
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.resources;

import com.supermap.gaf.authentication.service.CustomLoginService;
import com.supermap.gaf.authority.commontype.AuthUser;
import com.supermap.gaf.authority.service.AuthUserQueryService;
import com.supermap.gaf.commontypes.MessageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Map;

import static com.supermap.gaf.authentication.entity.constant.LoginConstant.CLIENT_ID;
import static com.supermap.gaf.authentication.entity.constant.LoginConstant.USER_NAME;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author dqc
 * @date:2021/6/11
 * /authentication/user
 */
@Path("/")
@Api("oauth用户查询接口")
@Slf4j
@Component
public class UserResource {
    @Autowired
    private AuthUserQueryService authUserQueryService;
    @Autowired
    private CustomLoginService customLoginService;

    @ApiOperation(value = "oauth用户查询接口", notes = "oauth用户查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "access_token",value = "token",paramType = "query",dataType = "string",example = "123"),
            @ApiImplicitParam(name = "client_id",value = "第三方客户端名",paramType = "query",dataType = "string",example = "client"),
            @ApiImplicitParam(name = "userID",value = "用户名",paramType = "query",dataType = "string",example = "admin")
    })
    @Produces(APPLICATION_JSON)
    @GET
    public MessageResult<AuthUser> oauthUserInfo(@QueryParam("access_token")String accessToken,
                                       @QueryParam("client_id")String clientId,
                                       @QueryParam("user_name")String username) throws Exception{
        Map<String,?> checkResult = null;
        try {
            checkResult = customLoginService.checkJwtToken(accessToken);
        }catch (Exception e){
            log.info("Oauth user验证token失败");
        }
        if (checkResult == null){
            return MessageResult.failed(AuthUser.class).build();
        }
        String resultClientId = (String) checkResult.get(CLIENT_ID);
        String resultUsername = (String) checkResult.get(USER_NAME);
        if (!StringUtils.isEmpty(resultClientId) &&
                !StringUtils.isEmpty(resultUsername) &&
                resultClientId.equalsIgnoreCase(clientId) &&
                resultUsername.equalsIgnoreCase(username)){
            AuthUser authUser = authUserQueryService.getByUserName(username);
            authUser.setPassword(null);
            return MessageResult.successe(AuthUser.class).data(authUser).build();
        }
        return MessageResult.failed(AuthUser.class).build();
    }



}

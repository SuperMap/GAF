/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.resources;

import com.supermap.services.components.commontypes.Geometry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Component;
import javax.ws.rs.*;
import java.util.HashMap;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author dqc
 * oauth接口搬运，jersey方式
 * @see org.springframework.security.oauth2.provider.endpoint.TokenEndpoint
 * @date:2021/3/25
 * /authentication/oauth
 */
@Path("/")
@Api("oauth接口")
@Component
public class OauthResource {
    @Autowired
    private AuthorizationServerEndpointsConfiguration configuration;


    @ApiOperation(value = "oauth2协议passwd模式登录接口", notes = "oauth2协议passwd模式登录接口  oauth2已提供接口，这里暂未实现")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "用户名",paramType = "form",dataType = "string",required = true),
            @ApiImplicitParam(name = "password",value = "密码",paramType = "form",dataType = "string",required = true),
            @ApiImplicitParam(name = "grant_type",value = "授权模式",paramType = "form",dataType = "string",required = true),
            @ApiImplicitParam(name = "client_id",value = "客户端id",paramType = "form",dataType = "string",required = true),
            @ApiImplicitParam(name = "scope",value = "scope字段",paramType = "form",dataType = "string",required = true),
            @ApiImplicitParam(name = "client_secret",value = "客户端密码",paramType = "form",dataType = "string",required = true)
    })
    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Path("/token")
    public OAuth2AccessToken postAccessToken(@FormParam("username") String username,
                                             @FormParam("password") String password,
                                             @FormParam("grant_type") String grantType,
                                             @FormParam("client_id") String clientId,
                                             @FormParam("scope") String scope,
                                             @FormParam("client_secret") String clientSecret) throws Exception {
        Map<String,String> parameters = new HashMap<>(16);
        parameters.put("username",username);
        parameters.put("password",password);
        parameters.put("grant_type",grantType);
        parameters.put("client_id",clientId);
        parameters.put("scope",scope);
        parameters.put("client_secret",clientSecret);

        TokenEndpoint tokenEndpoint = configuration.tokenEndpoint();

        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(clientId,clientSecret);

        //TODO 解决jersey暴力oauth2请求接口问题
        return null;
    }


}

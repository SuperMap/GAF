/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.AuthP3TenantMapping;
import com.supermap.gaf.authority.service.AuthP3TenantMappingService;
import com.supermap.gaf.commontypes.MessageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;



/**
 * 第三方租户映射接口
 * @date:2021/3/25
 * @author yangdong
 */
@Component
@Api(value = "第三方租户映射接口")
public class AuthP3TenantMappingResource {
    private final AuthP3TenantMappingService authP3TenantMappingService;

    public AuthP3TenantMappingResource(AuthP3TenantMappingService authP3TenantMappingService) {
        this.authP3TenantMappingService = authP3TenantMappingService;
    }

    @ApiOperation(value = "新增第三方租户映射", notes = "新增第三方租户映射")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authP3TenantMapping", value = "第三方租户映射", dataTypeClass = AuthP3TenantMapping.class, paramType = "body",required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<AuthP3TenantMapping> insertAuthP3TenantMapping(AuthP3TenantMapping authP3TenantMapping) {
        AuthP3TenantMapping data = authP3TenantMappingService.insertAuthP3TenantMapping(authP3TenantMapping);
        return MessageResult.data(data).build();
    }

}

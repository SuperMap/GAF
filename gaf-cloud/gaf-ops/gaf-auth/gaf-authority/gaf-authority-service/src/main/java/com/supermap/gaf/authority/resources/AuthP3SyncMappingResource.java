/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.service.p3sync.P3SyncService;
import com.supermap.gaf.authority.vo.P3SyncVo;
import com.supermap.gaf.commontypes.MessageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 第三方资源映射接口
 *
 * @author yd
 * @date:2021/3/25
 * @since 2020-12-02
 */
@Component
@Api(value = "第三方资源映射接口")
public class AuthP3SyncMappingResource {

    private final P3SyncService p3SyncService;

    public AuthP3SyncMappingResource(P3SyncService p3SyncService) {
        this.p3SyncService = p3SyncService;
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "映射资源", notes = "通过规则id查询规则信息，映射资源，与第三方资源进行同步。例如租户、部门、用户的同步")
    public MessageResult<Object> mapping(@NotNull @Valid P3SyncVo p3SyncVo) {
        Object data = p3SyncService.dispatch(p3SyncVo.getRuleId(), p3SyncVo.getSearch(), p3SyncVo.getGafResourceId());
        return MessageResult.successe(Object.class).data(data).build();
    }

}

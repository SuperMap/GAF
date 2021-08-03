/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.PublishServiceParameter;
import com.supermap.gaf.data.mgt.service.publisher.manager.PublishDispatcher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 服务发布接口
 *
 * @author:yd
 * @date:2021/3/25
 * @Date 2021-3-01
 */

@Component
@Api(value = "服务发布接口")
public class ServiceResource {

    @Autowired
    private PublishDispatcher publishDispatcher;

    @ApiOperation(value = "服务发布", notes = "使用工作空间做服务发布", response = MessageResult.class)
    @ApiImplicitParam(name = "originParameter", value = "服务发布参数实体类", paramType = "body", dataTypeClass = PublishServiceParameter.class)

    @Path("/publish-service")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<String> publishService(PublishServiceParameter originParameter) {
        return publishDispatcher.dispatch(originParameter);
    }
}

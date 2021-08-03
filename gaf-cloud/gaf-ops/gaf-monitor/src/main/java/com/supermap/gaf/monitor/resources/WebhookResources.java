/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.monitor.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author : duke
 * @date:2021/3/25 /monitor/webhook
 * @since 2020/12/22 9:49 AM
 */
@Path("/")
@Api("webhook接口")
public class WebhookResources {


    @ApiOperation(value = "监控告警的webhook提醒接口", notes = "监控告警发送通知到monitor组件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "alertMsg", value = "提醒消息", paramType = "body", dataType = "string", required = true)
    })
    @Path("/alert")
    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public String webhook(String alertMsg) {
        System.out.println(alertMsg);
        return alertMsg;
    }

}

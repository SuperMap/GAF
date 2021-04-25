/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.resource;

import com.supermap.gaf.commontypes.MessageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * 查询iserver发布的服务的信息
 * @author wangxiaolong
 * @date 2020-12-05
 */
@Component
@Api(value = "查询iserver发布的服务的信息")
public class WebgisIserverResource {

    @Autowired
    RestTemplate restTemplate;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据iserver发布的地图服务地址查询地图的json元信息，需要能够访问到地图服务地址", notes = "根据iserver发布的地图服务地址查询地图的json元信息，需要能够访问到地图服务地址")
    @ApiImplicitParam(name = "address",value = "地图服务地址",paramType = "query",dataType = "string",required = true )
    public MessageResult<String> getJson(@QueryParam("address") String address) {
        String result = restTemplate.getForObject(address, String.class);
        return MessageResult.successe(String.class).data(result).status(200).message("查询成功").build();
    }



}

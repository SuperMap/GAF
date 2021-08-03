/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.srv.governance.resources;


import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.gateway.GatewayRouteDefinition;
import com.supermap.gaf.srv.governance.rest.client.GafGatewayClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


/**
 * @author dqc
 * @date:2021/3/25 /srv-governance/routes
 */
@Component
@Api(value = "网关路由管理接口")
public class GatewayRouteResource {
    @Autowired
    private GafGatewayClient gafGatewayClient;

    @ApiOperation(value = "查询自定义网关路由", notes = "查询自定义网关路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "routeSearchParam", value = "路由搜索参数json字符串", paramType = "query", dataType = "string")
    })
    @GET
    @Produces(APPLICATION_JSON)
    public MessageResult<List<GatewayRouteDefinition>> queryTenantGatewayRoutes(@QueryParam("routeSearchParam") String routeSearchParam) {
        return gafGatewayClient.queryTenantGatewayRoutes(routeSearchParam);
    }

    @ApiOperation(value = "Id为参数查询自定义网关路由", notes = "Id为参数查询自定义网关路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "自定义路由id", paramType = "path", dataType = "string")
    })
    @GET
    @Produces(APPLICATION_JSON)
    @Path("/{id}")
    public MessageResult<GatewayRouteDefinition> queryTenantGatewayRoute(@PathParam("id") String id) {
        return gafGatewayClient.queryTenantGatewayRoute(id);
    }

    @ApiOperation(value = "新增自定义网关路由", notes = "添加动态网关路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "route", value = "动态网关路由对象", paramType = "body", dataTypeClass = GatewayRouteDefinition.class)
    })
    @POST
    @Produces(APPLICATION_JSON)
    public MessageResult<String> addTenantRoute(GatewayRouteDefinition route) {
        return gafGatewayClient.addTenantRoute(route);
    }

    @ApiOperation(value = "批量删除路由", notes = "传入json字符串参数，批量删除路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "idsJsonStr", value = "json字符串参数", paramType = "body")
    })
    @DELETE
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public MessageResult<String> batchDeleteTenantRoute(String idsJsonStr) {
        return gafGatewayClient.batchDeleteTenantRoute(idsJsonStr);
    }

    @ApiOperation(value = "ID删除路由", notes = "传入ID参数，删除路由")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "路由id", paramType = "path", dataType = "string")
    })
    @DELETE
    @Produces(APPLICATION_JSON)
    @Path("/{id}")
    public MessageResult<String> deleteTenantRoute(@PathParam("id") String id) {
        return gafGatewayClient.deleteTenantRoute(id);
    }

    @ApiOperation(value = "编辑路由", notes = "编辑路由信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "route", value = "路由对象", paramType = "body", dataTypeClass = GatewayRouteDefinition.class)
    })
    @PUT
    @Produces(APPLICATION_JSON)
    public MessageResult<String> updateTenantRoute(GatewayRouteDefinition route) {
        return gafGatewayClient.updateTenantRoute(route);
    }


}

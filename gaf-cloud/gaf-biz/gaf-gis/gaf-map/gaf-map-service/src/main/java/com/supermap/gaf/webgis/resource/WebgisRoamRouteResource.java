/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.webgis.domain.WebgisRouteInfo;
import com.supermap.gaf.webgis.domain.WebgisRouteStopInfo;
import com.supermap.gaf.webgis.service.WebgisRoamRouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.sasl.AuthenticationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 漫游路线接口
 * @author wangxiaolong 
 * @date yyyy-mm-dd
 */
@Component
@Api(value = "漫游路线接口")
public class WebgisRoamRouteResource{
    @Autowired
    private WebgisRoamRouteService webgisRoamRouteService;


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "查询当前用户下的所有漫游路线")
    @Path("/routes")
    public MessageResult<List<WebgisRouteInfo>> listRoutes(){
        MessageResult<List<WebgisRouteInfo>> messageResult = new MessageResult<>();
        List<WebgisRouteInfo> infos = webgisRoamRouteService.listRoutes();
        messageResult.setSuccessed(true);
        messageResult.setData(infos);
        return messageResult;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据路线id查询路线下的所有站点")
    @Path("/{routeId}/stops")
    @ApiImplicitParam(name = "routeId",value = "路线id",paramType = "path",dataType = "string",required = true )
    public MessageResult<List<WebgisRouteStopInfo>> listRouteStops(@PathParam("routeId") String routeId) {
        MessageResult<List<WebgisRouteStopInfo>> messageResult = new MessageResult<>();
        List<WebgisRouteStopInfo> stopInfos = webgisRoamRouteService.listRouteStops(routeId);
        messageResult.setSuccessed(true);
        messageResult.setData(stopInfos);
        return messageResult;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据路线id查询对应的路线文件weburl")
    @Path("/{routeId}/weburl")
    @ApiImplicitParam(name = "routeId",value = "路线id",paramType = "path",dataType = "string",required = true )
    public MessageResult<String> getRouteFileUrl(@PathParam("routeId") String routeId) throws URISyntaxException, AuthenticationException {
        String webUrl = webgisRoamRouteService.getRouteFileUrl(routeId);
        return MessageResult.successe(String.class).data(webUrl).build();
    }


    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增漫游路线")
    @ApiImplicitParam(name = "webgisRouteInfo",value = "漫游路线",paramType = "body",dataTypeClass = WebgisRouteInfo.class,required = true )
    public MessageResult<WebgisRouteInfo> createRoute(WebgisRouteInfo webgisRouteInfo) throws IOException {
        WebgisRouteInfo result = webgisRoamRouteService.createRoute(webgisRouteInfo);
        return MessageResult.successe(WebgisRouteInfo.class).data(result).status(200).message("新增操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据路线id删除当前用户下的漫游路线")
    @Path("/{routeId}")
    @ApiImplicitParam(name = "routeId",value = "路线id",paramType = "path",dataType = "string",required = true )
    public MessageResult<Void> deleteRoute(@PathParam("routeId") String routeId) throws IOException {
        webgisRoamRouteService.deleteRoute(routeId);
        return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据路线id清除路线的所有漫游站点")
    @Path("/{routeId}/all-stops")
    @ApiImplicitParam(name = "routeId",value = "路线id",paramType = "path",dataType = "string",required = true )
    public MessageResult<Void> clearRouteStops(@PathParam("routeId") String routeId) throws IOException {
        webgisRoamRouteService.clearRouteStops(routeId);
        return MessageResult.successe(Void.class).status(200).message("清除路线的所有漫游站点").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "添加漫游站点")
    @Path("/route-stops")
    @ApiImplicitParam(name = "webgisRouteInfo",value = "漫游站点",paramType = "body",dataTypeClass = WebgisRouteInfo.class,required = true )
    public MessageResult<Void> addRouteStops(WebgisRouteInfo webgisRouteInfo) throws IOException {
        webgisRoamRouteService.addRouteStops(webgisRouteInfo);
        return MessageResult.successe(Void.class).status(200).message("添加漫游站点成功").build();
    }

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.webgis.entity.WebgisCatalogLayer;
import com.supermap.gaf.webgis.service.WebgisCatalogLayerService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisCatalogLayerSelectVo;
import com.supermap.gaf.webgis.vo.WebgisServiceToLayerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 图层接口
 *
 * @author wangxiaolong
 * @date 2020-12-05
 */
@Component
@Api(value = "图层接口")
public class WebgisCatalogLayerResource {
    @Autowired
    private WebgisCatalogLayerService webgisCatalogLayerService;


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id查询图层", notes = "根据id查询图层")
    @Path("/{catalogLayerId}")
    @ApiImplicitParam(name = "catalogLayerId", value = "图层id", paramType = "path", dataType = "string", required = true)
    public MessageResult<WebgisCatalogLayer> getById(@PathParam("catalogLayerId") String catalogLayerId) {
        WebgisCatalogLayer webgisCatalogLayer = webgisCatalogLayerService.getById(catalogLayerId);
        return MessageResult.successe(WebgisCatalogLayer.class).data(webgisCatalogLayer).status(200).message("查询成功").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "查询资源目录下的图层数量", notes = "查询资源目录下的图层数量")
    @Path("/count/{LayerCatalogId}")
    @ApiImplicitParam(name = "LayerCatalogId", value = "图层id", paramType = "path", dataType = "string", required = true)
    public MessageResult<Integer> countByCatalogId(@NotEmpty @PathParam("LayerCatalogId") String LayerCatalogId) {
        Integer count = webgisCatalogLayerService.countByCatalogId(LayerCatalogId);
        return MessageResult.successe(Integer.class).data(count).status(200).message("查询成功").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询图层", notes = "分页条件查询图层")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1", defaultValue = "1", allowableValues = "range[0,infinity]", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10", allowableValues = "range[0,infinity]", paramType = "query", dataType = "integer")})
    public MessageResult<Page<WebgisCatalogLayer>> pageList(@Valid @BeanParam WebgisCatalogLayerSelectVo webgisCatalogLayerSelectVo,
                                                            @DefaultValue("1") @QueryParam("pageNum") Integer pageNum,
                                                            @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {

        Page<WebgisCatalogLayer> page = webgisCatalogLayerService.listByPageCondition(webgisCatalogLayerSelectVo, pageNum, pageSize);
        return MessageResult.data(page).status(200).message("查询成功").build();
    }


    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增图层", notes = "新增图层")
    @ApiImplicitParam(name = "webgisCatalogLayer", value = "图层", paramType = "body", dataTypeClass = WebgisCatalogLayer.class, required = true)
    public MessageResult<Void> insertWebgisCatalogLayer(WebgisCatalogLayer webgisCatalogLayer) {
        webgisCatalogLayerService.insertWebgisCatalogLayer(webgisCatalogLayer);
        return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch/webgis-service")
    @ApiOperation(value = "增加webgis服务对应的图层", notes = "增加webgis服务对应的图层")
    @ApiImplicitParam(name = "webgisServiceToLayerVos", value = "图层数组", paramType = "body", allowMultiple = true, required = true)
    public MessageResult<Void> batchInsertByService(List<WebgisServiceToLayerVo> webgisServiceToLayerVos) {
        if (webgisServiceToLayerVos == null || webgisServiceToLayerVos.size() == 0) {
            return MessageResult.failed(Void.class).message("未选择的webgis服务").build();
        }
        webgisCatalogLayerService.batchInsertByService(webgisServiceToLayerVos);
        return MessageResult.successe(Void.class).status(200).message("操作成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增图层", notes = "批量新增图层")
    @ApiImplicitParam(name = "WebgisCatalogLayers", value = "图层数组", paramType = "body", allowMultiple = true, required = true)
    public MessageResult<Void> batchInsert(List<WebgisCatalogLayer> WebgisCatalogLayers) {
        webgisCatalogLayerService.batchInsert(WebgisCatalogLayers);
        return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除图层", notes = "根据id删除图层")
    @Path("/{catalogLayerId}")
    @ApiImplicitParam(name = "catalogLayerId", value = "图层id", paramType = "path", dataType = "string", required = true)
    public MessageResult<Void> deleteWebgisCatalogLayer(@PathParam("catalogLayerId") String catalogLayerId) {
        webgisCatalogLayerService.deleteWebgisCatalogLayer(catalogLayerId);
        return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除图层", notes = "批量删除图层")
    @ApiImplicitParam(name = "catalogLayerIds", value = "图层id数组", paramType = "body", allowMultiple = true, required = true)
    public MessageResult<Void> batchDelete(List<String> catalogLayerIds) {
        webgisCatalogLayerService.batchDelete(catalogLayerIds);
        return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }


    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新图层", notes = "根据id更新图层")
    @Path("/{catalogLayerId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "webgisCatalogLayer", value = "图层", paramType = "body", dataTypeClass = WebgisCatalogLayer.class, required = true),
            @ApiImplicitParam(name = "catalogLayerId", value = "图层id", paramType = "path", dataType = "string", required = true)})
    public MessageResult<Void> updateWebgisCatalogLayer(WebgisCatalogLayer webgisCatalogLayer, @PathParam("catalogLayerId") String catalogLayerId) {
        webgisCatalogLayer.setCatalogLayerId(catalogLayerId);
        webgisCatalogLayerService.updateWebgisCatalogLayer(webgisCatalogLayer);
        return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }


}

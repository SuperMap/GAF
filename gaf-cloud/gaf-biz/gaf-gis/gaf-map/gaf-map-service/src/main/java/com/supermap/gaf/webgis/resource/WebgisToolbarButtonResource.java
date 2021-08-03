/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.webgis.domain.WebgisToolbarButtonDo;
import com.supermap.gaf.webgis.entity.WebgisToolbarButton;
import com.supermap.gaf.webgis.service.WebgisToolbarButtonService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisToolbarButtonSelectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 工具条按钮接口
 *
 * @author zhurongcheng
 * @date 2020-12-05
 */
@Component
@Api(value = "工具条按钮接口")
public class WebgisToolbarButtonResource {
    @Autowired
    private WebgisToolbarButtonService webgisToolbarButtonService;


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id查询工具条按钮", notes = "根据id查询工具条按钮")
    @Path("/{toolbarButtonId}")
    @ApiImplicitParam(name = "toolbarButtonId", value = "工具条按钮id", paramType = "path", dataType = "string", required = true)
    public MessageResult<WebgisToolbarButton> getById(@PathParam("toolbarButtonId") String toolbarButtonId) {
        WebgisToolbarButton webgisToolbarButton = webgisToolbarButtonService.getById(toolbarButtonId);
        return MessageResult.successe(WebgisToolbarButton.class).data(webgisToolbarButton).status(200).message("查询成功").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id查询按钮配置及其按钮详情", notes = "根据id查询按钮配置及其按钮详情")
    @Path("/do/{toolbarButtonId}")
    @ApiImplicitParam(name = "toolbarButtonId", value = "工具条按钮id", paramType = "path", dataType = "string", required = true)
    public MessageResult<WebgisToolbarButtonDo> getDoById(@PathParam("toolbarButtonId") String toolbarButtonId) {
        WebgisToolbarButtonDo toolbarButtonDo = webgisToolbarButtonService.getDoById(toolbarButtonId);
        return MessageResult.successe(WebgisToolbarButtonDo.class).data(toolbarButtonDo).status(200).message("查询成功").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据工具条id查询所有按钮配置及其按钮详情列表", notes = "根据工具条id查询所有按钮配置及其按钮详情列表")
    @Path("/dos/{toolbarId}")
    @ApiImplicitParam(name = "toolbarId", value = "工具条id", paramType = "path", dataType = "string", required = true)
    public MessageResult<List> selectDosByToolbarId(@PathParam("toolbarId") String toolbarId) {
        List<WebgisToolbarButtonDo> toolbarButtonDos = webgisToolbarButtonService.selectDosByToolbarId(toolbarId);
        return MessageResult.successe(List.class).data(toolbarButtonDos).status(200).message("查询成功").build();
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询工具条按钮", notes = "分页条件查询工具条按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1", defaultValue = "1", allowableValues = "range[0,infinity]", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10", allowableValues = "range[0,infinity]", paramType = "query", dataType = "integer")})
    public MessageResult<Page<WebgisToolbarButton>> pageList(@Valid @BeanParam WebgisToolbarButtonSelectVo webgisToolbarButtonSelectVo,
                                                             @DefaultValue("1") @QueryParam("pageNum") Integer pageNum,
                                                             @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        if (StringUtils.isEmpty(webgisToolbarButtonSelectVo.getOrderFieldName())) {
            webgisToolbarButtonSelectVo.setOrderFieldName("updated_time");
            webgisToolbarButtonSelectVo.setOrderMethod("desc");
        }
        Page<WebgisToolbarButton> page = webgisToolbarButtonService.listByPageCondition(webgisToolbarButtonSelectVo, pageNum, pageSize);
        return MessageResult.data(page).status(200).message("查询成功").build();
    }


    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增工具条按钮", notes = "新增工具条按钮")
    @ApiImplicitParam(name = "webgisToolbarButton", value = "工具条按钮", paramType = "body", dataTypeClass = WebgisToolbarButton.class, required = true)
    public MessageResult<Void> insertWebgisToolbarButton(WebgisToolbarButton webgisToolbarButton) {
        webgisToolbarButtonService.insertWebgisToolbarButton(webgisToolbarButton);
        return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增工具条按钮", notes = "批量新增工具条按钮")
    @ApiImplicitParam(name = "WebgisToolbarButtons", value = "工具条按钮", paramType = "body", allowMultiple = true, required = true)
    public MessageResult<Void> batchInsert(List<WebgisToolbarButton> WebgisToolbarButtons) {
        webgisToolbarButtonService.batchInsert(WebgisToolbarButtons);
        return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除工具条按钮", notes = "根据id删除工具条按钮")
    @Path("/{toolbarButtonId}")
    @ApiImplicitParam(name = "toolbarButtonId", value = "工具条按钮id", paramType = "path", dataType = "string", required = true)
    public MessageResult<Void> deleteWebgisToolbarButton(@PathParam("toolbarButtonId") String toolbarButtonId) {
        webgisToolbarButtonService.deleteWebgisToolbarButton(toolbarButtonId);
        return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除工具条按钮", notes = "批量删除工具条按钮")
    @ApiImplicitParam(name = "toolbarButtonIds", value = "工具条按钮数组", paramType = "body", allowMultiple = true, required = true)
    public MessageResult<Void> batchDelete(List<String> toolbarButtonIds) {
        webgisToolbarButtonService.batchDelete(toolbarButtonIds);
        return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }


    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新工具条按钮", notes = "根据id更新工具条按钮")
    @Path("/{toolbarButtonId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "webgisToolbarButton", value = "工具条按钮", paramType = "body", dataTypeClass = WebgisToolbarButton.class, required = true),
            @ApiImplicitParam(name = "toolbarButtonId", value = "工具条按钮id", paramType = "path", dataType = "string", required = true)})
    public MessageResult<Void> updateWebgisToolbarButton(WebgisToolbarButton webgisToolbarButton, @PathParam("toolbarButtonId") String toolbarButtonId) {
        webgisToolbarButton.setToolbarButtonId(toolbarButtonId);
        webgisToolbarButtonService.updateWebgisToolbarButton(webgisToolbarButton);
        return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }


}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.webgis.domain.WebgisConfigData;
import com.supermap.gaf.webgis.domain.WebgisToolbarDo;
import com.supermap.gaf.webgis.entity.WebgisToolbar;
import com.supermap.gaf.webgis.service.WebgisConfigService;
import com.supermap.gaf.webgis.service.WebgisToolbarService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisToolbarSelectVo;
import com.supermap.gaf.webgis.vo.WebgisToolbarVo;
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
import java.util.Arrays;
import java.util.List;

/**
 * 工具条接口
 *
 * @author zhurongcheng
 * @date 2020-12-05
 */
@Component
@Api(value = "工具条接口")
public class WebgisToolbarResource {
    @Autowired
    private WebgisToolbarService webgisToolbarService;

    @Autowired
    private WebgisConfigService webgisConfigService;


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id查询工具条", notes = "根据id查询工具条")
    @Path("/{toolbarId}")
    @ApiImplicitParam(name = "toolbarId", value = "工具条id", paramType = "path", dataType = "string", required = true)
    public MessageResult<WebgisToolbar> getById(@PathParam("toolbarId") String toolbarId) {
        WebgisToolbar webgisToolbar = webgisToolbarService.getById(toolbarId);
        return MessageResult.successe(WebgisToolbar.class).data(webgisToolbar).status(200).message("查询成功").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id查询工具条及其按钮配置信息", notes = "根据id查询工具条及其按钮配置信息")
    @Path("/do/{toolbarId}")
    @ApiImplicitParam(name = "toolbarId", value = "工具条id", paramType = "path", dataType = "string", required = true)
    public MessageResult<WebgisToolbarDo> getDoById(@PathParam("toolbarId") String toolbarId) {

        WebgisToolbarDo webgisToolbarDo = webgisToolbarService.getDoById(toolbarId);
        return MessageResult.successe(WebgisToolbarDo.class).data(webgisToolbarDo).status(200).message("查询成功").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "工具条配置", notes = "工具条配置")
    @Path("/{toolbarId}/config")
    @ApiImplicitParam(name = "toolbarId", value = "工具条id", paramType = "path", dataType = "string", required = true)
    public MessageResult<WebgisConfigData> getConfig(@PathParam("toolbarId") String toolbarId) {
        WebgisToolbarDo webgisToolbarDo = webgisToolbarService.getDoById(toolbarId);
        WebgisConfigData appConfigData = webgisConfigService.convert2ToolbarConfig(Arrays.asList(webgisToolbarDo));
        return MessageResult.data(appConfigData).status(200).message("查询成功").build();

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "检查字段是否可用", notes = "检查字段是否可用")
    @Path("/checkAvailable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "toolbar", value = "工具条", paramType = "body", dataTypeClass = WebgisToolbar.class, required = true),
            @ApiImplicitParam(name = "isUpdate", value = "是否更新操作", paramType = "query", dataType = "boolean", defaultValue = "false")})
    public MessageResult<Void> checkAvailable(WebgisToolbar toolbar, @DefaultValue("false") @QueryParam("isUpdate") boolean isUpdate) {
        webgisToolbarService.checkAvailable(toolbar, isUpdate);
        return MessageResult.successe(Void.class).status(200).message("可使用").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询工具条", notes = "分页条件查询工具条")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1", defaultValue = "1", allowableValues = "range[0,infinity]", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10", allowableValues = "range[0,infinity]", paramType = "query", dataType = "integer")})
    public MessageResult<Page<WebgisToolbar>> pageList(@Valid @BeanParam WebgisToolbarSelectVo webgisToolbarSelectVo,
                                                       @DefaultValue("1") @QueryParam("pageNum") Integer pageNum,
                                                       @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        if (StringUtils.isEmpty(webgisToolbarSelectVo.getOrderFieldName())) {
            webgisToolbarSelectVo.setOrderFieldName("updated_time");
            webgisToolbarSelectVo.setOrderMethod("desc");
        }
        Page<WebgisToolbar> page = webgisToolbarService.listByPageCondition(webgisToolbarSelectVo, pageNum, pageSize);
        return MessageResult.data(page).status(200).message("查询成功").build();
    }


    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增工具条及其按钮配置", notes = "新增工具条及其按钮配置")
    @ApiImplicitParam(name = "toolbarVo", value = "工具条及其按钮配置", paramType = "body", dataTypeClass = WebgisToolbarVo.class, required = true)
    public MessageResult<Void> insertWebgisToolbar(@Valid WebgisToolbarVo toolbarVo) {
        webgisToolbarService.insertWebgisToolbar(toolbarVo);
        return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增工具条", notes = "批量新增工具条")
    @ApiImplicitParam(name = "WebgisToolbars", value = "工具条数组", paramType = "body", allowMultiple = true, required = true)
    public MessageResult<Void> batchInsert(List<WebgisToolbar> WebgisToolbars) {
        webgisToolbarService.batchInsert(WebgisToolbars);
        return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除工具条", notes = "根据id删除工具条")
    @Path("/{toolbarId}")
    @ApiImplicitParam(name = "toolbarId", value = "工具条id", paramType = "path", dataType = "string", required = true)
    public MessageResult<Void> deleteWebgisToolbar(@PathParam("toolbarId") String toolbarId) {
        webgisToolbarService.deleteWebgisToolbar(toolbarId);
        return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除工具条", notes = "批量删除工具条")
    @ApiImplicitParam(name = "toolbarIds", value = "工具条id数组", paramType = "body", allowMultiple = true, required = true)
    public MessageResult<Void> batchDelete(List<String> toolbarIds) {
        webgisToolbarService.batchDelete(toolbarIds);
        return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }


    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新工具条及其按钮配置", notes = "根据id更新工具条及其按钮配置")
    @Path("/{toolbarId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "toolbarVo", value = "工具条及其按钮配置", paramType = "body", dataTypeClass = WebgisToolbarVo.class, required = true),
            @ApiImplicitParam(name = "toolbarId", value = "工具条id", paramType = "path", dataType = "string", required = true)})
    public MessageResult<Void> updateWebgisToolbar(@Valid WebgisToolbarVo toolbarVo, @PathParam("toolbarId") String toolbarId) {
        toolbarVo.setToolbarId(toolbarId);
        webgisToolbarService.updateWebgisToolbar(toolbarVo);
        return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }


}

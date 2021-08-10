/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.SysComponent;
import com.supermap.gaf.authority.service.SysComponentService;
import com.supermap.gaf.authority.vo.SelectOptionVo;
import com.supermap.gaf.authority.vo.SysComponentSelectVo;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.validator.StringRange;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * @author zhm
 * @date:2021/3/25
 */
@Component
@Api(value = "组件接口")
public class SysComponentResource {
    private final SysComponentService sysComponentService;

    public SysComponentResource(SysComponentService sysComponentService) {
        this.sysComponentService = sysComponentService;
    }

    @ApiOperation(value = "查询组件", notes = "根据id查询组件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysComponentId", value = "组件id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{sysComponentId}")
    public MessageResult<SysComponent> getById(@PathParam("sysComponentId") String sysComponentId) {
        return MessageResult.data(sysComponentService.getById(sysComponentId)).message("查询成功").build();
    }

    @ApiOperation(value = "获取所有组件选择项", notes = "获取所有组件选择项，提供给前端选择器的数据")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/options")
    public MessageResult<List<SelectOptionVo>> getAllOptions() {
        return MessageResult.data(sysComponentService.getAllOptions()).message("查询成功").build();
    }

    @ApiOperation(value = "分页条件查询组件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchFieldName", value = "模糊查询字段名", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "searchFieldValue", value = "模糊查询字段值", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "orderFieldName", value = "排序字段值", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "orderMethod", value = "排序方式。升序为ASC,降序为DESC。默认不排序", allowableValues = "ASC,DESC", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1", defaultValue = "1", allowableValues = "range[1,infinity]", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10", allowableValues = "range(0,infinity]", paramType = "query", dataType = "integer"),
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Map<String, Object>> pageList(@StringRange(entityClass = SysComponent.class) @QueryParam("searchFieldName") String searchFieldName,
                                                       @QueryParam("searchFieldValue") String searchFieldValue,
                                                       @StringRange(entityClass = SysComponent.class) @QueryParam("orderFieldName") String orderFieldName,
                                                       @StringRange({"asc", "desc"}) @QueryParam("orderMethod") String orderMethod,
                                                       @DefaultValue("1") @QueryParam("pageNum") Integer pageNum,
                                                       @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer offset = (pageNum - 1) * pageSize;
        SysComponentSelectVo selectVo = SysComponentSelectVo.builder()
                .searchFieldName(searchFieldName)
                .searchFieldValue(searchFieldValue)
                .orderFieldName(orderFieldName)
                .orderMethod(orderMethod)
                .offset(offset)
                .pageSize(pageSize)
                .build();
        Map<String, Object> result;
        if (searchFieldName == null & orderFieldName == null) {
            result = sysComponentService.pageList(selectVo);
        } else {
            result = sysComponentService.searchList(selectVo);
        }
        return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "新增组件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysComponent", value = "组件", dataTypeClass = SysComponent.class, paramType = "body", required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> insertSysComponent(SysComponent sysComponent) {
        sysComponentService.insertSysComponent(sysComponent);
        return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @ApiOperation(value = "批量新增组件")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    public MessageResult<Void> batchInsert(List<SysComponent> sysComponents) {
        sysComponentService.batchInsert(sysComponents);
        return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @ApiOperation(value = "删除组件", notes = "根据id删除组件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysComponentId", value = "组件id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{sysComponentId}")
    public MessageResult<Void> deleteSysComponent(@PathParam("sysComponentId") String sysComponentId) {
        sysComponentService.deleteSysComponent(sysComponentId);
        return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

    @ApiOperation(value = "批量删除组件", notes = "根据id集合批量删除组件")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> batchDelete(@NotEmpty List<String> sysComponentIds) {
        sysComponentService.batchDelete(sysComponentIds);
        return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

    @ApiOperation(value = "更新组件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysComponent", value = "组件", dataTypeClass = SysComponent.class, paramType = "body", required = true),
            @ApiImplicitParam(name = "sysComponentId", value = "组件id", paramType = "path", dataType = "string", required = true)
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{sysComponentId}")
    public MessageResult<Void> updateSysComponent(SysComponent sysComponent, @PathParam("sysComponentId") String sysComponentId) {
        sysComponent.setSysComponentId(sysComponentId);
        sysComponentService.updateSysComponent(sysComponent);
        return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }

}

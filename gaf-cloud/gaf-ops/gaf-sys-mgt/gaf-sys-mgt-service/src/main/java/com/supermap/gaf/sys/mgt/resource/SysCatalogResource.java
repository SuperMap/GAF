/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.sys.mgt.resource;

import com.supermap.gaf.authority.vo.SelectOptionVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.commontypes.Page;
import com.supermap.gaf.sys.mgt.client.SysCatalogClient;
import com.supermap.gaf.sys.mgt.commontype.SysCatalog;
import com.supermap.gaf.sys.mgt.enums.CatalogTypeEnum;
import com.supermap.gaf.sys.mgt.service.SysCatalogService;
import com.supermap.gaf.validator.StringRange;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 目录接口
 *
 * @author wangxiaolong
 * @date:2021/3/25
 */
@Component
@Api(value = "目录接口")
public class SysCatalogResource implements SysCatalogClient {

    private final SysCatalogService sysCatalogService;

    public SysCatalogResource(SysCatalogService sysCatalogService) {
        this.sysCatalogService = sysCatalogService;
    }

    @ApiOperation(value = "查询根目录下的所有节点", notes = "根据根目录id查询当前根目录下的所有节点（包括当前根目录）. 这些节点未组织为树形结构。根目录指的是parentId等于'0'的目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rootCatalogId", value = "目录id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/root/{rootCatalogId}")
    public MessageResult<List<TreeNode>> getTreeByRoot(@NotEmpty @PathParam("rootCatalogId") String rootCatalogId) {
        return MessageResult.data(sysCatalogService.getTreeByCatalogId(rootCatalogId)).build();
    }

    /**
     * 等值条件查询
     *
     * @param queryCatalog 查询参数
     * @return 目录集合
     */
    @ApiOperation(value = "等值条件查询", notes = "注意所有参数非必须，以此为准。但是至少有一个参数")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/condition")
    @Override
    public MessageResult<List<SysCatalog>> list(@BeanParam SysCatalog queryCatalog) {
        return MessageResult.data(sysCatalogService.getByCombination(queryCatalog)).build();
    }

    @ApiOperation(value = "查询目录的所有业务类型可选项")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/biz-types")
    public MessageResult<List<SelectOptionVo>> getBizTypes() {
        return MessageResult.data(sysCatalogService.getBizTypes()).build();
    }

    @ApiOperation(value = "查询目录", notes = "根据id查询目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "catalogId", value = "目录id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{catalogId}")
    public MessageResult<SysCatalog> getById(@PathParam("catalogId") String catalogId) {
        return MessageResult.data(sysCatalogService.getById(catalogId)).status(200).message("查询成功").build();
    }

    @ApiOperation(value = "获取目录节点", notes = "根据目录类型获取目录节点（未组织为树形结构）。" +
            "目录类型有模块分组(1),API分组(2)," +
            "角色分组(3),菜单分组(4),webgis服务分组(5)" +
            "资源目录(6),字典分组(7)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "目录类型", example = "1", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/nodes/type/{type}")
    public MessageResult<List<TreeNode>> getNodesByType(@NotEmpty @PathParam("type") String type) {
        return MessageResult.data(sysCatalogService.getNodesByType(type)).status(200).message("查询成功").build();
    }

    @ApiOperation(value = "查询上级目录路径", notes = "根据id查询上级目录路径")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "catalogId", value = "目录id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/parentPath/{catalogId}")
    public MessageResult<List<String>> getParentPath(@PathParam("catalogId") String catalogId) {
        return MessageResult.data(sysCatalogService.getParentPath(catalogId)).status(200).message("查询成功").build();
    }


    @ApiOperation(value = "分页条件查询类型为6(资源目录)的根节点目录")
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
    @Path("/resource-root-catalogs")
    public MessageResult<Page<SysCatalog>> pageListResourceRootCatalog(@StringRange(entityClass = SysCatalog.class) @QueryParam("searchFieldName") String searchFieldName,
                                                                       @QueryParam("searchFieldValue") String searchFieldValue,
                                                                       @StringRange(entityClass = SysCatalog.class) @QueryParam("orderFieldName") String orderFieldName,
                                                                       @StringRange({"asc", "desc"}) @QueryParam("orderMethod") String orderMethod,
                                                                       @DefaultValue("1") @QueryParam("pageNum") Integer pageNum,
                                                                       @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        Page<SysCatalog> page = sysCatalogService.listByPageCondition(searchFieldName, searchFieldValue, orderFieldName, orderMethod, pageNum, pageSize, CatalogTypeEnum.RESOURCE_GROUP_TYPE);
        return MessageResult.data(page).message("查询成功").build();
    }

    @ApiOperation(value = "新增目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysCatalog", value = "目录", dataTypeClass = SysCatalog.class, paramType = "body", required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Override
    public MessageResult<SysCatalog> insertSysCatalog(@NotNull SysCatalog sysCatalog) {
        SysCatalog insertedSysCatalog = sysCatalogService.insertSysCatalog(sysCatalog);
        return MessageResult.data(insertedSysCatalog).message("新增操作成功").build();
    }

    @ApiOperation(value = "批量新增目录")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    public MessageResult<Void> batchInsert(List<SysCatalog> sysCatalogs) {
        sysCatalogService.batchInsert(sysCatalogs);
        return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @ApiOperation(value = "删除目录", notes = "根据id删除目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "catalogId", value = "目录id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{catalogId}")
    public MessageResult<SysCatalog> deleteSysCatalog(@PathParam("catalogId") String catalogId) {
        return MessageResult.data(sysCatalogService.deleteSysCatalog(catalogId)).message("删除操作成功").build();
    }

    @ApiOperation(value = "批量删除目录", notes = "根据id批量删除目录")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> batchDelete(List<String> catalogIds) {
        sysCatalogService.batchDelete(catalogIds);
        return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

    @ApiOperation(value = "更新目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysCatalog", value = "目录", paramType = "body", dataTypeClass = SysCatalog.class, required = true),
            @ApiImplicitParam(name = "catalogId", value = "目录id", paramType = "path", dataType = "string", required = true)
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{catalogId}")
    public MessageResult<SysCatalog> updateSysCatalog(SysCatalog sysCatalog, @PathParam("catalogId") String catalogId) {
        sysCatalog.setCatalogId(catalogId);
        return MessageResult.data(sysCatalogService.updateSysCatalog(sysCatalog)).message("更新操作成功").build();
    }


}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.AuthResourceMenu;
import com.supermap.gaf.authority.commontype.AuthResourceMenuNode;
import com.supermap.gaf.authority.service.AuthResourceMenuService;
import com.supermap.gaf.authority.vo.AuthResourceMenuSelectVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.validator.StringRange;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * 菜单接口
 *
 * @author wxl
 * @date:2021/3/25
 */
@Component
@Api(value = "菜单接口")
public class AuthResourceMenuResource {
    private final AuthResourceMenuService authResourceMenuService;

    public AuthResourceMenuResource(AuthResourceMenuService authResourceMenuService) {
        this.authResourceMenuService = authResourceMenuService;
    }

    @ApiOperation(value = "查询菜单", notes = "根据id查询菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceMenuId", value = "菜单id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{resourceMenuId}")
    public MessageResult<AuthResourceMenu> getById(@PathParam("resourceMenuId") String resourceMenuId) {
        return MessageResult.data(authResourceMenuService.getById(resourceMenuId)).message("查询成功").build();
    }

    @ApiOperation(value = "条件查询菜单树", notes = "若当前用户所属租户是平台管理员所属租户，则获取所有菜单")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/tree/condition")
    public MessageResult<List<AuthResourceMenuNode>> getMenuTree(@QueryParam("name") String name) {
        return MessageResult.data(authResourceMenuService.getMenuTree(name)).message("查询成功").build();
    }

    @ApiOperation(value = "查询菜单树节点集合", notes = "查询当前用户的菜单并转换为树节点（未组织为树结构）。若当前用户所属租户是平台管理员所属租户，则获取所有菜单")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/tree")
    public MessageResult<List<TreeNode>> getMenuNodes() {
        return MessageResult.data(authResourceMenuService.getMenuNodes()).message("查询成功").build();
    }

    @ApiOperation(value = "分页条件查询菜单")
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
    public MessageResult<Map<String, Object>> pageList(@StringRange(entityClass = AuthResourceMenu.class) @QueryParam("searchFieldName") String searchFieldName,
                                                       @QueryParam("searchFieldValue") String searchFieldValue,
                                                       @StringRange(entityClass = AuthResourceMenu.class) @QueryParam("orderFieldName") String orderFieldName,
                                                       @StringRange({"asc", "desc"}) @QueryParam("orderMethod") String orderMethod,
                                                       @QueryParam("pageNum") Integer pageNum,
                                                       @DefaultValue("50") @QueryParam("pageSize") Integer pageSize) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer offset = (pageNum - 1) * pageSize;
        AuthResourceMenuSelectVo selectVo = AuthResourceMenuSelectVo.builder()
                .searchFieldName(searchFieldName)
                .searchFieldValue(searchFieldValue)
                .orderFieldName(orderFieldName)
                .orderMethod(orderMethod)
                .offset(offset)
                .pageSize(pageSize)
                .build();
        Map<String, Object> result;
        if (searchFieldName == null & orderFieldName == null) {
            result = authResourceMenuService.pageList(selectVo);
        } else {
            result = authResourceMenuService.searchList(selectVo);
        }
        return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "新增菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authResourceMenu", value = "菜单", dataTypeClass = AuthResourceMenu.class, paramType = "body", required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> insertAuthResourceMenu(AuthResourceMenu authResourceMenu) {
        authResourceMenuService.insertAuthResourceMenu(authResourceMenu);
        return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @ApiOperation(value = "批量新增菜单")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    public MessageResult<Void> batchInsert(List<AuthResourceMenu> authResourceMenus) {
        authResourceMenuService.batchInsert(authResourceMenus);
        return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @ApiOperation(value = "删除菜单", notes = "根据id删除菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceMenuId", value = "菜单id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{resourceMenuId}")
    public MessageResult<Void> deleteAuthResourceMenu(@PathParam("resourceMenuId") String resourceMenuId) {
        authResourceMenuService.deleteAuthResourceMenu(resourceMenuId);
        return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

    @ApiOperation(value = "批量删除菜单", notes = "根据id集合批量删除菜单")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> batchDelete(List<String> resourceMenuIds) {
        authResourceMenuService.batchDelete(resourceMenuIds);
        return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

    @ApiOperation(value = "更新菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authResourceMenu", value = "菜单", dataTypeClass = AuthResourceMenu.class, paramType = "body", required = true),
            @ApiImplicitParam(name = "resourceMenuId", value = "菜单id", paramType = "path", dataType = "string", required = true)
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{resourceMenuId}")
    public MessageResult<Void> updateAuthResourceMenu(AuthResourceMenu authResourceMenu, @PathParam("resourceMenuId") String resourceMenuId) {
        authResourceMenu.setResourceMenuId(resourceMenuId);
        authResourceMenuService.updateAuthResourceMenu(authResourceMenu);
        return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }


}

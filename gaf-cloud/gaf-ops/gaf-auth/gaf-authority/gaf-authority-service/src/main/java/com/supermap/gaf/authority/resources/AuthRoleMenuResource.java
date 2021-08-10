/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.AuthRoleMenu;
import com.supermap.gaf.authority.service.AuthRoleMenuService;
import com.supermap.gaf.authority.valid.ValidList;
import com.supermap.gaf.authority.vo.AuthRoleMenuSelectVo;
import com.supermap.gaf.authority.vo.RoleMenuVo;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.validator.StringRange;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * @author wxl
 * @date:2021/3/25
 */
@Component
@Api(value = "角色菜单接口")
public class AuthRoleMenuResource {
    private final AuthRoleMenuService authRoleMenuService;

    public AuthRoleMenuResource(AuthRoleMenuService authRoleMenuService) {
        this.authRoleMenuService = authRoleMenuService;
    }

    @ApiOperation(value = "查询角色菜单", notes = "根据角色菜单id查询角色菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleMenuId", value = "角色菜单id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{roleMenuId}")
    public MessageResult<AuthRoleMenu> getById(@PathParam("roleMenuId") String roleMenuId) {
        return MessageResult.data(authRoleMenuService.getById(roleMenuId)).message("查询成功").build();
    }

    @ApiOperation(value = "分页条件查询角色菜单")
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
    public MessageResult<Map<String, Object>> pageList(@StringRange(entityClass = AuthRoleMenu.class) @QueryParam("searchFieldName") String searchFieldName,
                                                       @QueryParam("searchFieldValue") String searchFieldValue,
                                                       @StringRange(entityClass = AuthRoleMenu.class) @QueryParam("orderFieldName") String orderFieldName,
                                                       @StringRange({"asc", "desc"}) @QueryParam("orderMethod") String orderMethod,
                                                       @QueryParam("pageNum") Integer pageNum,
                                                       @DefaultValue("50") @QueryParam("pageSize") Integer pageSize) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer offset = (pageNum - 1) * pageSize;
        AuthRoleMenuSelectVo selectVo = AuthRoleMenuSelectVo.builder()
                .searchFieldName(searchFieldName)
                .searchFieldValue(searchFieldValue)
                .orderFieldName(orderFieldName)
                .orderMethod(orderMethod)
                .offset(offset)
                .pageSize(pageSize)
                .build();
        Map<String, Object> result;
        if (searchFieldName == null & orderFieldName == null) {
            result = authRoleMenuService.pageList(selectVo);
        } else {
            result = authRoleMenuService.searchList(selectVo);
        }
        return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "查询角色已绑定的角色菜单", notes = "根据角色id条件查询角色菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/list-by-role/{roleId}")
    public MessageResult<List<AuthRoleMenu>> listByRole(@PathParam("roleId") String roleId) {
        return MessageResult.data(authRoleMenuService.listByRole(roleId)).message("查询成功").build();
    }

    @ApiOperation(value = "更新某角色与菜单的关联关系", notes = "传入角色id和菜单id列表，查询到该角色已绑定的菜单，与传入的菜单id列表做对比更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleMenuVo", value = "角色菜单关联对象", dataTypeClass = RoleMenuVo.class, paramType = "body", required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/handle")
    public MessageResult<Void> handleRoleMenu(RoleMenuVo roleMenuVo) {
        authRoleMenuService.handleRoleMenu(roleMenuVo);
        return MessageResult.successe(Void.class).status(200).message("关联操作成功").build();
    }

    @ApiOperation(value = "批量新增角色菜单")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    public MessageResult<Void> batchInsert(@Valid ValidList<AuthRoleMenu> authRoleMenus) {
        authRoleMenuService.batchInsertRoleMenu(authRoleMenus);
        return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

}

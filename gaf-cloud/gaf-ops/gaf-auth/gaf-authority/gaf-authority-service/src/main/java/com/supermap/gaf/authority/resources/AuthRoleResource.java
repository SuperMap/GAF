/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.AuthRole;
import com.supermap.gaf.authority.service.AuthRoleService;
import com.supermap.gaf.authority.valid.ValidList;
import com.supermap.gaf.authority.vo.AuthRoleSelectVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.commontypes.ShiroUser;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.validator.StringRange;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 角色接口
 * @date:2021/3/25
 * @author zhm
 */
@Component
@Api(value = "角色接口")
public class AuthRoleResource  {
    private final AuthRoleService authRoleService;

    public AuthRoleResource(AuthRoleService authRoleService) {
        this.authRoleService = authRoleService;
    }

    @ApiOperation(value = "查询角色", notes = "根据id查询角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{roleId}")
    public MessageResult<AuthRole> getById(@PathParam("roleId") String roleId) {
        return MessageResult.data(authRoleService.getById(roleId)).message("查询成功").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "查询角色树节点集合", notes = "查询当前用户所属租户下的角色，然后转换为树节点。返回的数据中 ,节点类型type值为12表示角色组节点，type值为5表示角色节点。" +
            "注意:查询到的角色包括平台内置角色和角色组。树节点集合未组织为树结构")
    @Path("/tree")
    public MessageResult<List<TreeNode>> getRoleTree(){
        return MessageResult.data(authRoleService.getRoleTree()).message("查询成功").build();
    }

    @ApiOperation(value = "查询角色树节点集合（不包含平台内置角色）", notes = "查询当前用户所属租户下的角色，然后转换为树节点。返回的数据中 ,节点类型type值为12表示角色组节点，type值为5表示角色节点。" +
            "注意:查询到的角色包括平台内置角色和角色组。树节点集合未组织为树结构")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/selftree")
    public MessageResult<List<TreeNode>> getRoleTreeWithSelf(){
        return MessageResult.data(authRoleService.getRoleTreeWithOutInnerRole()).message("查询成功").build();
    }

    @ApiOperation(value = "分页条件查询角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchFieldName", value = "模糊查询字段名", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "searchFieldValue", value = "模糊查询字段值", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "orderFieldName", value = "排序字段值", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "orderMethod", value = "排序方式。升序为ASC,降序为DESC。默认不排序",allowableValues="ASC,DESC", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1",defaultValue = "1", allowableValues = "range[1,infinity]",paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10",allowableValues = "range(0,infinity]", paramType = "query", dataType = "integer"),
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Map<String, Object>> pageList(@StringRange(entityClass = AuthRole.class) @QueryParam("searchFieldName") String searchFieldName,
                                       @QueryParam("searchFieldValue") String searchFieldValue,
                                       @StringRange(entityClass = AuthRole.class) @QueryParam("orderFieldName") String orderFieldName,
                                       @StringRange({"asc","desc"}) @QueryParam("orderMethod") String orderMethod,
                                       @DefaultValue("1")@QueryParam("pageNum") Integer pageNum,
                                       @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer offset = (pageNum - 1) * pageSize;
        AuthRoleSelectVo selectVo = AuthRoleSelectVo.builder()
                .searchFieldName(searchFieldName)
                .searchFieldValue(searchFieldValue)
                .orderFieldName(orderFieldName)
                .orderMethod(orderMethod)
                .offset(offset)
                .pageSize(pageSize)
                .build();
        Map<String, Object> result;
        if (searchFieldName == null & orderFieldName == null) {
            result = authRoleService.pageList(selectVo);
        } else {
            result = authRoleService.searchList(selectVo);
        }
        return MessageResult.data(result).message("查询成功").build();
    }

    @Path("/list-by-catalog/{roleCatalogId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleCatalogId", value = "角色分组id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "查询角色组下角色", notes = "根据角色组id条件查询角色组下的角色")
    public MessageResult<List<AuthRole>> listByPost(@PathParam("roleCatalogId") String roleCatalogId) {
        return MessageResult.data(authRoleService.listByRoleCatalog(roleCatalogId)).message("查询成功").build();
    }

    @ApiOperation(value = "查询角色分组下角色的数量", notes = "根据角色分组id条件查询角色的数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleCatalogId", value = "角色分组id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/role-catalog-id/{roleCatalogId}")
    public MessageResult<Integer> countByRoleCatalogId(@NotEmpty @PathParam("roleCatalogId") String roleCatalogId) {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        return MessageResult.data(authRoleService.countByRoleCatalogIdAndTenantId(roleCatalogId, tenantId)).message("查询成功").build();
    }

    @ApiOperation(value = "新增角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authRole", value = "角色", dataTypeClass = AuthRole.class, paramType = "body",required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> insertAuthRole(@Valid AuthRole authRole) {
        authRoleService.insertAuthRole(authRole);
        return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @ApiOperation(value = "批量新增角色")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    public MessageResult<Void> batchInsert(@Valid ValidList<AuthRole> authRoles) {
        authRoleService.batchInsert(authRoles);
        return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleCatalogId", value = "角色分组id", paramType = "path", dataType = "string", required = true),
            @ApiImplicitParam(name = "roleId", value = "角色id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "删除角色", notes = "根据角色组id和角色id删除角色")
    @Path("/{roleCatalogId}/{roleId}")
    public MessageResult<Void> deleteAuthRole(@PathParam("roleCatalogId") String roleCatalogId, @PathParam("roleId") String roleId) {
        authRoleService.deleteAuthRole(roleCatalogId, roleId);
        return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

    @ApiOperation(value = "批量删除角色", notes = "根据角色组id和角色id集合批量删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleCatalogId", value = "角色分组id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{roleCatalogId}")
    public MessageResult<Void> batchDelete(@PathParam("roleCatalogId") String roleCatalogId, List<String> roleIds) {
        authRoleService.batchDelete(roleCatalogId, roleIds);
        return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

    @ApiOperation(value = "更新角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authRole", value = "角色", dataTypeClass = AuthRole.class, paramType = "body",required = true),
            @ApiImplicitParam(name = "roleId", value = "角色id", paramType = "path", dataType = "string", required = true)
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{roleId}")
    public MessageResult<Void> updateAuthRole(@Valid AuthRole authRole, @PathParam("roleId") String roleId) {
        authRole.setRoleId(roleId);
        authRoleService.updateAuthRole(authRole);
        return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }
}

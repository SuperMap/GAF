/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.AuthUserRole;
import com.supermap.gaf.authority.service.AuthUserRoleService;
import com.supermap.gaf.authority.valid.ValidList;
import com.supermap.gaf.authority.vo.AuthUserRoleSelectVo;
import com.supermap.gaf.authority.vo.TreeVo;
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
 * @author C
 * @date:2021/3/25
 */
@Component
@Api(value = "用户角色接口")
public class AuthUserRoleResource {
    private final AuthUserRoleService authUserRoleService;

    public AuthUserRoleResource(AuthUserRoleService authUserRoleService) {
        this.authUserRoleService = authUserRoleService;
    }

    @ApiOperation(value = "查询用户角色详情", notes = "根据用户角色id查询用户角色详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userRoleId", value = "用户角色id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{userRoleId}")
    public MessageResult<AuthUserRole> getById(@PathParam("userRoleId") String userRoleId) {
        return MessageResult.data(authUserRoleService.getById(userRoleId)).message("查询成功").build();
    }

    @ApiOperation(value = "条件查询用户角色")
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
    public MessageResult<Map<String, Object>> pageList(@StringRange(entityClass = AuthUserRole.class) @QueryParam("searchFieldName") String searchFieldName,
                                                       @QueryParam("searchFieldValue") String searchFieldValue,
                                                       @StringRange(entityClass = AuthUserRole.class) @QueryParam("orderFieldName") String orderFieldName,
                                                       @StringRange({"asc", "desc"}) @QueryParam("orderMethod") String orderMethod,
                                                       @DefaultValue("1") @QueryParam("pageNum") Integer pageNum,
                                                       @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer offset = (pageNum - 1) * pageSize;
        AuthUserRoleSelectVo selectVo = AuthUserRoleSelectVo.builder()
                .searchFieldName(searchFieldName)
                .searchFieldValue(searchFieldValue)
                .orderFieldName(orderFieldName)
                .orderMethod(orderMethod)
                .offset(offset)
                .pageSize(pageSize)
                .build();
        Map<String, Object> result;
        if (searchFieldName == null && orderFieldName == null) {
            result = authUserRoleService.pageList(selectVo);
        } else {
            result = authUserRoleService.searchList(selectVo);
        }
        return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "查询角色树和用户已分配的角色id集合", notes = "返回的数据中rootTreeNodes表示角色树,checkedKeys表示当前用户已分配的角色id集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "query", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/tree")
    public MessageResult<TreeVo> getUserRoleTree(@QueryParam("userId") String userId) {
        return MessageResult.data(authUserRoleService.getUserRoleTree(userId)).message("查询成功").build();
    }

    @ApiOperation(value = "查询用户已绑定的用户角色", notes = "根据用户id查询其绑定的用户角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/list-by-user/{userId}")
    public MessageResult<List<AuthUserRole>> listByPost(@PathParam("userId") String userId) {
        return MessageResult.data(authUserRoleService.listByUser(userId)).message("查询成功").build();
    }

    @ApiOperation(value = "批量更新用户角色关联关系", notes = "根据用户id查询已绑定的用户角色，与传入的用户角色对比进行更新。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "path", dataType = "string", required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch/{userId}")
    public MessageResult<Void> batchUpdate(@Valid ValidList<AuthUserRole> authUserRoles, @PathParam("userId") String userId) {
        authUserRoleService.batchUpdate(authUserRoles, userId);
        return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @ApiOperation(value = "删除用户角色", notes = "根据用户角色id删除用户角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userRoleId", value = "用户角色id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{userRoleId}")
    public MessageResult<Void> deleteAuthUserRole(@PathParam("userRoleId") String userRoleId) {
        authUserRoleService.deleteAuthUserRole(userRoleId);
        return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

    @ApiOperation(value = "批量删除用户角色", notes = "根据用户角色id集合批量删除用户角色")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> batchDelete(List<String> userRoleIds) {
        authUserRoleService.batchDelete(userRoleIds);
        return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

    @ApiOperation(value = "更新用户角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authUserRole", value = "用户角色", dataTypeClass = AuthUserRole.class, paramType = "body", required = true),
            @ApiImplicitParam(name = "userRoleId", value = "用户角色id", paramType = "path", dataType = "string", required = true)
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{userRoleId}")
    public MessageResult<Void> updateAuthUserRole(AuthUserRole authUserRole, @PathParam("userRoleId") String userRoleId) {
        authUserRole.setUserRoleId(userRoleId);
        authUserRoleService.updateAuthUserRole(authUserRole);
        return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }


}

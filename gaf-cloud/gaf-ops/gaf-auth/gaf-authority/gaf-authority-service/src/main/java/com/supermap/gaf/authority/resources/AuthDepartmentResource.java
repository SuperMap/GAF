/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.AuthDepartment;
import com.supermap.gaf.authority.service.AuthDepartmentService;
import com.supermap.gaf.authority.valid.ValidList;
import com.supermap.gaf.authority.vo.AuthDepartmentSelectVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.commontypes.ShiroUser;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.sys.mgt.commontype.SysCatalog;
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
 * 部门接口
 *
 * @author zhm
 * @date:2021/3/25
 */
@Component
@Api(value = "部门接口")
public class AuthDepartmentResource {
    private final AuthDepartmentService authDepartmentService;

    public AuthDepartmentResource(AuthDepartmentService authDepartmentService) {
        this.authDepartmentService = authDepartmentService;
    }

    @ApiOperation(value = "查询部门", notes = "根据id查询部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "departmentId", value = "部门id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{departmentId}")
    public MessageResult<AuthDepartment> getById(@PathParam("departmentId") String departmentId) {
        return MessageResult.data(authDepartmentService.getById(departmentId)).message("查询成功").build();
    }

    @ApiOperation(value = "分页条件查询部门")
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
    public MessageResult<Map<String, Object>> pageList(@StringRange(entityClass = AuthDepartment.class) @QueryParam("searchFieldName") String searchFieldName,
                                                       @QueryParam("searchFieldValue") String searchFieldValue,
                                                       @StringRange(entityClass = AuthDepartment.class) @QueryParam("orderFieldName") String orderFieldName,
                                                       @StringRange({"asc", "desc"}) @QueryParam("orderMethod") String orderMethod,
                                                       @DefaultValue("1") @QueryParam("pageNum") Integer pageNum,
                                                       @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer offset = (pageNum - 1) * pageSize;
        AuthDepartmentSelectVo selectVo = AuthDepartmentSelectVo.builder()
                .searchFieldName(searchFieldName)
                .searchFieldValue(searchFieldValue)
                .orderFieldName(orderFieldName)
                .orderMethod(orderMethod)
                .offset(offset)
                .pageSize(pageSize)
                .build();
        Map<String, Object> result;
        if (searchFieldName == null & orderFieldName == null) {
            result = authDepartmentService.pageList(selectVo);
        } else {
            result = authDepartmentService.searchList(selectVo);
        }
        return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "查询某部门", notes = "根据部门名称模糊查询当前用户所属租户下的部门。若未传入search（岗位名）则查询所属租户下的所有部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "岗位名", example = "开发岗", paramType = "query", dataType = "string")
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/list")
    public MessageResult<List<AuthDepartment>> listDepartment(@QueryParam("search") String search) {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        return MessageResult.data(authDepartmentService.listDepartment(search, tenantId)).message("查询成功").build();
    }

    @ApiOperation(value = "查询部门树节点", notes = "查询当前用户所属租户下的所有部门，然后转换为树节点（未组织为树形结构）")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/list-tree-node")
    public MessageResult<List<TreeNode>> listDepartmentTreeNode() {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        return MessageResult.data(authDepartmentService.listDepartmentTreeNode(tenantId)).message("查询成功").build();
    }

    @ApiOperation(value = "查询路径", notes = "查询根部门到当前部门的路径（包括当前部门）。返回结果是部门id的有序集合，表示路径")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "departmentId", value = "部门id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/department-path/{departmentId}")
    public MessageResult<List<String>> getDepartmentPath(@NotEmpty @PathParam("departmentId") String departmentId) {
        return MessageResult.data(authDepartmentService.getDepartmentPath(departmentId)).message("查询成功").build();
    }

    @ApiOperation(value = "新增部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authDepartment", value = "部门", dataTypeClass = AuthDepartment.class, paramType = "body", required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<AuthDepartment> insertAuthDepartment(@Valid AuthDepartment authDepartment) {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        authDepartment.setTenantId(Objects.requireNonNull(shiroUser).getTenantId());
        return MessageResult.data(authDepartmentService.insertAuthDepartment(authDepartment)).message("新增操作成功").build();
    }

    @ApiOperation(value = "批量新增部门")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    public MessageResult<Void> batchInsert(@Valid ValidList<AuthDepartment> authDepartments) {
        authDepartmentService.batchInsert(authDepartments);
        return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @ApiOperation(value = "删除部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "departmentId", value = "部门id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{departmentId}")
    public MessageResult<AuthDepartment> deleteAuthDepartment(@PathParam("departmentId") String departmentId) {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        AuthDepartment deletedDepartment = authDepartmentService.deleteAuthDepartment(tenantId, departmentId);
        return MessageResult.successe(AuthDepartment.class).data(deletedDepartment).status(200).message("删除操作成功").build();
    }

    @ApiOperation(value = "批量删除部门", notes = "根据id集合批量删除部门")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> batchDelete(List<String> departmentIds) {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        authDepartmentService.batchDelete(tenantId, departmentIds);
        return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

    @ApiOperation(value = "更新部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authDepartment", value = "部门", paramType = "body", dataTypeClass = SysCatalog.class, required = true),
            @ApiImplicitParam(name = "departmentId", value = "部门id", paramType = "path", dataType = "string", required = true)
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{departmentId}")
    public MessageResult<AuthDepartment> updateAuthDepartment(@Valid AuthDepartment authDepartment, @PathParam("departmentId") String departmentId) {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        authDepartment.setDepartmentId(departmentId);
        authDepartment.setTenantId(tenantId);
        return MessageResult.data(authDepartmentService.updateAuthDepartment(authDepartment)).message("更新操作成功").build();
    }
}

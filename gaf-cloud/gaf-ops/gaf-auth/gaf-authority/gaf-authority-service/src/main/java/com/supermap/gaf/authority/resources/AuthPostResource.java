/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.AuthPost;
import com.supermap.gaf.authority.service.AuthPostService;
import com.supermap.gaf.authority.vo.AuthPostSelectVo;
import com.supermap.gaf.authority.vo.SelectOptionVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
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
 * @author zhm
 * @date:2021/3/25
 */
@Component
@Api(value = "岗位接口")
public class AuthPostResource {
    private final AuthPostService authPostService;

    public AuthPostResource(AuthPostService authPostService) {
        this.authPostService = authPostService;
    }

    @ApiOperation(value = "查询岗位", notes = "根据id查询岗位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "岗位id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{postId}")
    public MessageResult<AuthPost> getById(@PathParam("postId") String postId) {
        return MessageResult.data(authPostService.getById(postId)).message("查询成功").build();
    }

    @ApiOperation(value = "分页条件查询岗位")
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
    public MessageResult<Map<String, Object>> pageList(@StringRange(entityClass = AuthPost.class) @QueryParam("searchFieldName") String searchFieldName,
                                                       @QueryParam("searchFieldValue") String searchFieldValue,
                                                       @StringRange(entityClass = AuthPost.class) @QueryParam("orderFieldName") String orderFieldName,
                                                       @StringRange({"asc", "desc"}) @QueryParam("orderMethod") String orderMethod,
                                                       @DefaultValue("1") @QueryParam("pageNum") Integer pageNum,
                                                       @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer offset = (pageNum - 1) * pageSize;
        AuthPostSelectVo selectVo = AuthPostSelectVo.builder()
                .searchFieldName(searchFieldName)
                .searchFieldValue(searchFieldValue)
                .orderFieldName(orderFieldName)
                .orderMethod(orderMethod)
                .offset(offset)
                .pageSize(pageSize)
                .build();
        Map<String, Object> result;
        if (searchFieldName == null & orderFieldName == null) {
            result = authPostService.pageList(selectVo);
        } else {
            result = authPostService.searchList(selectVo);
        }
        return MessageResult.data(result).status(200).message("查询成功").build();
    }

    @ApiOperation(value = "查询部门下的岗位", notes = "查询当前用户所属的租户下的部门岗位。若传入岗位名可增加岗位名的模糊查询条件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "岗位名", example = "开发岗", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "departmentId", value = "部门id", paramType = "query", dataType = "string", required = true),
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/list")
    public MessageResult<List<AuthPost>> listPost(@QueryParam("search") String search, @QueryParam("departmentId") String departmentId) {
        List<AuthPost> result = authPostService.listPost(search, departmentId);
        return MessageResult.data(result).status(200).message("查询成功").build();
    }

    @ApiOperation(value = "查询部门岗位树", notes = "返回的树节点数据中，type为2表示部门节点,type为3表示岗位节点")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/tree")
    public MessageResult<List<TreeNode>> getPostTree() {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        List<TreeNode> result = authPostService.getPostTree(tenantId);
        return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "查询部门下岗位可选项", notes = "查询部门下的岗位,然后将其转换为选择项对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "departmentId", value = "部门id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/departments/{departmentId}")
    public MessageResult<List<SelectOptionVo>> getPostOptionsByDepartment(@NotEmpty @PathParam("departmentId") String departmentId) {
        List<SelectOptionVo> postOptions = authPostService.listPostOptionsByDepartment(departmentId);
        return MessageResult.data(postOptions).message("查询成功").build();
    }

    @ApiOperation(value = "新增岗位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authPost", value = "岗位", dataTypeClass = AuthPost.class, paramType = "body", required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<AuthPost> insertAuthPost(@Valid AuthPost authPost) {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        String tenantId = Objects.requireNonNull(shiroUser).getTenantId();
        authPost.setTenantId(tenantId);
        authPost.setCreatedBy(shiroUser.getAuthUser().getName());
        authPost.setUpdatedBy(shiroUser.getAuthUser().getName());
        AuthPost insertedAuthPost = authPostService.insertAuthPost(authPost);
        return MessageResult.successe(AuthPost.class).data(insertedAuthPost).status(200).message("新增操作成功").build();
    }

    @ApiOperation(value = "删除岗位", notes = "根据部门id和岗位id删除岗位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "departmentId", value = "部门id", paramType = "path", dataType = "string", required = true),
            @ApiImplicitParam(name = "postId", value = "岗位id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{departmentId}/{postId}")
    public MessageResult<AuthPost> deleteAuthPost(@PathParam("departmentId") String departmentId, @PathParam("postId") String postId) {
        AuthPost authPost = authPostService.deleteAuthPost(departmentId, postId);
        return MessageResult.data(authPost).message("删除操作成功").build();
    }

    @ApiOperation(value = "批量删除岗位", notes = "根据部门id和岗位id集合批量删除岗位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "departmentId", value = "部门id", paramType = "path", dataType = "string", required = true),
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{departmentId}")
    public MessageResult<Void> batchDelete(@PathParam("departmentId") String departmentId, List<String> postIds) {
        authPostService.batchDelete(departmentId, postIds);
        return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

    @ApiOperation(value = "更新岗位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "岗位id", paramType = "path", dataType = "string", required = true)
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{postId}")
    public MessageResult<AuthPost> updateAuthPost(@Valid AuthPost authPost, @PathParam("postId") String postId) {
        authPost.setPostId(postId);
        AuthPost updatedAuthPost = authPostService.updateAuthPost(authPost);
        return MessageResult.data(updatedAuthPost).message("更新操作成功").build();
    }
}

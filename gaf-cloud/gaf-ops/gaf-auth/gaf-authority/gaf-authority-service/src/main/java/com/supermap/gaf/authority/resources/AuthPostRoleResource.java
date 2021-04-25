/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.AuthPostRole;
import com.supermap.gaf.authority.service.AuthPostRoleService;
import com.supermap.gaf.authority.vo.AuthPostRoleSelectVo;
import com.supermap.gaf.authority.vo.PostRoleVo;
import com.supermap.gaf.authority.vo.TreeVo;
import com.supermap.gaf.commontypes.MessageResult;
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
 * @date:2021/3/25
 * @author yangdong
 */
@Component
@Api(value = "岗位角色接口")
public class AuthPostRoleResource {
    private final AuthPostRoleService authPostRoleService;

    public AuthPostRoleResource(AuthPostRoleService authPostRoleService) {
        this.authPostRoleService = authPostRoleService;
    }

    @ApiOperation(value = "查询岗位角色", notes = "根据岗位角色id查询岗位角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postRoleId", value = "岗位角色id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{postRoleId}")
    public MessageResult<AuthPostRole> getById(@PathParam("postRoleId") String postRoleId) {
        return MessageResult.data(authPostRoleService.getById(postRoleId)).message("查询成功").build();
    }

    @ApiOperation(value = "条件查询岗位角色", notes = "条件查询岗位角色")
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
    public MessageResult<Map<String, Object>> pageList(@QueryParam("searchFieldName") String searchFieldName,
                                       @QueryParam("searchFieldValue") String searchFieldValue,
                                       @QueryParam("orderFieldName") String orderFieldName,
                                       @QueryParam("orderMethod") String orderMethod,
                                       @QueryParam("pageNum") Integer pageNum,
                                       @DefaultValue("50") @QueryParam("pageSize") Integer pageSize) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer offset = (pageNum - 1) * pageSize;
        AuthPostRoleSelectVo selectVo = AuthPostRoleSelectVo.builder()
                .searchFieldName(searchFieldName)
                .searchFieldValue(searchFieldValue)
                .orderFieldName(orderFieldName)
                .orderMethod(orderMethod)
                .offset(offset)
                .pageSize(pageSize)
                .build();
        Map<String, Object> result;
        if (searchFieldName == null & orderFieldName == null) {
            result = authPostRoleService.pageList(selectVo);
        } else {
            result = authPostRoleService.searchList(selectVo);
        }
        return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "查询角色树和当前岗位绑定的角色id集合", notes = "返回的结果数据中，rootTreeNodes表示角色树，checkedKeys表示当前岗位绑定的角色id集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "岗位id", paramType = "query", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/tree")
    public MessageResult<TreeVo> getRoleTree(@QueryParam("postId") String postId) {
        return MessageResult.data(authPostRoleService.getRoleTree(postId)).message("查询成功").build();
    }

    @ApiOperation(value = "更新某岗位与角色的关联关系", notes = "传入岗位id和角色id列表，查询到该岗位已绑定的角色，与传入的角色id列表做对比更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postRoleVo", value = "岗位角色关联对象", dataTypeClass = PostRoleVo.class, paramType = "body",required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/handle")
    public MessageResult<Void> handlePostRole(PostRoleVo postRoleVo){
        authPostRoleService.handlePostRole(postRoleVo);
        return MessageResult.successe(Void.class).status(200).message("关联操作成功").build();
    }

    @ApiOperation(value = "查询岗位已绑定的岗位角色", notes = "根据岗位id条件查询岗位角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "岗位id", paramType = "query", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/list-by-post/{postId}")
    public MessageResult<List<AuthPostRole>> listByPost(@PathParam("postId") String postId) {
        return MessageResult.data(authPostRoleService.listByPost(postId)).message("查询成功").build();
    }

}

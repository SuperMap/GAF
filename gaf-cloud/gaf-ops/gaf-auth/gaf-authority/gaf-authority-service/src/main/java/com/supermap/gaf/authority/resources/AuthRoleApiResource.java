/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.AuthRoleApi;
import com.supermap.gaf.authority.service.AuthRoleApiService;
import com.supermap.gaf.authority.valid.ValidList;
import com.supermap.gaf.authority.vo.AuthRoleApiSelectVo;
import com.supermap.gaf.authority.vo.RoleApiVo;
import com.supermap.gaf.commontypes.MessageResult;
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
 * 角色接口接口
 * @date:2021/3/25
 * @author yangdong
 */
@Component
@Api(value = "角色接口接口")
public class AuthRoleApiResource {
    private final AuthRoleApiService authRoleApiService;

    public AuthRoleApiResource(AuthRoleApiService authRoleApiService) {
        this.authRoleApiService = authRoleApiService;
    }

    @ApiOperation(value = "查询角色接口", notes = "根据角色接口id查询角色接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleApiId", value = "角色接口id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{roleApiId}")
    public MessageResult<AuthRoleApi> getById(@PathParam("roleApiId") String roleApiId) {
        return MessageResult.data(authRoleApiService.getById(roleApiId)).message("查询成功").build();
    }

    @ApiOperation(value = "分页条件查询角色接口")
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
                                       @DefaultValue("1")@QueryParam("pageNum") Integer pageNum,
                                       @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer offset = (pageNum - 1) * pageSize;
        AuthRoleApiSelectVo selectVo = AuthRoleApiSelectVo.builder()
                .searchFieldName(searchFieldName)
                .searchFieldValue(searchFieldValue)
                .orderFieldName(orderFieldName)
                .orderMethod(orderMethod)
                .offset(offset)
                .pageSize(pageSize)
                .build();
        Map<String, Object> result;
        if (searchFieldName == null & orderFieldName == null) {
            result = authRoleApiService.pageList(selectVo);
        } else {
            result = authRoleApiService.searchList(selectVo);
        }
        return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "查询角色已绑定的角色接口", notes = "根据角色id条件查询角色接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/list-by-role/{roleId}")
    public MessageResult<List<AuthRoleApi>> listByRole(@PathParam("roleId") String roleId) {
        return MessageResult.data(authRoleApiService.listByRole(roleId)).message("查询成功").build();
    }

    @ApiOperation(value = "更新某角色与接口(api)的关联关系", notes = "传入角色id和接口(api)id集合，查询到该角色已绑定的接口(api)，与传入的接口id列表做对比更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleApiVo", value = "角色与接口(api)的关联对象", dataTypeClass = RoleApiVo.class, paramType = "body",required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/handle")
    public MessageResult<Void> handleRoleApi(RoleApiVo roleApiVo){
        authRoleApiService.handleRoleApi(roleApiVo);
        return MessageResult.successe(Void.class).status(200).message("关联操作成功").build();
    }


    @ApiOperation(value = "批量新增角色接口")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    public MessageResult<Void> batchInsert(@Valid ValidList<AuthRoleApi> authRoleApis) {
        authRoleApiService.batchInsertRoleApi(authRoleApis);
        return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

}

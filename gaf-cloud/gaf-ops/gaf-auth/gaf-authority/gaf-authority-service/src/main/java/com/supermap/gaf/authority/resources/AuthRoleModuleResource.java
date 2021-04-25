/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.AuthRoleModule;
import com.supermap.gaf.authority.service.AuthRoleModuleService;
import com.supermap.gaf.authority.vo.AuthRoleModuleSelectVo;
import com.supermap.gaf.authority.vo.RoleModuleVo;
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
 * 角色模块接口
 * @date:2021/3/25
 * @author  yangdong
 */
@Component
@Api(value = "角色模块接口")
public class AuthRoleModuleResource {
    private final AuthRoleModuleService authRoleModuleService;

    public AuthRoleModuleResource(AuthRoleModuleService authRoleModuleService) {
        this.authRoleModuleService = authRoleModuleService;
    }

    @ApiOperation(value = "查询角色模块", notes = "根据角色模块id查询角色模块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleModuleId", value = "角色模块id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{roleModuleId}")
    public MessageResult<AuthRoleModule> getById(@PathParam("roleModuleId") String roleModuleId) {
        AuthRoleModule authRoleModule = authRoleModuleService.getById(roleModuleId);
        return MessageResult.successe(AuthRoleModule.class).data(authRoleModule).status(200).message("查询成功").build();
    }

    @ApiOperation(value = "条件查询角色模块", notes = "条件查询角色模块")
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
        AuthRoleModuleSelectVo selectVo = AuthRoleModuleSelectVo.builder()
                .searchFieldName(searchFieldName)
                .searchFieldValue(searchFieldValue)
                .orderFieldName(orderFieldName)
                .orderMethod(orderMethod)
                .offset(offset)
                .pageSize(pageSize)
                .build();
        Map<String, Object> result;
        if (searchFieldName == null & orderFieldName == null) {
            result = authRoleModuleService.pageList(selectVo);
        } else {
            result = authRoleModuleService.searchList(selectVo);
        }
        return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "查询角色已绑定的模块", notes = "根据角色id条件查询角色模块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/list-by-role/{roleId}")
    public MessageResult<List<AuthRoleModule>> listByRole(@PathParam("roleId") String roleId) {
        return MessageResult.data(authRoleModuleService.listByRole(roleId)).message("查询成功").build();
    }

    @ApiOperation(value = "更新某角色与模块的关联关系",notes = "传入角色id和模块id列表，查询到该角色已绑定的模块，与传入的模块id列表做对比更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleModuleVo", value = "角色模块关联对象", dataTypeClass = RoleModuleVo.class, paramType = "body",required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/handle")
    public MessageResult<Void> handleRoleModule(RoleModuleVo roleModuleVo){
        authRoleModuleService.handleRoleModule(roleModuleVo);
        return MessageResult.successe(Void.class).status(200).message("关联操作成功").build();
    }
}

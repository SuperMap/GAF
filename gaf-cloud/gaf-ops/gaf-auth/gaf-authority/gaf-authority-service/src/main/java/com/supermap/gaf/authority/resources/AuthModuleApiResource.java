/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.AuthModuleApi;
import com.supermap.gaf.authority.service.AuthModuleApiService;
import com.supermap.gaf.authority.vo.AuthModuleApiSelectVo;
import com.supermap.gaf.authority.vo.ModuleApiVo;
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
 * 模块接口(API)关联接口
 *
 * @author zhm
 * @date:2021/3/25
 */
@Component
@Api(value = "模块接口(API)关联接口")
public class AuthModuleApiResource {
    private final AuthModuleApiService authModuleApiService;

    public AuthModuleApiResource(AuthModuleApiService authModuleApiService) {
        this.authModuleApiService = authModuleApiService;
    }

    @ApiOperation(value = "查询模块接口(API)", notes = "根据id查询模块接口(API)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "moduleApiId", value = "模块接口(API)的id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{moduleApiId}")
    public MessageResult<AuthModuleApi> getById(@PathParam("moduleApiId") String moduleApiId) {
        return MessageResult.data(authModuleApiService.getById(moduleApiId)).message("查询成功").build();
    }

    @ApiOperation(value = "分页条件查询模块接口(API)")
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
    public MessageResult<Map<String, Object>> pageList(@StringRange(entityClass = AuthModuleApi.class) @QueryParam("searchFieldName") String searchFieldName,
                                                       @QueryParam("searchFieldValue") String searchFieldValue,
                                                       @StringRange(entityClass = AuthModuleApi.class) @QueryParam("orderFieldName") String orderFieldName,
                                                       @StringRange({"asc", "desc"}) @QueryParam("orderMethod") String orderMethod,
                                                       @DefaultValue("1") @QueryParam("pageNum") Integer pageNum,
                                                       @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer offset = (pageNum - 1) * pageSize;
        AuthModuleApiSelectVo selectVo = AuthModuleApiSelectVo.builder()
                .searchFieldName(searchFieldName)
                .searchFieldValue(searchFieldValue)
                .orderFieldName(orderFieldName)
                .orderMethod(orderMethod)
                .offset(offset)
                .pageSize(pageSize)
                .build();
        Map<String, Object> result;
        if (searchFieldName == null & orderFieldName == null) {
            result = authModuleApiService.pageList(selectVo);
        } else {
            result = authModuleApiService.searchList(selectVo);
        }
        return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "查询某模块绑定的模块接口(API)", notes = "根据模块id查询模块接口(API)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "moduleId", value = "模块id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/module/{moduleId}")
    public MessageResult<List<AuthModuleApi>> getByModuleId(@PathParam("moduleId") String moduleId) {
        return MessageResult.data(authModuleApiService.getByModuleId(moduleId, true)).message("查询成功").build();
    }

    @ApiOperation(value = "更新某模块与接口(API)的关联关系", notes = "传入模块id和接口(API)的id列表，查询到该模块已绑定的接口(API)，与传入的接口(API)的id列表做对比更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "moduleApiVo", value = "模块接口(API)关联对象", dataTypeClass = ModuleApiVo.class, paramType = "body", required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/handle")
    public MessageResult<Void> handleModuleApi(ModuleApiVo moduleApiVo) {
        authModuleApiService.handleModuleApi(moduleApiVo);
        return MessageResult.successe(Void.class).status(200).message("关联操作成功").build();
    }

}

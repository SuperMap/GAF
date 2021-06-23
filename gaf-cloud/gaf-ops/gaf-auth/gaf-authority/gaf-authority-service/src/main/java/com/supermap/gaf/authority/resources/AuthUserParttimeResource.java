/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.AuthUserParttime;
import com.supermap.gaf.authority.service.AuthUserParttimeService;
import com.supermap.gaf.authority.vo.AuthUserParttimeSelectVo;
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
import java.util.Map;

/**
 * 用户挂职接口
 * @author c
 * @date:2021/3/25
 *
 */
@Component
@Api(value = "用户挂职接口")
public class AuthUserParttimeResource {
    private final AuthUserParttimeService authUserParttimeService;

    public AuthUserParttimeResource(AuthUserParttimeService authUserParttimeService) {
        this.authUserParttimeService = authUserParttimeService;
    }

    @ApiOperation(value = "查询用户挂职", notes = "根据用户挂职id查询用户挂职")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userParttimeId", value = "用户挂职id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{userParttimeId}")
    public MessageResult<AuthUserParttime> getById(@PathParam("userParttimeId") String userParttimeId) {
        return MessageResult.data(authUserParttimeService.getById(userParttimeId)).message("查询成功").build();
    }

    @ApiOperation(value = "条件查询用户挂职")
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
    public MessageResult<Map<String, Object>> pageList(@StringRange(entityClass = AuthUserParttime.class) @QueryParam("searchFieldName") String searchFieldName,
                                       @QueryParam("searchFieldValue") String searchFieldValue,
                                       @StringRange(entityClass = AuthUserParttime.class) @QueryParam("orderFieldName") String orderFieldName,
                                       @StringRange({"asc","desc"}) @QueryParam("orderMethod") String orderMethod,
                                       @DefaultValue("1")@QueryParam("pageNum") Integer pageNum,
                                       @DefaultValue("10") @QueryParam("pageSize") Integer pageSize,
                                        @QueryParam("departmentName") String departmentName) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        Integer offset = (pageNum - 1) * pageSize;
        AuthUserParttimeSelectVo selectVo = AuthUserParttimeSelectVo.builder()
                .searchFieldName(searchFieldName)
                .searchFieldValue(searchFieldValue)
                .orderFieldName(orderFieldName)
                .orderMethod(orderMethod)
                .offset(offset)
                .pageSize(pageSize)
                .departmentName(departmentName)
                .build();
        Map<String, Object> result;
        result = authUserParttimeService.searchList(selectVo);
        return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "新增用户挂职")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authUserParttime", value = "用户挂职", dataTypeClass = AuthUserParttime.class, paramType = "body",required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> insertAuthUserParttime(@Valid AuthUserParttime authUserParttime) {
        authUserParttimeService.insertAuthUserParttime(authUserParttime);
        return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @ApiOperation(value = "删除用户挂职", notes = "根据用户挂职id删除用户挂职")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userParttimeId", value = "用户挂职id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{userParttimeId}")
    public MessageResult<Void> deleteAuthUserParttime(@PathParam("userParttimeId") String userParttimeId) {
        authUserParttimeService.deleteAuthUserParttime(userParttimeId);
        return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

    @ApiOperation(value = "更新用户挂职")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authUserParttime", value = "用户挂职", dataTypeClass = AuthUserParttime.class, paramType = "body",required = true),
            @ApiImplicitParam(name = "userParttimeId", value = "用户挂职id", paramType = "path", dataType = "string", required = true)
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{userParttimeId}")
    public MessageResult<Void> updateAuthUserParttime(@Valid AuthUserParttime authUserParttime, @PathParam("userParttimeId") String userParttimeId) {
        authUserParttime.setUserParttimeId(userParttimeId);
        authUserParttimeService.updateAuthUserParttime(authUserParttime);
        return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }

}

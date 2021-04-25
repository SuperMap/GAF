/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.AuthP3MappingRule;
import com.supermap.gaf.authority.service.AuthP3MappingRuleService;
import com.supermap.gaf.authority.vo.AuthP3MappingRuleSelectVo;
import com.supermap.gaf.commontypes.MessageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;


/**
 * 第三方映射规则接口
 * @date:2021/3/25
 * @author yangdong
 */
@Component
@Api(value = "第三方映射规则接口")
public class AuthP3MappingRuleResource {
    private final AuthP3MappingRuleService authP3MappingRuleService;

    public AuthP3MappingRuleResource(AuthP3MappingRuleService authP3MappingRuleService) {
        this.authP3MappingRuleService = authP3MappingRuleService;
    }

    @ApiOperation(value = "新增第三方映射规则")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authP3MappingRule", value = "第三方映射规则", dataTypeClass = AuthP3MappingRule.class, paramType = "body",required = true)
    })
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<AuthP3MappingRule> insertAuthP3MappingRule(AuthP3MappingRule authP3MappingRule) {
        AuthP3MappingRule data = authP3MappingRuleService.insertAuthP3MappingRule(authP3MappingRule);
        return MessageResult.data(data).build();
    }
    @ApiOperation(value = "删除第三方映射规则", notes = "根据id删除第三方映射规则")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mappingRuleId", value = "第三方映射规则id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{mappingRuleId}")
    public MessageResult<Void> deleteAuthP3MappingRule(@PathParam("mappingRuleId") String mappingRuleId) {
        authP3MappingRuleService.deleteAuthP3MappingRule(mappingRuleId);
        return MessageResult.successe(Void.class).build();
    }

    @ApiOperation(value = "更新第三方映射规则", notes = "更新第三方映射规则")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authP3MappingRule", value = "第三方映射规则", dataTypeClass = AuthP3MappingRule.class, paramType = "body",required = true),
            @ApiImplicitParam(name = "mappingRuleId", value = "第三方映射规则id", paramType = "path", dataType = "string", required = true)
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{mappingRuleId}")
    public MessageResult<AuthP3MappingRule> updateAuthP3MappingRule(AuthP3MappingRule authP3MappingRule, @PathParam("mappingRuleId") String mappingRuleId) {
        authP3MappingRule.setMappingRuleId(mappingRuleId);
        return MessageResult.data(authP3MappingRuleService.updateAuthP3MappingRule(authP3MappingRule)).build();
    }

    @ApiOperation(value = "查询第三方映射规则", notes = "根据id查询第三方映射规则")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mappingRuleId", value = "第三方映射规则id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{mappingRuleId}")
    public MessageResult<AuthP3MappingRule> selectById(@PathParam("mappingRuleId") String mappingRuleId) {
        return MessageResult.data(authP3MappingRuleService.selectById(mappingRuleId)).build();
    }

    @ApiOperation(value = "批量删除第三方映射规则", notes = "根据id集合批量删除第三方映射规则")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> batchDelete(List<String> mappingRuleIds) {
        authP3MappingRuleService.batchDelete(mappingRuleIds);
        return MessageResult.successe(Void.class).build();
    }

    @ApiOperation(value = "分页条件查询第三方映射规则")
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
        AuthP3MappingRuleSelectVo selectVo = AuthP3MappingRuleSelectVo.builder()
                .searchFieldName(searchFieldName)
                .searchFieldValue(searchFieldValue)
                .orderFieldName(orderFieldName)
                .orderMethod(orderMethod)
                .offset(offset)
                .pageSize(pageSize)
                .build();

        Map<String, Object> result;
        if (searchFieldName == null & orderFieldName == null) {
            result = authP3MappingRuleService.pageList(selectVo);
        } else {
            result = authP3MappingRuleService.searchList(selectVo);
        }
        return MessageResult.data(result).build();
    }

    @ApiOperation(value = "条件查询第三方映射规则", notes = "根据映射类型、资源id查询第三方映射规则。映射类型暂时有1:租户，2：部门，3：用户,而资源id则是对应的id,例如租户id、部门id、用户id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mappingType", value = "映射类型.暂时有1:租户，2：部门，3：用户", example = "1", allowableValues = "1,2,3",paramType = "path", dataType = "string", required = true),
            @ApiImplicitParam(name = "id", value = "资源id。对应映射类型,例如租户id、部门id、用户id", example = "xxxx", paramType = "path", dataType = "string",required = true),
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/list-by-type/{mappingType}/{id}")
    public MessageResult<List<AuthP3MappingRule>> listByType(@NotNull @PathParam("mappingType") String mappingType, @NotNull @PathParam("id") String id) {
        return MessageResult.data(authP3MappingRuleService.listByMappingType(mappingType, id)).build();
    }

    @ApiOperation(value = "批量新增第三方映射规则")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    public MessageResult<Void> batchInsert(List<AuthP3MappingRule> authP3MappingRules) {
        authP3MappingRuleService.batchInsert(authP3MappingRules);
        return MessageResult.successe(Void.class).build();
    }

}

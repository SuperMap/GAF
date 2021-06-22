/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.client.AuthResourceApiClient;
import com.supermap.gaf.authority.commontype.AuthResourceApi;
import com.supermap.gaf.authority.service.AuthResourceApiService;
import com.supermap.gaf.authority.vo.AuthResourceApiSelectVo;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.validator.StringRange;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * 接口(api)接口
 * @date:2021/3/25
 * @author zhm
 */
@Component
@Api(value = "接口(api)接口")
public class AuthResourceApiResource implements AuthResourceApiClient {
    private final AuthResourceApiService authResourceApiService;

    public AuthResourceApiResource(AuthResourceApiService authResourceApiService) {
        this.authResourceApiService = authResourceApiService;
    }

    @ApiOperation(value = "查询接口(api)资源", notes = "根据id查询接口(api)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceApiId", value = "api资源id", paramType = "path", dataType = "string", required = true)
    })
	@GET
    @Produces({MediaType.APPLICATION_JSON})
	@Path("/{resourceApiId}")
    public MessageResult<AuthResourceApi> getById(@PathParam("resourceApiId")String resourceApiId){
		return MessageResult.data(authResourceApiService.getById(resourceApiId)).message("查询成功").build();
    }
    /**
     * 等值条件查询
     * @param query 查询参数
     * @return 接口列表
     */
    @ApiOperation(value = "等值条件查询接口", notes = "注意所有参数非必须，以此为准。但是至少有一个参数")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/condition")
    @Override
    public MessageResult<List<AuthResourceApi>> list(@BeanParam AuthResourceApi query) {
        return MessageResult.data(authResourceApiService.list(query)).build();
    }

    @ApiOperation(value = "分页条件查询接口(api)资源")
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
    public MessageResult<Map<String,Object>> pageList(@StringRange(entityClass = AuthResourceApi.class) @QueryParam("searchFieldName")String searchFieldName,
										@QueryParam("searchFieldValue")String searchFieldValue,
										@StringRange(entityClass = AuthResourceApi.class) @QueryParam("orderFieldName")String orderFieldName,
										@StringRange({"asc","desc"}) @QueryParam("orderMethod")String orderMethod,
                                        @DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
										@DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        if(pageNum==null || pageNum<1) {
            pageNum=1;
        }
        Integer offset = (pageNum-1)*pageSize;
        AuthResourceApiSelectVo selectVo = AuthResourceApiSelectVo.builder()
                                        .searchFieldName(searchFieldName)
										.searchFieldValue(searchFieldValue)
										.orderFieldName(orderFieldName)
										.orderMethod(orderMethod)
										.offset(offset)
                                        .pageSize(pageSize)
                                        .build(); 
        Map<String,Object> result;
		if(searchFieldName==null & orderFieldName==null) {
			result = authResourceApiService.pageList(selectVo);
		} else {
			result = authResourceApiService.searchList(selectVo);
		}
		return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "查询某接口(Api)分组下接口(api)的数量", notes = "根据接口(Api)分组id条件查询接口(Api)的数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "apiCatalogId", value = "Api分组id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/api-catalog-id/{apiCatalogId}")
    public MessageResult<Integer> countByApiCatalogId(@NotEmpty @PathParam("apiCatalogId") String apiCatalogId) {
	    return MessageResult.data(authResourceApiService.countByApiCatalogId(apiCatalogId)).message("查询成功").build();
    }

    @ApiOperation(value = "新增接口(api)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authResourceApi", value = "接口(api)", dataTypeClass = AuthResourceApi.class, paramType = "body",required = true)
    })
	@POST
    @Produces({MediaType.APPLICATION_JSON})
    @Override
    public MessageResult<AuthResourceApi> insertAuthResourceApi(AuthResourceApi authResourceApi){
        return MessageResult.data(authResourceApiService.insertAuthResourceApi(authResourceApi)).message("新增操作成功").build();
    }

    @ApiOperation(value = "批量新增接口(api)")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    public MessageResult<Void> batchInsert(List<AuthResourceApi> authResourceApis){
        authResourceApiService.batchInsert(authResourceApis);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @ApiOperation(value = "删除接口(api)", notes = "根据id删除接口(api)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceApiId", value = "api资源id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
	@Path("/{resourceApiId}")
    @Override
    public MessageResult<Void> deleteAuthResourceApi(@NotEmpty @PathParam("resourceApiId")String resourceApiId){
        authResourceApiService.deleteAuthResourceApi(resourceApiId);
		return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

    @ApiOperation(value = "批量删除接口(api)", notes = "根据id集合批量删除接口(api)")
	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> batchDelete(List<String> resourceApiIds){
        authResourceApiService.batchDelete(resourceApiIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

    @ApiOperation(value = "更新接口(api)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authResourceApi", value = "api资源", dataTypeClass = AuthResourceApi.class, paramType = "body",required = true),
            @ApiImplicitParam(name = "resourceApiId", value = "api资源id", paramType = "path", dataType = "string", required = true)
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
	@Path("/{resourceApiId}")
    @Override
    public MessageResult<Void> updateAuthResourceApi(AuthResourceApi authResourceApi,@NotEmpty @PathParam("resourceApiId")String resourceApiId){
        authResourceApi.setResourceApiId(resourceApiId);
        authResourceApiService.updateAuthResourceApi(authResourceApi);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }
}

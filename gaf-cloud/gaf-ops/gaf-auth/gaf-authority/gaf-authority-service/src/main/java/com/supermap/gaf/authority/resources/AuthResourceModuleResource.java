/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.resources;

import com.supermap.gaf.authority.commontype.AuthResourceModule;
import com.supermap.gaf.authority.service.AuthResourceModuleService;
import com.supermap.gaf.authority.vo.AuthResourceModuleSelectVo;
import com.supermap.gaf.authority.vo.TreeNode;
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
 * 模块接口
 * @date:2021/3/25
 * @author  zhm
 */
@Component
@Api(value = "模块接口")
public class AuthResourceModuleResource{
    private final AuthResourceModuleService authResourceModuleService;

    public AuthResourceModuleResource(AuthResourceModuleService authResourceModuleService) {
        this.authResourceModuleService = authResourceModuleService;
    }

    @ApiOperation(value = "查询模块", notes = "根据id查询模块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceModuleId", value = "模块id", paramType = "path", dataType = "string", required = true)
    })
	@GET
    @Produces({MediaType.APPLICATION_JSON})
	@Path("/{resourceModuleId}")
    public MessageResult<AuthResourceModule> getById(@PathParam("resourceModuleId")String resourceModuleId){
		return MessageResult.data(authResourceModuleService.getById(resourceModuleId)).message("查询成功").build();
    }

    @ApiOperation(value = "获取模块树节点集合", notes = "注意:未组织为树结构. 若未查询到则返回空集合。返回数据中type为12的表示分组目录节点，type为7表示模块树节点")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/tree")
    public MessageResult<List<TreeNode>> getModuleNodes(){
        return MessageResult.data(authResourceModuleService.getModuleNodes()).message("查询成功").build();
    }

    @ApiOperation(value = "分页条件查询模块")
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
    public MessageResult<Map<String,Object>> pageList(@StringRange(entityClass = AuthResourceModule.class) @QueryParam("searchFieldName")String searchFieldName,
										@QueryParam("searchFieldValue")String searchFieldValue,
										@StringRange(entityClass = AuthResourceModule.class) @QueryParam("orderFieldName")String orderFieldName,
										@StringRange({"asc","desc"}) @QueryParam("orderMethod")String orderMethod,
                                        @DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
										@DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        if(pageNum==null || pageNum<1) {
            pageNum=1;
        }
        Integer offset = (pageNum-1)*pageSize;
        AuthResourceModuleSelectVo selectVo = AuthResourceModuleSelectVo.builder()
                                        .searchFieldName(searchFieldName)
										.searchFieldValue(searchFieldValue)
										.orderFieldName(orderFieldName)
										.orderMethod(orderMethod)
										.offset(offset)
                                        .pageSize(pageSize)
                                        .build(); 
        Map<String,Object> result;
		if(searchFieldName==null & orderFieldName==null) {
			result = authResourceModuleService.pageList(selectVo);
		} else {
			result = authResourceModuleService.searchList(selectVo);
		}
		return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "查询某模块分组下模块的数量", notes = "根据模块分组id条件查询模块的数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "moduleCatalogId", value = "模块分组id", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/module-catalog-id/{moduleCatalogId}")
    public MessageResult<Integer> countByModuleCatalogId(@NotEmpty @PathParam("moduleCatalogId") String moduleCatalogId) {
        return MessageResult.data(authResourceModuleService.countByModuleCatalogId(moduleCatalogId)).message("查询成功").build();
    }

    @ApiOperation(value = "新增模块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authResourceModule", value = "模块", dataTypeClass = AuthResourceModule.class, paramType = "body",required = true)
    })
	@POST
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> insertAuthResourceModule(AuthResourceModule authResourceModule){
        authResourceModuleService.insertAuthResourceModule(authResourceModule);
		return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @ApiOperation(value = "批量新增模块")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    public MessageResult<Void> batchInsert(List<AuthResourceModule> authResourceModules){
        authResourceModuleService.batchInsert(authResourceModules);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @ApiOperation(value = "删除模块", notes = "根据id删除模块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resourceModuleId", value = "模块id", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
	@Path("/{resourceModuleId}")
    public MessageResult<Void> deleteAuthResourceModule(@PathParam("resourceModuleId")String resourceModuleId){
        authResourceModuleService.deleteAuthResourceModule(resourceModuleId);
		return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

    @ApiOperation(value = "批量删除模块", notes = "根据id集合批量删除模块")
	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> batchDelete(List<String> resourceModuleIds){
        authResourceModuleService.batchDelete(resourceModuleIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

    @ApiOperation(value = "更新模块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authResourceModule", value = "模块", dataTypeClass = AuthResourceModule.class, paramType = "body",required = true),
            @ApiImplicitParam(name = "resourceModuleId", value = "模块id", paramType = "path", dataType = "string", required = true)
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
	@Path("/{resourceModuleId}")
    public MessageResult<Void> updateAuthResourceModule(AuthResourceModule authResourceModule,@PathParam("resourceModuleId")String resourceModuleId){
        authResourceModule.setResourceModuleId(resourceModuleId);
        authResourceModuleService.updateAuthResourceModule(authResourceModule);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }

	
	


}

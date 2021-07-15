/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.resource;

import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import com.supermap.gaf.webgis.cache.RegistryResultCacheI;
import com.supermap.gaf.webgis.domain.BatchRegistryServiceResult;
import com.supermap.gaf.webgis.entity.WebgisService;
import com.supermap.gaf.webgis.service.AsyncService;
import com.supermap.gaf.webgis.service.WebgisServiceService;
import com.supermap.gaf.webgis.service.impl.WebgisServiceServiceImpl;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisServiceConditonVo;
import com.supermap.gaf.webgis.vo.WebgisServiceSelectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * GIS服务接口
 * @author wangxiaolong 
 * @date 2020-12-05
 */
@Component
@Api(value = "GIS服务接口")
public class WebgisServiceResource{

    @Autowired
    private WebgisServiceService webgisServiceService;

    @Autowired
    public RegistryResultCacheI registryResultCacheI;

    @Autowired
    public AsyncService asyncService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "查询关联数据服务集及其字段配置", notes = "查询关联数据服务集及其字段配置")
    @Path("/{gisServiceId}/associationDataServices")
    @ApiImplicitParam(name = "gisServiceId",value = "GIS服务id",paramType = "path",dataType = "string",required = true )
    public MessageResult<List> selectAssociationDataServices(@PathParam("gisServiceId") String gisServiceId){
        return MessageResult.successe(List.class).data(webgisServiceService.selectAssociationDataServices(gisServiceId)).status(200).message("查询成功").build();
    }




    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "查询所有服务类型", notes = "查询所有服务类型")
    @Path("/types")
    public MessageResult<List> getServiceTypes(){
        List<TreeNode> allTypes = webgisServiceService.getServiceTypes();
        return MessageResult.successe(List.class).data(allTypes).status(200).message("查询所有服务类型成功").build();
    }


	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id查询GIS服务", notes = "根据id查询GIS服务")
	@Path("/{gisServiceId}")
    @ApiImplicitParam(name = "gisServiceId",value = "GIS服务id",paramType = "path",dataType = "string",required = true )
    public MessageResult<WebgisService> getById(@NotEmpty @PathParam("gisServiceId")String gisServiceId){
        WebgisService webgisService = webgisServiceService.getById(gisServiceId);
		return MessageResult.successe(WebgisService.class).data(webgisService).status(200).message("查询成功").build();
    }

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询GIS服务", notes = "分页条件查询GIS服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1",defaultValue = "1", allowableValues = "range[0,infinity]",paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10",allowableValues = "range[0,infinity]", paramType = "query", dataType = "integer")})
    public MessageResult<Page> pageList(@Valid @BeanParam WebgisServiceSelectVo webgisServiceSelectVo,
                                        @DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
                                        @DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<WebgisService> page = webgisServiceService.listByPageCondition(webgisServiceSelectVo, pageNum, pageSize);
		return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询未挂载在某资源目录下GIS服务", notes = "分页条件查询未挂载在某资源目录下GIS服务")
    @Path("/layerCatalogId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1",defaultValue = "1", allowableValues = "range[0,infinity]",paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10",allowableValues = "range[0,infinity]", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "layerCatalogId",value = "资源目录id",paramType = "query",dataType = "string",required = true )})
    public MessageResult<Page> pageList(@Valid @BeanParam WebgisServiceConditonVo webgisServiceConditonVo,
                                        @DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
                                        @DefaultValue("10")@QueryParam("pageSize")Integer pageSize,
                                        @QueryParam("layerCatalogId") String layerCatalogId){
        if(StringUtils.isEmpty(webgisServiceConditonVo.getOrderFieldName())){
            webgisServiceConditonVo.setOrderFieldName("updated_time");
            webgisServiceConditonVo.setOrderMethod("desc");
        }
        Page<WebgisService> page = webgisServiceService.listByPageCondition(webgisServiceConditonVo, pageNum, pageSize, layerCatalogId);
        return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }


	@POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "注册GIS服务", notes = "注册GIS服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "registryType",value = "注冊类型",allowableValues = "server,batch,single", paramType = "query",dataType = "string",required = false )})
    public MessageResult<String> insertWebgisService(WebgisService webgisService,@DefaultValue("single") @QueryParam("registryType") String registryType){
        String code = null;
        if(WebgisServiceServiceImpl.REGISTRY_TYPE_SERVER.equals(registryType) || WebgisServiceServiceImpl.REGISTRY_TYPE_BATCH.equals(registryType)){
            try {
                new URL(webgisService.getAddress());
                new RestTemplate().headForHeaders(webgisService.getAddress());
            } catch (MalformedURLException malformedURLException) {
                throw new GafException("url格式错误");
            }catch (Exception e){
                throw new GafException("无法解析服务列表");
            }
            ShiroUser shiroUser = SecurityUtilsExt.getUser();
            webgisService.setCreatedBy(shiroUser.getAuthUser().getName());
            webgisService.setUpdatedBy(shiroUser.getAuthUser().getName());
            final String requestCode = registryResultCacheI.generateKey();
            code = requestCode;
            CompletableFuture future =  asyncService.batchRegistryWebgis(webgisService,registryType,code);
            future.thenRun(()->registryResultCacheI.done(requestCode));
        }else{
            webgisServiceService.insertWebgisService(webgisService,registryType);
        }
        return MessageResult.data(code).status(200).message("操作成功").build();
    }



    @GET
    @Path("/result/{code}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "查询批量注册结果", notes = "查询批量注册结果")
    @ApiImplicitParam(name = "code",value = "code",paramType = "path",dataType = "string",required = true )
    public MessageResult<BatchRegistryServiceResult> getRegistryServiceResult(@PathParam("code") String code){
        return MessageResult.data(webgisServiceService.getRegistryServiceResult(code)).status(200).message("获取成功").build();
    }



    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增GIS服务", notes = "批量新增GIS服务")
    @ApiImplicitParam(name = "WebgisServices",value = "GIS服务数组",paramType = "body",allowMultiple = true,required = true )
    public MessageResult<Void> batchInsert(List<WebgisService> WebgisServices){
        webgisServiceService.batchInsert(WebgisServices);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }


    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除GIS服务", notes = "根据id删除GIS服务")
	@Path("/{gisServiceId}")
    @ApiImplicitParam(name = "gisServiceId",value = "GIS服务id",paramType = "path",dataType = "string",required = true )
    public MessageResult<Void> deleteWebgisService(@PathParam("gisServiceId")String gisServiceId){
        webgisServiceService.deleteWebgisService(gisServiceId);
		return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除GIS服务", notes = "批量删除GIS服务")
    @ApiImplicitParam(name = "gisServiceIds",value = "GIS服务数组",paramType = "body",allowMultiple = true,required = true )
    public MessageResult<Void> batchDelete(List<String> gisServiceIds){
        webgisServiceService.batchDelete(gisServiceIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

	
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新GIS服务", notes = "根据id更新GIS服务")
	@Path("/{gisServiceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "webgisService",value = "GIS服务",paramType = "body",dataTypeClass = WebgisService.class,required = true ),
            @ApiImplicitParam(name = "gisServiceId",value = "GIS服务id",paramType = "path",dataType = "string",required = true )})
    public MessageResult<Void> updateWebgisService(WebgisService webgisService,@PathParam("gisServiceId")String gisServiceId){
        webgisService.setGisServiceId(gisServiceId);
        webgisServiceService.updateWebgisService(webgisService);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }

    @POST
    @Path("/get-by-webgis-ids")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id集合批量查询webgis服务", notes = "根据id集合批量查询webgis服务")
    @ApiImplicitParam(name = "webgisIds",value = "webgis服务id集合",paramType = "body",dataType = "list",required = true )
    public MessageResult<List<WebgisService>> getByWebgisIds(List<String>webgisIds){
        List<WebgisService> webgisServices = webgisServiceService.listByIds(webgisIds);
        return MessageResult.data(webgisServices).status(200).message("获取成功").build();
    }
}

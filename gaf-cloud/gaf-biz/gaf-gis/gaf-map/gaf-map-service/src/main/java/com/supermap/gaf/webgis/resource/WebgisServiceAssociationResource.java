/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.webgis.domain.WebgisServiceDo;
import com.supermap.gaf.webgis.entity.WebgisServiceAssociation;
import com.supermap.gaf.webgis.service.WebgisServiceAssociationService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisServiceAssociationSelectVo;
import com.supermap.gaf.webgis.vo.WebgisServiceSelectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * GIS服务关联接口
 * @author wangxiaolong 
 * @date yyyy-mm-dd
 */
@Component
@Api(value = "GIS服务关联接口")
public class WebgisServiceAssociationResource{
    @Autowired
    private WebgisServiceAssociationService webgisServiceAssociationService;
	

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id查询GIS服务关联", notes = "根据id查询GIS服务关联")
	@Path("/{gisServiceAssocId}")
    @ApiImplicitParam(name = "gisServiceAssocId",value = "GIS服务关联id",paramType = "path",dataType = "string",required = true )
    public MessageResult<WebgisServiceAssociation> getById(@PathParam("gisServiceAssocId")String gisServiceAssocId){
        WebgisServiceAssociation webgisServiceAssociation = webgisServiceAssociationService.getById(gisServiceAssocId);
		return MessageResult.successe(WebgisServiceAssociation.class).data(webgisServiceAssociation).status(200).message("查询成功").build();
    }

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据webgis服务id分页查询关联的服务", notes = "根据webgis服务id分页查询关联的服务")
	@Path("/webgis-service/{webgisServiceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "webgisServiceId",value = "webgis服务id",paramType = "path",dataType = "string",required = true ),
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1",defaultValue = "1", allowableValues = "range[0,infinity]",paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10",allowableValues = "range[0,infinity]", paramType = "query", dataType = "integer")})
    public MessageResult<Page<WebgisServiceDo>> listAssociationServices(@NotEmpty @PathParam("webgisServiceId")String webgisServiceId,
                                                        @BeanParam WebgisServiceSelectVo webgisServiceSelectVo,
                                                        @DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
                                                        @DefaultValue("5")@QueryParam("pageSize")Integer pageSize){
        Page<WebgisServiceDo> result = webgisServiceAssociationService.listAssociationServices(webgisServiceId,webgisServiceSelectVo,pageNum,pageSize);
		return MessageResult.data(result).status(200).message("查询成功").build();
    }
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据webgis服务id分页查询不关联的服务", notes = "根据webgis服务id分页查询不关联的服务")
	@Path("/unrelated/{webgisServiceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "webgisServiceId",value = "webgis服务id",paramType = "path",dataType = "string",required = true ),
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1",defaultValue = "1", allowableValues = "range[0,infinity]",paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10",allowableValues = "range[0,infinity]", paramType = "query", dataType = "integer")})
    public MessageResult<Page<WebgisServiceDo>> listUnrelatedServices(@NotEmpty @PathParam("webgisServiceId")String webgisServiceId,
                                                        @BeanParam WebgisServiceSelectVo webgisServiceSelectVo,
                                                        @DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
                                                        @DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<WebgisServiceDo> result = webgisServiceAssociationService.listUnrelatedServices(webgisServiceId,webgisServiceSelectVo,pageNum,pageSize);
		return MessageResult.data(result).status(200).message("查询成功").build();
    }

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询GIS服务关联", notes = "分页条件查询GIS服务关联")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1",defaultValue = "1", allowableValues = "range[0,infinity]",paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10",allowableValues = "range[0,infinity]", paramType = "query", dataType = "integer")})
    public MessageResult<Page> pageList(@Valid @BeanParam WebgisServiceAssociationSelectVo webgisServiceAssociationSelectVo,
										@DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
										@DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<WebgisServiceAssociation> page = webgisServiceAssociationService.listByPageCondition(webgisServiceAssociationSelectVo, pageNum, pageSize);
		return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }


	@POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增GIS服务关联", notes = "新增GIS服务关联")
    @ApiImplicitParam(name = "webgisServiceAssociation",value = "GIS服务关联",paramType = "body",dataTypeClass = WebgisServiceAssociation.class,required = true )
    public MessageResult<Void> insertWebgisServiceAssociation(WebgisServiceAssociation webgisServiceAssociation){
        webgisServiceAssociationService.insertWebgisServiceAssociation(webgisServiceAssociation);
		return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增GIS服务关联", notes = "批量新增GIS服务关联")
    @ApiImplicitParam(name = "WebgisServiceAssociations",value = "GIS服务关联数组",paramType = "body",allowMultiple = true,required = true )
    public MessageResult<Void> batchInsert(List<WebgisServiceAssociation> WebgisServiceAssociations){
        webgisServiceAssociationService.batchInsert(WebgisServiceAssociations);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除GIS服务关联", notes = "根据id删除GIS服务关联")
	@Path("/{gisServiceAssocId}")
    @ApiImplicitParam(name = "gisServiceAssocId",value = "GIS服务关联id",paramType = "path",dataType = "string",required = true )
    public MessageResult<Void> deleteWebgisServiceAssociation(@PathParam("gisServiceAssocId")String gisServiceAssocId){
        webgisServiceAssociationService.deleteWebgisServiceAssociation(gisServiceAssocId);
		return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除GIS服务关联", notes = "批量删除GIS服务关联")
    @ApiImplicitParam(name = "gisServiceAssocIds",value = "GIS服务关联数组",paramType = "body",allowMultiple = true,required = true )
    public MessageResult<Void> batchDelete(List<String> gisServiceAssocIds){
        webgisServiceAssociationService.batchDelete(gisServiceAssocIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

	
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新GIS服务关联", notes = "根据id更新GIS服务关联")
	@Path("/{gisServiceAssocId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "webgisServiceAssociation",value = "GIS服务关联",paramType = "body",dataTypeClass = WebgisServiceAssociation.class,required = true ),
            @ApiImplicitParam(name = "gisServiceAssocId",value = "GIS服务关联id",paramType = "path",dataType = "string",required = true )})
    public MessageResult<Void> updateWebgisServiceAssociation(WebgisServiceAssociation webgisServiceAssociation,@PathParam("gisServiceAssocId")String gisServiceAssocId){
        webgisServiceAssociation.setGisServiceAssocId(gisServiceAssocId);
        webgisServiceAssociationService.updateWebgisServiceAssociation(webgisServiceAssociation);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }

	
	


}

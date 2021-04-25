/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.webgis.entity.WebgisRoamStop;
import com.supermap.gaf.webgis.service.WebgisRoamStopService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisRoamStopSelectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 漫游站点接口
 * @author wangxiaolong 
 * @date yyyy-mm-dd
 */
@Component
@Api(value = "漫游站点接口")
public class WebgisRoamStopResource{
    @Autowired
    private WebgisRoamStopService webgisRoamStopService;
	

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id查询漫游站点", notes = "根据id查询漫游站点")
	@Path("/{gisRoamStopId}")
    @ApiImplicitParam(name = "gisRoamStopId",value = "漫游站点id",paramType = "path",dataType = "string",required = true )
    public MessageResult<WebgisRoamStop> getById(@PathParam("gisRoamStopId")String gisRoamStopId){
        WebgisRoamStop webgisRoamStop = webgisRoamStopService.getById(gisRoamStopId);
		return MessageResult.successe(WebgisRoamStop.class).data(webgisRoamStop).status(200).message("查询成功").build();
    }

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询漫游站点", notes = "分页条件查询漫游站点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1",defaultValue = "1", allowableValues = "range[0,infinity]",paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10",allowableValues = "range[0,infinity]", paramType = "query", dataType = "integer")})
    public MessageResult<Page<WebgisRoamStop>> pageList(@BeanParam WebgisRoamStopSelectVo webgisRoamStopSelectVo,
										@DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
										@DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<WebgisRoamStop> page = webgisRoamStopService.listByPageCondition(webgisRoamStopSelectVo, pageNum, pageSize);
		return MessageResult.data(page).status(200).message("查询成功").build();
    }


	@POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增漫游站点", notes = "新增漫游站点")
    @ApiImplicitParam(name = "webgisRoamStop",value = "漫游站点",paramType = "body",dataTypeClass = WebgisRoamStop.class,required = true )
    public MessageResult<Void> insertWebgisRoamStop(WebgisRoamStop webgisRoamStop){
        webgisRoamStopService.insertWebgisRoamStop(webgisRoamStop);
		return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增漫游站点", notes = "批量新增漫游站点")
    @ApiImplicitParam(name = "WebgisRoamStops",value = "漫游站点数组",paramType = "body",allowMultiple = true,required = true )
    public MessageResult<Void> batchInsert(List<WebgisRoamStop> WebgisRoamStops){
        webgisRoamStopService.batchInsert(WebgisRoamStops);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }



    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除漫游站点", notes = "根据id删除漫游站点")
	@Path("/{gisRoamStopId}")
    @ApiImplicitParam(name = "gisRoamStopId",value = "漫游站点id",paramType = "path",dataType = "string",required = true )
    public MessageResult<Void> deleteWebgisRoamStop(@PathParam("gisRoamStopId")String gisRoamStopId){
        webgisRoamStopService.deleteWebgisRoamStop(gisRoamStopId);
		return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除漫游站点", notes = "批量删除漫游站点")
    @ApiImplicitParam(name = "gisRoamStopIds",value = "漫游站点id数组",paramType = "body",allowMultiple = true,required = true )
    public MessageResult<Void> batchDelete(List<String> gisRoamStopIds){
        webgisRoamStopService.batchDelete(gisRoamStopIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }



	
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新漫游站点", notes = "根据id更新漫游站点")
	@Path("/{gisRoamStopId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "webgisRoamStop",value = "漫游站点",paramType = "body",dataTypeClass = WebgisRoamStop.class,required = true ),
            @ApiImplicitParam(name = "gisRoamStopId",value = "漫游站点id",paramType = "path",dataType = "string",required = true )})
    public MessageResult<Void> updateWebgisRoamStop(WebgisRoamStop webgisRoamStop,@PathParam("gisRoamStopId")String gisRoamStopId){
        webgisRoamStop.setGisRoamStopId(gisRoamStopId);
        webgisRoamStopService.updateWebgisRoamStop(webgisRoamStop);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }

	
	


}

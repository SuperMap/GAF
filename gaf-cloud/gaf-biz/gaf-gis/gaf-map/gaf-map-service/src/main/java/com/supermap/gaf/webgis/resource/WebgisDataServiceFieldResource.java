/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.webgis.entity.WebgisDataServiceField;
import com.supermap.gaf.webgis.service.WebgisDataServiceFieldService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.DataServiceFieldsVo;
import com.supermap.gaf.webgis.vo.WebgisDataServiceFieldSelectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * GIS数据服务字段接口
 * @author wangxiaolong 
 * @date yyyy-mm-dd
 */
@Component
@Api(value = "GIS数据服务字段接口")
public class WebgisDataServiceFieldResource{
    @Autowired
    private WebgisDataServiceFieldService webgisDataServiceFieldService;

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id查询GIS数据服务字段", notes = "根据id查询GIS数据服务字段")
	@Path("/{gisServiceFieldId}")
    @ApiImplicitParam(name = "gisServiceFieldId",value = "GIS数据服务字段id",paramType = "path",dataType = "string",required = true )
    public MessageResult<WebgisDataServiceField> getById(@PathParam("gisServiceFieldId")String gisServiceFieldId){
        WebgisDataServiceField webgisDataServiceField = webgisDataServiceFieldService.getById(gisServiceFieldId);
		return MessageResult.successe(WebgisDataServiceField.class).data(webgisDataServiceField).status(200).message("查询成功").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据Webgis服务(只能是数据服务)id查询服务的数据集的所有字段列表及已选择的字段名", notes = "根据Webgis服务(只能是数据服务)id查询服务的数据集的所有字段列表及已选择的字段名")
    @Path("/data-service/{webgisServiceId}")
    @ApiImplicitParam(name = "webgisServiceId",value = "Webgis服务id",paramType = "path",dataType = "string",required = true )
    public MessageResult<DataServiceFieldsVo> listDataSetFields(@NotEmpty @PathParam("webgisServiceId") String webgisServiceId){
        DataServiceFieldsVo results = webgisDataServiceFieldService.listFieldsAndSelectFieldNames(webgisServiceId);
        return MessageResult.successe(DataServiceFieldsVo.class).data(results).status(200).message("查询成功").build();
    }

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询GIS数据服务字段", notes = "分页条件查询GIS数据服务字段")
    @ApiImplicitParams({
           @ApiImplicitParam(name = "pageNum", value = "页码", example = "1",defaultValue = "1", allowableValues = "range[0,infinity]",paramType = "query", dataType = "integer"),
           @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10",allowableValues = "range[0,infinity]", paramType = "query", dataType = "integer")})
    public MessageResult<Page<WebgisDataServiceField>> pageList(@BeanParam WebgisDataServiceFieldSelectVo webgisDataServiceFieldSelectVo,
										@DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
										@DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<WebgisDataServiceField> page = webgisDataServiceFieldService.listByPageCondition(webgisDataServiceFieldSelectVo, pageNum, pageSize);
		return MessageResult.data(page).status(200).message("查询成功").build();
    }


	@POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增GIS数据服务字段", notes = "新增GIS数据服务字段")
    @ApiImplicitParam(name = "webgisDataServiceField",value = "数据服务字段",paramType = "body",dataTypeClass = WebgisDataServiceField.class,required = true )
    public MessageResult<Void> insertWebgisDataServiceField(WebgisDataServiceField webgisDataServiceField){
        webgisDataServiceFieldService.insertWebgisDataServiceField(webgisDataServiceField);
		return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增GIS数据服务字段", notes = "批量新增GIS数据服务字段")
    @ApiImplicitParam(name = "WebgisDataServiceFields",value = "GIS数据服务字段列表",paramType = "body",allowMultiple = true,required = true )
    public MessageResult<Void> batchInsert(List<WebgisDataServiceField> WebgisDataServiceFields){
        webgisDataServiceFieldService.batchInsert(WebgisDataServiceFields);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除GIS数据服务字段", notes = "根据id删除GIS数据服务字段")
	@Path("/{gisServiceFieldId}")
    @ApiImplicitParam(name = "gisServiceFieldId",value = "GIS数据服务字段id",paramType = "path",dataType = "string",required = true )
    public MessageResult<Void> deleteWebgisDataServiceField(@PathParam("gisServiceFieldId")String gisServiceFieldId){
        webgisDataServiceFieldService.deleteWebgisDataServiceField(gisServiceFieldId);
		return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除GIS数据服务字段", notes = "批量删除GIS数据服务字段")
    @ApiImplicitParam(name = "gisServiceFieldIds",value = "GIS数据服务字段id数组",paramType = "body",allowMultiple = true,required = true )
    public MessageResult<Void> batchDelete(List<String> gisServiceFieldIds){
        webgisDataServiceFieldService.batchDelete(gisServiceFieldIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

	
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新GIS数据服务字段", notes = "根据id更新GIS数据服务字段")
	@Path("/{gisServiceFieldId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "webgisDataServiceField",value = "GIS数据服务字段",paramType = "body",dataTypeClass = WebgisDataServiceField.class,required = true ),
            @ApiImplicitParam(name = "gisServiceFieldId",value = "GIS数据服务字段id",paramType = "path",dataType = "string",required = true )})
    public MessageResult<Void> updateWebgisDataServiceField(WebgisDataServiceField webgisDataServiceField,@PathParam("gisServiceFieldId")String gisServiceFieldId){
        webgisDataServiceField.setGisServiceFieldId(gisServiceFieldId);
        webgisDataServiceFieldService.updateWebgisDataServiceField(webgisDataServiceField);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }


    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "配置webgis数据服务的字段", notes = "配置webgis数据服务的字段")
	@Path("/configuration/{webgisServiceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fields",value = "webgis数据服务的字段数组",paramType = "body",allowMultiple = true,required = true ),
            @ApiImplicitParam(name = "webgisServiceId",value = "webgis数据服务id",paramType = "path",dataType = "string",required = true )})
    public MessageResult<Void> configFields(List<WebgisDataServiceField> fields,@PathParam("webgisServiceId") String webgisServiceId){
        webgisDataServiceFieldService.configFields(fields, webgisServiceId);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }

	
	


}

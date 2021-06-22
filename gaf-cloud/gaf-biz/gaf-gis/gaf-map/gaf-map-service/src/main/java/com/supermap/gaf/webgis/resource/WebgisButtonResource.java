/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.webgis.entity.WebgisButton;
import com.supermap.gaf.webgis.service.WebgisButtonService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisButtonSelectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 地图按钮接口
 * @author zhurongcheng 
 * @date 2020-12-05
 */
@Component
@Api(value = "地图按钮接口")
public class WebgisButtonResource{
    @Autowired
    private WebgisButtonService webgisButtonService;
	

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id查询地图按钮", notes = "根据id查询地图按钮")
	@Path("/{buttonId}")
    @ApiImplicitParam(name = "buttonId",value = "地图按钮id",paramType = "path",dataType = "string",required = true )
    public MessageResult<WebgisButton> getById(@PathParam("buttonId")String buttonId){
        WebgisButton webgisButton = webgisButtonService.getById(buttonId);
		return MessageResult.successe(WebgisButton.class).data(webgisButton).status(200).message("查询成功").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "检查字段是否可用", notes = "检查字段是否可用")
    @Path("/checkAvailable")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "button",value = "地图按钮",paramType = "body",dataTypeClass = WebgisButton.class,required = true ),
            @ApiImplicitParam(name = "isUpdate",value = "是否为更新操作检查",paramType = "query",dataType = "boolean",required = true )})
    public MessageResult<Void> checkAvailable(@BeanParam WebgisButton button,@DefaultValue("false")@QueryParam("isUpdate") boolean isUpdate){
        webgisButtonService.checkAvailable(button,isUpdate);
        return MessageResult.successe(Void.class).status(200).message("可使用").build();
    }

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询地图按钮", notes = "分页条件查询地图按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1",defaultValue = "1", allowableValues = "range[0,infinity]",paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10",allowableValues = "range[0,infinity]", paramType = "query", dataType = "integer")})
    public MessageResult<Page> pageList(@Valid @BeanParam WebgisButtonSelectVo webgisButtonSelectVo,
										@DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
                                        @DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        if(StringUtils.isEmpty(webgisButtonSelectVo.getOrderFieldName())){
            webgisButtonSelectVo.setOrderFieldName("updated_time");
            webgisButtonSelectVo.setOrderMethod("desc");
        }
	    Page<WebgisButton> page = webgisButtonService.listByPageCondition(webgisButtonSelectVo, pageNum, pageSize);
		return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }


	@POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增地图按钮", notes = "新增地图按钮")
    @ApiImplicitParam(name = "webgisButton",value = "地图按钮",paramType = "body",dataTypeClass = WebgisButton.class,required = true )
    public MessageResult<Void> insertWebgisButton(WebgisButton webgisButton){
        webgisButtonService.insertWebgisButton(webgisButton);
		return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增地图按钮", notes = "批量新增地图按钮")
    @ApiImplicitParam(name = "WebgisButtons",value = "地图按钮数组",paramType = "body",allowMultiple = true,required = true )
    public MessageResult<Void> batchInsert(List<WebgisButton> WebgisButtons){
        webgisButtonService.batchInsert(WebgisButtons);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除地图按钮", notes = "根据id删除地图按钮")
	@Path("/{buttonId}")
    @ApiImplicitParam(name = "buttonId",value = "地图按钮id",paramType = "path",dataType = "string",required = true )
    public MessageResult<Void> deleteWebgisButton(@PathParam("buttonId")String buttonId){
        webgisButtonService.deleteWebgisButton(buttonId);
		return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除地图按钮", notes = "批量删除地图按钮")
    @ApiImplicitParam(name = "buttonIds",value = "地图按钮id数组",paramType = "body",allowMultiple = true,required = true )
    public MessageResult<Void> batchDelete(List<String> buttonIds){
        webgisButtonService.batchDelete(buttonIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

	
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新地图按钮", notes = "根据id更新地图按钮")
	@Path("/{buttonId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "webgisButton",value = "地图按钮",paramType = "body",dataTypeClass = WebgisButton.class,required = true ),
            @ApiImplicitParam(name = "buttonId",value = "地图按钮id",paramType = "path",dataType = "string",required = true )})
    public MessageResult<Void> updateWebgisButton(WebgisButton webgisButton,@PathParam("buttonId")String buttonId){
        webgisButton.setButtonId(buttonId);
        webgisButtonService.updateWebgisButton(webgisButton);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }

	
	


}

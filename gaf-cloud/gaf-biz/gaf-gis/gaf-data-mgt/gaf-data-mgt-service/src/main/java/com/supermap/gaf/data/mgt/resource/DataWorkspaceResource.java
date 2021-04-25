/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.DataWorkspace;
import com.supermap.gaf.data.mgt.service.DataWorkspaceService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.DataWorkspaceSelectVo;
import com.supermap.gaf.data.mgt.vo.WorkspaceIdServiceType;
import com.supermap.gaf.exception.GafException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.sasl.AuthenticationException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * 工作空间接口
 * @author zrc 
 * @date yyyy-mm-dd
 */
@Component
@Api(value = "工作空间接口")
public class DataWorkspaceResource{
    @Autowired
    private DataWorkspaceService dataWorkspaceService;

    @ApiOperation(value = "服务发布", notes = "根据工作空间id和服务类型集合做服务发布", response = MessageResult.class)
    @ApiImplicitParam(name = "workspaceIdServiceTypes",value = "工作空间id服务类型集合对象",paramType = "body")

    @Path("/publish-service")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<List<MessageResult<Object>>> publishService(List<WorkspaceIdServiceType> workspaceIdServiceTypes) throws IOException {
        if (workspaceIdServiceTypes == null || workspaceIdServiceTypes.isEmpty()){
            throw new GafException("工作空间id服务类型集合不能为空");
        }
        List<MessageResult<Object>> resultList = dataWorkspaceService.publishService(workspaceIdServiceTypes);
        return MessageResult.data(resultList).message("服务发布完成").build();
    }


    @ApiOperation(value = "根据工作空间id查询工作空间", notes = "根据工作空间id查询工作空间")
    @ApiImplicitParam(name = "workspaceId",value = "工作空间id",paramType = "path",dataType = "String",example = "xxxxxx")

    @GET
    @Produces({MediaType.APPLICATION_JSON})
	@Path("/{workspaceId}")
    public MessageResult<DataWorkspace> getById(@PathParam("workspaceId")String workspaceId){
        DataWorkspace dataWorkspace = dataWorkspaceService.getById(workspaceId);
		return MessageResult.successe(DataWorkspace.class).data(dataWorkspace).status(200).message("查询成功").build();
    }
    @ApiOperation(value = "按条件分页查询工作空间", notes = "按条件分页查询工作空间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataWorkspaceSelectVo",value = "工作控件实体类",paramType = "body",dataTypeClass = DataWorkspaceSelectVo.class),
            @ApiImplicitParam(name = "pageNum",value = "当前页数",paramType = "query",dataType = "Integer",example = "1"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示多少条",paramType = "query",dataType = "Integer",example = "10")
    })

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Page> pageList(@BeanParam DataWorkspaceSelectVo dataWorkspaceSelectVo,
										@DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
										@DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        if(StringUtils.isEmpty(dataWorkspaceSelectVo.getOrderFieldName())){
            dataWorkspaceSelectVo.setOrderFieldName("updated_time");
            dataWorkspaceSelectVo.setOrderMethod("desc");
        }
        Page<DataWorkspace> page = dataWorkspaceService.listByPageCondition(dataWorkspaceSelectVo, pageNum, pageSize);
		return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }

    @ApiOperation(value = "新增工作空间", notes = "新增工作空间")
    @ApiImplicitParam(name = "dataWorkspace",value = "工作空间实体类",paramType = "body",dataTypeClass = DataWorkspace.class)
	@POST
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> insertDataWorkspace(@Valid DataWorkspace dataWorkspace){
        dataWorkspaceService.insertDataWorkspace(dataWorkspace);
		return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }


    @ApiOperation(value = "创建工作空间", notes = "创建工作空间")
    @POST
    @Path("/minio/{path:.*}")
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> create(@PathParam("path") String path) throws AuthenticationException {
        dataWorkspaceService.createWorkspace(path);
        return MessageResult.successe(Void.class).status(200).message("创建成功").build();
    }

    @ApiOperation(value = "批量新增工作空间", notes = "批量新增工作空间")
    @ApiImplicitParam(name = "dataWorkspace",value = "工作空间实体类列表",paramType = "body",allowMultiple = true,dataTypeClass = DataWorkspace.class,example = "[{},{}]")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    public MessageResult<Void> batchInsert(List<DataWorkspace> dataWorkspaces){
        dataWorkspaceService.batchInsert(dataWorkspaces);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }
    @ApiOperation(value = "批量新增工作空间", notes = "批量新增工作空间")
    @ApiImplicitParam(name = "dataWorkspace",value = "工作空间实体类列表",paramType = "body",allowMultiple = true,dataTypeClass = DataWorkspace.class,example = "[{},{}]")

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
	@Path("/{workspaceId}")
    public MessageResult<Void> deleteDataWorkspace(@PathParam("workspaceId")String workspaceId) throws AuthenticationException {
        dataWorkspaceService.deleteDataWorkspace(workspaceId);
		return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

    @ApiOperation(value = "批量删除工作空间", notes = "批量删除工作空间")
    @ApiImplicitParam(name = "workspaceIds",value = "工作空间id数组",paramType = "query",dataType = "String",allowMultiple = true,example = "[xxx,xxx]")
	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Void> batchDelete(List<String> workspaceIds) throws AuthenticationException {
        dataWorkspaceService.batchDelete(workspaceIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

    @ApiOperation(value = "根据工作空间id更新工作空间", notes = "根据工作空间id更新工作空间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataWorkspace",value = "工作空间实体类",paramType = "body",dataTypeClass = DataWorkspace.class),
            @ApiImplicitParam(name = "workspaceId",value = "工作空间id",paramType = "path",dataType = "String",example = "xxxx")
    })
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
	@Path("/{workspaceId}")
    public MessageResult<Void> updateDataWorkspace(DataWorkspace dataWorkspace,@PathParam("workspaceId")String workspaceId){
        dataWorkspace.setWorkspaceId(workspaceId);
        dataWorkspaceService.updateDataWorkspace(dataWorkspace);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }

//
//    public static void main(String[] args) {
//        java.nio.file.Path pathAbsolute = Paths.get("E:\\test.smwu");
//        java.nio.file.Path pathBase = Paths.get("E:/Work/datasource/testx.udbx");
//        java.nio.file.Path pathRelative = pathAbsolute.relativize(pathBase);
//        System.out.println(pathRelative);
//    }


}

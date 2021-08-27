/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.MmPhysics;
import com.supermap.gaf.data.mgt.entity.vo.MmPhysicsVO;
import com.supermap.gaf.data.mgt.model.PhysicsResult;
import com.supermap.gaf.data.mgt.service.MmPhysicsService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmPhysicsSelectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 物理表接口
 * @author wxl 
 * @date yyyy-mm-dd
 * data-mgt/model-manage/physics
 */
@Component
@Api(value = "物理表接口")
public class MmPhysicsResource{
    @Autowired
    private MmPhysicsService mmPhysicsService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询物理表", notes = "分页条件查询物理表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1",defaultValue = "1", allowableValues = "range[1,infinity]",paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10",allowableValues = "range(0,infinity]", paramType = "query", dataType = "integer")
    })
    public MessageResult<Page> pageList(@Valid @BeanParam MmPhysicsSelectVo mmPhysicsSelectVo,
                                        @DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
                                        @DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<MmPhysics> page = mmPhysicsService.listByPageCondition(mmPhysicsSelectVo, pageNum, pageSize);
        return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }


	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id查询物理表", notes = "根据id查询物理表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "physicsId", value = "物理表id", paramType = "path", dataType = "string", required = true)
    })
	@Path("/{physicsId}")
    public MessageResult<MmPhysics> getById(@PathParam("physicsId")String physicsId){
        MmPhysics mmPhysics = mmPhysicsService.getById(physicsId);
		return MessageResult.successe(MmPhysics.class).data(mmPhysics).status(200).message("查询成功").build();
    }

	@GET
    @Path("/detail")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询物理表详情", notes = "分页条件查询物理表详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", example = "1",defaultValue = "1", allowableValues = "range[1,infinity]",paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", example = "10", defaultValue = "10",allowableValues = "range(0,infinity]", paramType = "query", dataType = "integer")
    })
    public MessageResult<Page> pageListWithDetail(@Valid @BeanParam MmPhysicsSelectVo mmPhysicsSelectVo,
										@DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
										@DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<MmPhysicsVO> page = mmPhysicsService.listWithDetail(mmPhysicsSelectVo, pageNum, pageSize);
		return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量物理化", notes = "批量物理化")
    @Path("/physicalization-batch")
    public MessageResult<PhysicsResult> physicalization(List<MmPhysics> mmPhysicsList){
        PhysicsResult physicsResult = mmPhysicsService.physicalization(mmPhysicsList);
        return MessageResult.data(physicsResult).message("批量物理化").build();
    }

	@POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增物理表", notes = "新增物理表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mmPhysics", value = "物理表", dataTypeClass = MmPhysics.class, paramType = "body",required = true)
    })
    public MessageResult<Void> insertMmPhysics(MmPhysics mmPhysics){
        mmPhysicsService.insertMmPhysics(mmPhysics);
		return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增物理表", notes = "批量新增物理表")
    public MessageResult<Void> batchInsert(List<MmPhysics> mmPhysicss){
        mmPhysicsService.batchInsert(mmPhysicss);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除物理表", notes = "根据id删除物理表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "physicsId", value = "物理表id", paramType = "path", dataType = "string", required = true)
    })
	@Path("/{physicsId}")
    public MessageResult<Void> deleteMmPhysics(@PathParam("physicsId")String physicsId){
        mmPhysicsService.deleteMmPhysics(physicsId);
		return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除物理表", notes = "批量删除物理表")
    public MessageResult<Void> batchDelete(List<String> physicsIds){
        mmPhysicsService.batchDelete(physicsIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

	
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新物理表", notes = "根据id更新物理表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mmPhysics", value = "物理表", dataTypeClass = MmPhysics.class, paramType = "body",required = true),
            @ApiImplicitParam(name = "physicsId", value = "物理表id", paramType = "path", dataType = "string", required = true)
    })
	@Path("/{physicsId}")
    public MessageResult<Void> updateMmPhysics(MmPhysics mmPhysics,@PathParam("physicsId")String physicsId){
        mmPhysics.setPhysicsId(physicsId);
        mmPhysicsService.updateMmPhysics(mmPhysics);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }



	
	


}

package com.supermap.gaf.data.mgt.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.MmTable;
import com.supermap.gaf.data.mgt.service.MmTableService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmTableSelectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 逻辑表接口
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Component
@Api(value = "逻辑表接口")
public class MmTableResource{
    @Autowired
    private MmTableService mmTableService;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id查询逻辑表", notes = "根据id查询逻辑表")
	@Path("/{tableId}")
    public MessageResult<MmTable> getById(@PathParam("tableId")String tableId){
        MmTable mmTable = mmTableService.getById(tableId);
		return MessageResult.successe(MmTable.class).data(mmTable).status(200).message("查询成功").build();
    }

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询逻辑表", notes = "分页条件查询逻辑表")
    public MessageResult<Page> pageList(@BeanParam MmTableSelectVo mmTableSelectVo,
										@DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
										@DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<MmTable> page = mmTableService.listByPageCondition(mmTableSelectVo, pageNum, pageSize);
		return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }
    //
    //@POST
    //@Produces({MediaType.APPLICATION_JSON})
    //@ApiOperation(value = "批量物理化", notes = "批量物理化")
    //@Path("/physicalization")
    //public MessageResult<Void> physicalization(List<String> logicTableIds, MmPhysics mmPhysics ){
    //    //Page<MmTable> page = mmTableService.listByPageCondition(mmTableSelectVo, pageNum, pageSize);
    //    return MessageResult.successe(Void.class).data(null).status(200).message("查询成功").build();
    //}


	@POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增逻辑表", notes = "新增逻辑表")
    public MessageResult<MmTable> insertMmTable(MmTable mmTable){
		return MessageResult.data(mmTableService.insertMmTable(mmTable)).status(200).message("新增操作成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增逻辑表", notes = "批量新增逻辑表")
    public MessageResult<Void> batchInsert(List<MmTable> MmTables){
        mmTableService.batchInsert(MmTables);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除逻辑表", notes = "根据id删除逻辑表")
	@Path("/{tableId}")
    public MessageResult<MmTable> deleteMmTable(@PathParam("tableId")String tableId){
		return MessageResult.data(mmTableService.deleteMmTable(tableId)).message("删除操作成功").build();
    }

	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除逻辑表", notes = "批量删除逻辑表")
    public MessageResult<Void> batchDelete(List<String> tableIds){
        mmTableService.batchDelete(tableIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

	
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新逻辑表", notes = "根据id更新逻辑表")
	@Path("/{tableId}")
    public MessageResult<MmTable> updateMmTable(MmTable mmTable,@PathParam("tableId")String tableId){
        mmTable.setTableId(tableId);
		return MessageResult.data(mmTableService.updateMmTable(mmTable)).message("更新操作成功").build();
    }

	
	


}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.DataDataset;
import com.supermap.gaf.data.mgt.service.DataDatasetService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.DataDatasetSelectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 数据集接口
 * @author yw 
 * @date yyyy-mm-dd
 */
@Component
@Api(value = "数据集接口")
public class DataDatasetResource {
    @Autowired
    private DataDatasetService dataDatasetService;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id查询数据集", notes = "根据id查询数据集")
	@Path("/{datasetId}")
    public MessageResult<DataDataset> getById(@PathParam("datasetId")String datasetId){
        DataDataset dataDataset = dataDatasetService.getById(datasetId);
		return MessageResult.successe(DataDataset.class).data(dataDataset).status(200).message("查询成功").build();
    }

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询数据集", notes = "分页条件查询数据集")
    public MessageResult<Page> pageList(@BeanParam DataDatasetSelectVo dataDatasetSelectVo,
                                        @DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
                                        @DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<DataDataset> page = dataDatasetService.listByPageCondition(dataDatasetSelectVo, pageNum, pageSize);
		return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }


	@POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增数据集", notes = "新增数据集")
    public MessageResult<Void> insertDataDataset(DataDataset dataDataset){
        dataDatasetService.insertDataDataset(dataDataset);
		return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增数据集", notes = "批量新增数据集")
    public MessageResult<Void> batchInsert(List<DataDataset> DataDatasets){
        dataDatasetService.batchInsert(DataDatasets);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除数据集", notes = "根据id删除数据集")
	@Path("/{datasetId}")
    public MessageResult<Void> deleteDataDataset(@PathParam("datasetId")String datasetId){
        dataDatasetService.deleteDataDataset(datasetId);
		return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除数据集", notes = "批量删除数据集")
    public MessageResult<Void> batchDelete(List<String> datasetIds){
        dataDatasetService.batchDelete(datasetIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

	
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新数据集", notes = "根据id更新数据集")
	@Path("/{datasetId}")
    public MessageResult<Void> updateDataDataset(DataDataset dataDataset, @PathParam("datasetId")String datasetId){
        dataDataset.setDatasetId(datasetId);
        dataDatasetService.updateDataDataset(dataDataset);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }

	
	


}

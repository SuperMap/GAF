package com.supermap.gaf.data.mgt.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.commontypes.tree.DefaultTreeNode;
import com.supermap.gaf.data.mgt.entity.MmModel;
import com.supermap.gaf.data.mgt.service.MmModelService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmModelSelectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 模型接口
 * @author wxl 
 * @since yyyy-mm-dd
 */
@Component
@Api(value = "模型接口")
public class MmModelResource{

    @Autowired
    private MmModelService mmModelService;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id查询模型", notes = "根据id查询模型")
	@Path("/{modelId}")
    public MessageResult<MmModel> getById(@PathParam("modelId")String modelId){
        MmModel mmModel = mmModelService.getById(modelId);
		return MessageResult.successe(MmModel.class).data(mmModel).status(200).message("查询成功").build();
    }

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询模型", notes = "分页条件查询模型")
    public MessageResult<Page> pageList(@BeanParam MmModelSelectVo mmModelSelectVo,
										@DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
										@DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<MmModel> page = mmModelService.listByPageCondition(mmModelSelectVo, pageNum, pageSize);
		return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "查询所有模型及其所有逻辑表的构成的树", notes = "查询所有模型及其所有逻辑表的构成的树")
    @Path("/model-tables-tree")
    public MessageResult<List<DefaultTreeNode>> modelTablesTree(){
        List<DefaultTreeNode> tree = mmModelService.modelTablesTree();
        MessageResult<List<DefaultTreeNode>> mr = new MessageResult<>();
        mr.setSuccessed(true);
        mr.setData(tree);
        mr.setMessage("查询成功");
        return mr;
    }


	@POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增模型", notes = "新增模型")
    public MessageResult<MmModel> insertMmModel(MmModel mmModel){
		return MessageResult.data(mmModelService.insertMmModel(mmModel)).status(200).message("新增操作成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增模型", notes = "批量新增模型")
    public MessageResult<Void> batchInsert(List<MmModel> MmModels){
        mmModelService.batchInsert(MmModels);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除模型", notes = "根据id删除模型")
	@Path("/{modelId}")
    public MessageResult<MmModel> deleteMmModel(@PathParam("modelId")String modelId){
		return MessageResult.data(mmModelService.deleteMmModel(modelId)).message("删除操作成功").build();
    }

	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除模型", notes = "批量删除模型")
    public MessageResult<Void> batchDelete(List<String> modelIds){
        mmModelService.batchDelete(modelIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

	
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新模型", notes = "根据id更新模型")
	@Path("/{modelId}")
    public MessageResult<MmModel> updateMmModel(MmModel mmModel,@PathParam("modelId")String modelId){
        mmModel.setModelId(modelId);
        MmModel model = mmModelService.updateMmModel(mmModel);
        return MessageResult.data(model).status(200).message("更新操作成功").build();
    }

	
	


}

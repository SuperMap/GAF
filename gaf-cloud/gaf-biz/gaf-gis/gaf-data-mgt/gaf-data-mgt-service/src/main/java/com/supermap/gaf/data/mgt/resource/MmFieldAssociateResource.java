package com.supermap.gaf.data.mgt.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.MmFieldAssociate;
import com.supermap.gaf.data.mgt.service.MmFieldAssociateService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmFieldAssociateSelectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 字段关联接口
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Component
@Api(value = "字段关联接口")
public class MmFieldAssociateResource{
    @Autowired
    private MmFieldAssociateService mmFieldAssociateService;
	
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id查询字段关联", notes = "根据id查询字段关联")
	@Path("/{fieldAssociateId}")
    public MessageResult<MmFieldAssociate> getById(@PathParam("fieldAssociateId")String fieldAssociateId){
        MmFieldAssociate mmFieldAssociate = mmFieldAssociateService.getById(fieldAssociateId);
		return MessageResult.successe(MmFieldAssociate.class).data(mmFieldAssociate).status(200).message("查询成功").build();
    }

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询字段关联", notes = "分页条件查询字段关联")
    public MessageResult<Page> pageList(@BeanParam MmFieldAssociateSelectVo mmFieldAssociateSelectVo,
										@DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
										@DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<MmFieldAssociate> page = mmFieldAssociateService.listByPageCondition(mmFieldAssociateSelectVo, pageNum, pageSize);
		return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }


	@POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增字段关联", notes = "新增字段关联")
    public MessageResult<Void> insertMmFieldAssociate(MmFieldAssociate mmFieldAssociate){
        mmFieldAssociateService.insertMmFieldAssociate(mmFieldAssociate);
		return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增字段关联", notes = "批量新增字段关联")
    public MessageResult<Void> batchInsert(List<MmFieldAssociate> MmFieldAssociates){
        mmFieldAssociateService.batchInsert(MmFieldAssociates);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除字段关联", notes = "根据id删除字段关联")
	@Path("/{fieldAssociateId}")
    public MessageResult<Void> deleteMmFieldAssociate(@PathParam("fieldAssociateId")String fieldAssociateId){
        mmFieldAssociateService.deleteMmFieldAssociate(fieldAssociateId);
		return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除字段关联", notes = "批量删除字段关联")
    public MessageResult<Void> batchDelete(List<String> fieldAssociateIds){
        mmFieldAssociateService.batchDelete(fieldAssociateIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

	
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新字段关联", notes = "根据id更新字段关联")
	@Path("/{fieldAssociateId}")
    public MessageResult<Void> updateMmFieldAssociate(MmFieldAssociate mmFieldAssociate,@PathParam("fieldAssociateId")String fieldAssociateId){
        mmFieldAssociate.setFieldAssociateId(fieldAssociateId);
        mmFieldAssociateService.updateMmFieldAssociate(mmFieldAssociate);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }

	
	


}

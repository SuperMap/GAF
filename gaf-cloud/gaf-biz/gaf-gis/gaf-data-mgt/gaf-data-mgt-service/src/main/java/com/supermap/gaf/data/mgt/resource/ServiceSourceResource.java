package com.supermap.gaf.data.mgt.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.ServiceSource;
import com.supermap.gaf.data.mgt.service.ServiceSourceService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.ServiceSourceSelectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


/**
 * 服务来源关联表接口
 * @author zrc 
 * @date yyyy-mm-dd
 */
@Component
@Api(value = "服务来源关联表接口")
public class ServiceSourceResource{
    @Autowired
    private ServiceSourceService serviceSourceService;
	

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "通过id查询服务来源关联表", notes = "通过id查询服务来源关联表")
	@Path("/{serviceSourceId}")
    public MessageResult<ServiceSource> getById(@PathParam("serviceSourceId")String serviceSourceId){
        ServiceSource serviceSource = serviceSourceService.getById(serviceSourceId);
		return MessageResult.successe(ServiceSource.class).data(serviceSource).status(200).message("查询成功").build();
    }
	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询服务来源关联表", notes = "分页条件查询服务来源关联表")
    public MessageResult<Page> pageList(@BeanParam ServiceSourceSelectVo serviceSourceSelectVo,
										@DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
										@DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<ServiceSource> page = serviceSourceService.listByPageCondition(serviceSourceSelectVo, pageNum, pageSize);
		return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }


	@POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增服务来源关联表", notes = "新增服务来源关联表")
    public MessageResult<Void> insertServiceSource(ServiceSource serviceSource){
        serviceSourceService.insertServiceSource(serviceSource);
		return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增服务来源关联表", notes = "批量新增服务来源关联表")
    public MessageResult<Void> batchInsert(List<ServiceSource> ServiceSources){
        serviceSourceService.batchInsert(ServiceSources);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除服务来源关联表", notes = "根据id删除服务来源关联表")
	@Path("/{serviceSourceId}")
    public MessageResult<Void> deleteServiceSource(@PathParam("serviceSourceId")String serviceSourceId){
        serviceSourceService.deleteServiceSource(serviceSourceId);
		return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除服务来源关联表", notes = "批量删除服务来源关联表")
    public MessageResult<Void> batchDelete(List<String> serviceSourceIds){
        serviceSourceService.batchDelete(serviceSourceIds);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

	
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新服务来源关联表", notes = "根据id更新服务来源关联表")
	@Path("/{serviceSourceId}")
    public MessageResult<Void> updateServiceSource(ServiceSource serviceSource,@PathParam("serviceSourceId")String serviceSourceId){
        serviceSource.setServiceSourceId(serviceSourceId);
        serviceSourceService.updateServiceSource(serviceSource);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }

}

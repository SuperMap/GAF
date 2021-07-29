package com.supermap.gaf.storage.resources;

import com.supermap.gaf.common.storage.entity.ObjectInfo;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.storage.entity.Permission;
import com.supermap.gaf.storage.entity.vo.StoragePermissionSelectVo;
import com.supermap.gaf.storage.service.StoragePermissionService;
import com.supermap.gaf.storage.utils.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 接口
 * @author zrc 
 * @date yyyy-mm-dd
 */
@Component
@Api(value = "文件权限管理接口")
public class StoragePermissionResource{
    @Autowired
    private StoragePermissionService storagePermissionService;

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "删除ower的所有资源权限", notes = "删除ower的所有资源权限")
    @Path("/ower/{ower}")
    public MessageResult<Integer> deleteOwerStoragePermission(@PathParam("ower")String ower){
        int re = storagePermissionService.deleteOwerStoragePermission(ower);
        return MessageResult.data(re).status(200).message("更新操作成功").build();
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "更新ower的资源权限", notes = "更新ower的资源权限")
    @Path("/ower/{ower}")
    public MessageResult<Integer> updateOwerStoragePermission(List<Permission> permission, @PathParam("ower")String ower){
        int re = storagePermissionService.updateOwerStoragePermission(permission,ower);
        return MessageResult.data(re).status(200).message("更新操作成功").build();
    }
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "查询携带权限信息的文件列表", notes = "查询携带权限信息的文件列表")
    @Path("/ower/{ower}/{configName}/list-objects/{prefix:.*}")
    public MessageResult<List<ObjectInfo>> listObjects(@DefaultValue("")@PathParam("prefix") String prefix, @DefaultValue("false")@PathParam("configName") String configName, @PathParam("ower")String ower){
        List<ObjectInfo> re = storagePermissionService.listObjectsWithOwerPermissions(ower,configName,prefix);
        return MessageResult.data(re).status(200).message("操作成功").build();

    }


	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
	@Path("/{id}")
    public MessageResult<Permission> getById(@PathParam("id")String id){
        Permission permission = storagePermissionService.getById(id);
		return MessageResult.successe(Permission.class).data(permission).status(200).message("查询成功").build();
    }

	@GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询", notes = "分页条件查询")
    public MessageResult<Page> pageList(@BeanParam StoragePermissionSelectVo storagePermissionSelectVo,
                                        @DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
                                        @DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<Permission> page = storagePermissionService.listByPageCondition(storagePermissionSelectVo, pageNum, pageSize);
		return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }


	@POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "新增", notes = "新增")
    public MessageResult<Void> insertStoragePermission(Permission permission){
        storagePermissionService.insertStoragePermission(permission);
		return MessageResult.successe(Void.class).status(200).message("新增操作成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/batch")
    @ApiOperation(value = "批量新增", notes = "批量新增")
    public MessageResult<Void> batchInsert(List<Permission> permissions){
        storagePermissionService.batchInsert(permissions);
		return MessageResult.successe(Void.class).status(200).message("批量新增操作成功").build();
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id删除", notes = "根据id删除")
	@Path("/{id}")
    public MessageResult<Void> deleteStoragePermission(@PathParam("id")String id){
        storagePermissionService.deleteStoragePermission(id);
		return MessageResult.successe(Void.class).status(200).message("删除操作成功").build();
    }

	@DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "批量删除", notes = "批量删除")
    public MessageResult<Void> batchDelete(List<String> ids){
        storagePermissionService.batchDelete(ids);
		return MessageResult.successe(Void.class).status(200).message("批量删除操作成功").build();
    }

	
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "根据id更新", notes = "根据id更新")
	@Path("/{id}")
    public MessageResult<Void> updateStoragePermission(Permission permission, @PathParam("id")String id){
        permission.setId(id);
        storagePermissionService.updateStoragePermission(permission);
		return MessageResult.successe(Void.class).status(200).message("更新操作成功").build();
    }

}

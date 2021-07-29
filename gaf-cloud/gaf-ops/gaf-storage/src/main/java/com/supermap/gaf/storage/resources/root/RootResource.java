package com.supermap.gaf.storage.resources.root;

import com.amazonaws.services.s3.AmazonS3;
import com.supermap.gaf.common.storage.entity.MinioConfig;
import com.supermap.gaf.common.storage.service.S3ClientService;
import com.supermap.gaf.common.storage.utils.CommonStorageUtils;
import com.supermap.gaf.common.storage.web.FileStorageResource;
import com.supermap.gaf.common.storage.web.VolumeConfigResource;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.storage.entity.Space;
import com.supermap.gaf.storage.entity.vo.SpaceSelectVo;
import com.supermap.gaf.storage.enums.SelectMode;
import com.supermap.gaf.storage.resources.GlobalServerConfigResource;
import com.supermap.gaf.storage.resources.StoragePermissionResource;
import com.supermap.gaf.storage.resources.TenantSpaceConfigResource;
import com.supermap.gaf.storage.service.TenantSpaceConfigService;
import com.supermap.gaf.storage.utils.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Component
@Path("/")
@Api()
public class RootResource implements InitializingBean {

    @Resource
    private S3ClientService s3ClientService;

    @Autowired
    private TenantSpaceConfigService tenantSpaceConfigService;

    @Autowired
    private VolumeConfigResource volumeConfigResource;

    private final Map<String,FileStorageResource> fileStorageResourceMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        for(SelectMode item:SelectMode.values()){
            fileStorageResourceMap.put(item.getPathName(), new FileStorageResource(s3ClientService, item));
        }
    }
    @Path("/api/{selectMode}/{configName}")
    public FileStorageResource fileStorageResource(@PathParam("selectMode") String selectMode) {
        return fileStorageResourceMap.get(selectMode);
    }
    @Path("/tenant-server-configs")
    public Class<TenantSpaceConfigResource> tenantServerConfigResource(){
        return  TenantSpaceConfigResource.class;
    }

    @GET
    @Path("/tenant-allocated-space")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "分页条件查询", notes = "分页条件查询")
    public MessageResult<Page> allocated(@BeanParam SpaceSelectVo spaceSelectVo,
                                         @DefaultValue("1")@QueryParam("pageNum")Integer pageNum,
                                         @DefaultValue("10")@QueryParam("pageSize")Integer pageSize){
        Page<Space> page = tenantSpaceConfigService.allocated(spaceSelectVo, pageNum, pageSize);
        return MessageResult.successe(Page.class).data(page).status(200).message("查询成功").build();
    }
    @Path("/global-server-configs")
    public Class<GlobalServerConfigResource> globalServerConfigResource(){
        return  GlobalServerConfigResource.class;
    }
    @Path("/permissions")
    public Class<StoragePermissionResource> storagePermissionResource(){
        return  StoragePermissionResource.class;
    }
    @Path("/volume")
    public VolumeConfigResource volumeConfigResource() {
        return volumeConfigResource;
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "验证S3Config配置", notes = "验证S3Config配置")
    @Path("/validate-s3config")
    public MessageResult<Void> validateS3Config(MinioConfig minioConfig){
        try{
            AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
            s3Client.doesBucketExistV2(minioConfig.getBucketName());
        }catch (Exception e){
            return MessageResult.failed(Void.class).message(e.getMessage()).build();
        }
        return MessageResult.successe(Void.class).message("验证成功").build();
    }


}

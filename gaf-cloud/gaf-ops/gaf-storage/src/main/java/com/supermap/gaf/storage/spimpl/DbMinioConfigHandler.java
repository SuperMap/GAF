package com.supermap.gaf.storage.spimpl;

import com.supermap.gaf.common.storage.config.StorageConfig;
import com.supermap.gaf.common.storage.config.StorageNotFoundException;
import com.supermap.gaf.common.storage.entity.MinioConfig;
import com.supermap.gaf.common.storage.entity.VolumePathReturn;
import com.supermap.gaf.common.storage.handler.AbstractMinioConfigHandler;
import com.supermap.gaf.common.storage.spi.TenantInfoI;
import com.supermap.gaf.common.storage.utils.CommonStorageUtils;
import com.supermap.gaf.common.storage.web.SelectModeI;
import com.supermap.gaf.storage.dao.S3ServerMapper;
import com.supermap.gaf.storage.dao.SpaceMapper;
import com.supermap.gaf.storage.entity.DBMinioConfig;
import com.supermap.gaf.storage.entity.S3Server;
import com.supermap.gaf.storage.entity.Space;
import com.supermap.gaf.storage.entity.SpaceConfig;
import com.supermap.gaf.storage.entity.vo.SpaceConfigSelectVo;
import com.supermap.gaf.storage.entity.vo.SpaceSelectVo;
import com.supermap.gaf.storage.enums.CreatedType;
import com.supermap.gaf.storage.enums.SelectMode;
import com.supermap.gaf.storage.enums.SelectModeObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class DbMinioConfigHandler extends AbstractMinioConfigHandler {

    @Autowired
    private SpaceMapper spaceMapper;

    @Autowired
    private S3ServerMapper s3ServerMapper;


    public DbMinioConfigHandler(@Autowired StorageConfig storageConfig, @Autowired TenantInfoI tenantInfoI) {
        super(storageConfig, tenantInfoI);
    }

    @Override
    public MinioConfig getConfig(String name, SelectModeI selectModeI) {
        DBMinioConfig re = null;
        ROOT:
        do {
            SelectModeObj selectModeObj = (SelectModeObj) selectModeI.selectMode();
            SpaceSelectVo selectVo = SpaceSelectVo.builder().name(name).target(selectModeObj.getTarget()).build();
            if (selectModeI == SelectMode.TENANT_CREATED_FIRST) {
                selectVo.setOrderFieldName("created_type");
                selectVo.setOrderMethod("desc");
            } else {
                selectVo.setCreatedType(selectModeObj.getCreatedType());
            }
            List<Space> spaces = spaceMapper.selectList(selectVo);
            if (!CollectionUtils.isEmpty(spaces)) {
                Space space = spaces.get(0);
                re = new DBMinioConfig();
                re.setId(space.getId());
                re.setName(name);
                re.setTarget(space.getTarget());
                re.setTotalSize(space.getTotalSize());
                re.setBucketName(space.getStorageName());
                StringBuilder storageNameChain = new StringBuilder();
                storageNameChain.append(space.getStorageName());
                while (!space.getCreatedType().equals(CreatedType.CREATED.getValue())) {
                    List<Space> list = spaceMapper.selectList(SpaceSelectVo.builder().id(space.getParentSpaceId()).build());
                    if (CollectionUtils.isEmpty(list)) {
                        re = null;
                        break ROOT;
                    }
                    space = list.get(0);
                    storageNameChain.append(space.getStorageName());
                }
                int separatorIndex = storageNameChain.indexOf("/");
                if(separatorIndex==-1){
                    re.setBucketName(storageNameChain.toString());
                    re.setDir("");
                }else{
                    re.setBucketName(storageNameChain.substring(0,separatorIndex));
                    re.setDir(storageNameChain.substring(separatorIndex+1));
                }
                re.setRootId(space.getId());
                S3Server s3Server = s3ServerMapper.select(space.getParentSpaceId());
                re.setAccessKey(s3Server.getAccessKey());
                re.setSecretKey(s3Server.getSecretKey());
                re.setServiceEndpoint(s3Server.getServiceEndpoint());

            }
        } while (false);
        if (re == null) {
            throw new StorageNotFoundException(String.format("配置为%s的存储不存在，请到【存储配置管理菜单】先创建或联系平台管理员分配", name));
        }
        return re;
    }

    SpaceConfig getSpaceConfig(Space space) {
        SpaceConfig re = new SpaceConfig();
        re.setId(space.getId());
        re.setName(space.getName());
        re.setTotalSize(space.getTotalSize());
        re.setBucketName(space.getStorageName());
        S3Server s3Server = s3ServerMapper.select(space.getParentSpaceId());
        re.setAccessKey(s3Server.getAccessKey());
        re.setSecretKey(s3Server.getSecretKey());
        re.setServiceEndpoint(s3Server.getServiceEndpoint());
        return re;
    }

    protected String getS3VolumePath(String path) {
        Path root = Paths.get(storageConfig.getMountRoot());
        String re = root.resolve(path).toString();
        if (System.lineSeparator().equals("\r\n")) {
            re = re.replace("\\", "/");
        }
        return re;
    }



    @Override
    public String getVolumeConfigIni(boolean isWin) {
        List<SpaceConfig> spaceConfigs = spaceMapper.selectSpaceConfig(SpaceConfigSelectVo.builder().createdType(CreatedType.CREATED.getValue()).build());
        StringBuilder body = new StringBuilder();
        String lineSeparator = "\n";
        if (isWin) {
            lineSeparator = "\r\n";
        }
        List<String> configs = new ArrayList<>();
        for (SpaceConfig config : spaceConfigs) {
            String bucketName = config.getBucketName();
            int hasSubPath = bucketName.indexOf("/");
            if (hasSubPath != -1) {
                bucketName = bucketName.substring(0, hasSubPath);
                config.setBucketName(bucketName);
            }
            try{
                MinioConfig minioConfig = MinioConfig.builder().bucketName(config.getBucketName()).serviceEndpoint(config.getServiceEndpoint()).accessKey(config.getAccessKey()).secretKey(config.getSecretKey()).build();
                CommonStorageUtils.initBucket(CommonStorageUtils.createClient(minioConfig),minioConfig);
            }catch (Exception e){}
            String volumeId = config.getId();
            configs.add(volumeId);
            body.append(String.format("[%s]", volumeId)).append(lineSeparator);
            body.append(String.format("url=%s", config.getServiceEndpoint())).append(lineSeparator);
            body.append(String.format("key=%s", config.getAccessKey())).append(lineSeparator);
            body.append(String.format("secret=%s", config.getSecretKey())).append(lineSeparator);
            body.append(String.format("bucket=%s", bucketName)).append(lineSeparator);
            body.append(String.format("mntPoint=%s", getS3VolumePath(volumeId))).append(lineSeparator);
        }
        StringBuilder header = new StringBuilder("[config]");
        header.append(lineSeparator).append("section=").append(StringUtils.join(configs, ",")).append(lineSeparator);
        return new String(header.append(body));
    }

    @Override
    public VolumePathReturn getVolumePath(String name, String path, boolean returnUrl, SelectModeI selectMode) {
        DBMinioConfig dbMinioConfig = (DBMinioConfig) getConfig(name, selectMode);
        VolumePathReturn re = new VolumePathReturn();
        String encodePath = dbMinioConfig.getRootId() + "/" + encodeKeyName(dbMinioConfig, path);
        re.setPath(getS3VolumePath(encodePath));
        if (returnUrl) {
            String publicUrl = CommonStorageUtils.createClient(dbMinioConfig).getUrl(dbMinioConfig.getBucketName(), encodePath).toString();
            re.setPublicUrl(publicUrl);
        }
        return re;
    }

}

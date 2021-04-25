/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.storage.service.impl;


import com.supermap.gaf.storage.config.MinioConfig;
import com.supermap.gaf.storage.service.MinioConfigHandlerI;
import com.supermap.gaf.storage.service.TenantMinioConfigService;
import com.supermap.gaf.storage.spi.TenantInfoI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.security.sasl.AuthenticationException;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.supermap.gaf.storage.enums.TenantMode.*;

/**
 * @date:2021/3/25
 * @author heykb
 */
//@Component
//@ConditionalOnMissingBean(MinioConfigHandlerI.class)
public class MultiTenantMinioConfigHandler implements MinioConfigHandlerI {

    @Autowired
    private TenantMinioConfigService tenantMinioConfigService;
    @Autowired(required = false)
    private TenantInfoI tenantInfoI;
    @Override
    public MinioConfig getConfig() {
        try{
            if(tenantMinioConfigService.getMode() == MULTI_NODE || tenantMinioConfigService.getMode() == SINGLE_NODE_MULTI_BUCKET){
                String tenantId = tenantInfoI.getTenantId();
                return tenantMinioConfigService.getConfig().get(tenantId);
            }else{
                return tenantMinioConfigService.getSingleConfig();
            }
        }catch (AuthenticationException e){
            throw new RuntimeException("无法获取租户id");
        }catch (Exception e){
            throw new RuntimeException("请为该租户配置minio服务");
        }
    }



    @Override
    public String getVolumeRootPath() throws AuthenticationException {
        File[] roots = File.listRoots();
        String subPath = "";
        if(tenantMinioConfigService.getMode()!= NONE){
            subPath = tenantInfoI.getTenantId();
        }
        if(roots == null || roots.length<=0){
            return Paths.get("/data-s3fs/"+subPath).toString();
        }else{
            return roots[0].toPath().resolve("data-s3fs/"+subPath).toString();
        }
    }

    @Override
    public String encodeKeyName(String keyName) throws AuthenticationException {
        if(tenantMinioConfigService.getMode()==SINGLE_NODE_MULTI_PATH){
            return tenantInfoI.getTenantId()+"/"+keyName;
        }
        return keyName;
    }

    @Override
    public String decodeKeyName(String encodedKeyName) throws AuthenticationException {
        if(tenantMinioConfigService.getMode()==SINGLE_NODE_MULTI_PATH){
            return encodedKeyName.replaceFirst(tenantInfoI.getTenantId()+"/","");
        }
        return encodedKeyName;
    }

    @Override
    public String getConfigIni() throws AuthenticationException {
        Map<String,MinioConfig> configs = new HashMap<>();
        if(tenantMinioConfigService.getMode() == MULTI_NODE || tenantMinioConfigService.getMode() == SINGLE_NODE_MULTI_BUCKET){
            configs=tenantMinioConfigService.getConfig();
        }else{
            configs.put("single",tenantMinioConfigService.getSingleConfig());
        }
        if(CollectionUtils.isEmpty(configs)){
            return "";
        }
        StringBuilder sb = new StringBuilder("[config]\n");
        sb.append("section=").append(StringUtils.join(configs.keySet(),",")).append("\n");
        for(String configName:configs.keySet()){
            sb.append(String.format("[%s]\n",configName));
            MinioConfig config = configs.get(configName);
            sb.append(String.format("url=%s\n",config.getServiceEndpoint()));
            sb.append(String.format("key=%s\n",config.getAccessKey()));
            sb.append(String.format("secret=%s\n",config.getSecretKey()));
            sb.append(String.format("bucket=%s\n",config.getBucketName()));
            if("single".equals(configName)){
                sb.append("mntPoint=/data-s3fs\n");
            }else{
                sb.append(String.format("mntPoint=/data-s3fs/%s\n",configName));
            }
        }
        return new String(sb);
    }

}

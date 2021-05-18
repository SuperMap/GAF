/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.storage.service.impl;

import com.supermap.gaf.storage.config.MinioConfig;
import com.supermap.gaf.storage.config.TenantMinioConfig;
import com.supermap.gaf.storage.enums.TenantMode;
import com.supermap.gaf.storage.service.TenantMinioConfigService;
import com.supermap.gaf.storage.spi.TenantInfoI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.util.Map;

/**
 * @date:2021/3/25
 * @author heykb
 */
@Service
public class TenantMinioConfigServiceImpl implements TenantMinioConfigService {
    @Autowired
    private TenantMinioConfig tenantMinioConfig;

    @Autowired
    private TenantInfoI tenantInfoI;

    @Override
    public TenantMode getMode() {
        return tenantMinioConfig.getMode();
    }

    @Override
    public MinioConfig getSingleConfig() {
        return tenantMinioConfig.getSingleConfig();
    }

    @Override
    public Map<String, MinioConfig> getConfig() throws AuthenticationException {
        if(tenantMinioConfig.getMode()==TenantMode.SINGLE_NODE_MULTI_BUCKET){
            for(String key:tenantMinioConfig.getConfig().keySet()){

                MinioConfig minioConfig = tenantMinioConfig.getConfig().get(key);
                String bucketName = minioConfig.getBucketName();
                if(StringUtils.isEmpty(minioConfig.getBucketName())){
                    bucketName = tenantInfoI.getTenantId();
                }
                MinioConfig singleConfig = tenantMinioConfig.getSingleConfig();
                BeanUtils.copyProperties(singleConfig,minioConfig);
                minioConfig.setBucketName(bucketName);
            }
        }
        return tenantMinioConfig.getConfig();
    }

    @Override
    public String getMountRoot() {
        return tenantMinioConfig.getMountRoot();
    }
}

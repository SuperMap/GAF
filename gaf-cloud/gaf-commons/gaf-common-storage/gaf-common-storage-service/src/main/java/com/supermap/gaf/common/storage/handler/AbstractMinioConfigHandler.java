/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.common.storage.handler;

import com.supermap.gaf.common.storage.config.StorageConfig;
import com.supermap.gaf.common.storage.entity.MinioConfig;
import com.supermap.gaf.common.storage.spi.TenantInfoI;
import org.springframework.util.StringUtils;

/**
 * @author heykb
 * @date:2021/3/25
 */
public abstract class AbstractMinioConfigHandler implements MinioConfigHandlerI {

    protected StorageConfig storageConfig;
    protected TenantInfoI tenantInfoI;

    public AbstractMinioConfigHandler(StorageConfig storageConfig, TenantInfoI tenantInfoI) {
        this.storageConfig = storageConfig;
        this.tenantInfoI = tenantInfoI;
    }

    @Override
    public String encodeKeyName(MinioConfig minioConfig, String keyName) {
        if (StringUtils.isEmpty(minioConfig.getDir())) {
            return keyName;
        } else {
            return minioConfig.getDir() + keyName;
        }
    }

    @Override
    public String decodeKeyName(MinioConfig minioConfig, String encodedKeyName) {
        if (StringUtils.isEmpty(minioConfig.getDir())) {
            return encodedKeyName;
        } else {
            return encodedKeyName.substring(minioConfig.getDir().length());
        }
    }


}

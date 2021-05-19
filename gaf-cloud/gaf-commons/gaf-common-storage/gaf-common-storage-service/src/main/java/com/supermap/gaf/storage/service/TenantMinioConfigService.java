/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.storage.service;

import com.supermap.gaf.storage.config.MinioConfig;
import com.supermap.gaf.storage.enums.TenantMode;

import javax.security.sasl.AuthenticationException;
import java.util.Map;

/**
 * The interface Tenant minio config service.
 *
 * @date:2021/3/25
 * @author heykb
 */
public interface TenantMinioConfigService {
    /**
     * Gets mode.
     *
     * @return the mode
     */
    TenantMode getMode();

    /**
     * Gets single config.
     *
     * @return the single config
     */
    MinioConfig getSingleConfig();

    /**
     * Gets config.
     *
     * @return the config
     */
    Map<String,MinioConfig> getConfig() throws AuthenticationException;


    default String getMountRoot(){
        return "/data-s3fs";
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.storage.config;

import com.supermap.gaf.storage.enums.TenantMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @date:2021/3/25
 * @author heykb
 */
@ConfigurationProperties(prefix = "minio")
@Data
@Configuration
public class TenantMinioConfig {
    private Map<String, MinioConfig> config;
    private MinioConfig singleConfig;
    private TenantMode mode = TenantMode.SINGLE_NODE_MULTI_PATH;
    public void setMode(String mode) {
        this.mode = TenantMode.valueOf(mode);
    }
    private String mountRoot="/data-s3fs";
}

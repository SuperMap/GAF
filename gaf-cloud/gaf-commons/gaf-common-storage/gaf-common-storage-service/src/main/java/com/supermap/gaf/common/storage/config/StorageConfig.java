package com.supermap.gaf.common.storage.config;

import com.supermap.gaf.common.storage.enums.TenantMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author heykb
 */
@ConfigurationProperties(prefix = "gaf.storage")
@Data
public class StorageConfig {
    //    private String defaultConfig = "default";
    private TenantMode mode = TenantMode.SINGLE_NODE_MULTI_BUCKET;
    private String mountRoot = "/data-s3fs";

    public void setMode(String mode) {
        this.mode = TenantMode.valueOf(mode);
    }
}

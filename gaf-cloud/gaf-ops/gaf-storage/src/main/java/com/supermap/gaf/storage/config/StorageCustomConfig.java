package com.supermap.gaf.storage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author kb
 */
@Configuration
public class StorageCustomConfig {
    public static String TENANTID_HEADER;
    public static String PERMISSION_HEADER;
    public static String SUPER_OWER;
    public static boolean PERMISSION_SWITCH;
    public static List<String> MOUNT_RELOAD_URLS;

    @Value("${GAF_STORAGE_TENANTID_HEADER:TENANT_ID}")
    public void setTenantIdHeader(String tenantIdHeader) {
        StorageCustomConfig.TENANTID_HEADER = tenantIdHeader;
    }

    @Value("${GAF_STORAGE_PERMISSION_HEADER:PERMISSION}")
    public void setPermissionHeader(String permissionHeader) {
        StorageCustomConfig.PERMISSION_HEADER = permissionHeader;
    }

    @Value("${GAF_STORAGE_PERMISSION_ENABLE:false}")
    public void setPermissionSwitch(boolean permissionSwitch) {
        PERMISSION_SWITCH = permissionSwitch;
    }

    @Value("${GAF_STORAGE_SUPER_OWER:admin}")
    public void setSuperOwer(String superOwer) {
        StorageCustomConfig.SUPER_OWER = superOwer;
    }

    @Value("${GAF_STORAGE_MOUNT_RELOAD_URLS:}")
    public void setMountReloadUrls(List<String> mountReloadUrls) {
        MOUNT_RELOAD_URLS = mountReloadUrls;
    }
}

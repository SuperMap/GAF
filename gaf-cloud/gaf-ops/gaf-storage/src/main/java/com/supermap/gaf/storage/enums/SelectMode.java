package com.supermap.gaf.storage.enums;

import com.supermap.gaf.common.storage.spi.exceptions.StorageTenantException;
import com.supermap.gaf.common.storage.web.SelectModeI;
import com.supermap.gaf.storage.config.StorageCustomConfig;
import com.supermap.gaf.storage.filter.RequestFilter;
import org.springframework.util.StringUtils;

public enum SelectMode implements SelectModeI {
    PLATFORM("platform") {
        @Override
        public Object selectMode() {
            return SelectModeObj.builder().target("").createdType(CreatedType.CREATED.getValue()).build();
        }
    },
    TENANT_CREATED("tenant-created") {
        @Override
        public Object selectMode() {
            String tenantId = RequestFilter.requestContextHolder.get().getHeaderString(StorageCustomConfig.TENANTID_HEADER);
            if (tenantId == null) {
                throw new StorageTenantException();
            }
            return SelectModeObj.builder().target(tenantId).createdType(CreatedType.CREATED.getValue()).build();
        }
    },
    TENANT_ALLOCATED("tenant-allocated") {
        @Override
        public Object selectMode() {
            String tenantId = RequestFilter.requestContextHolder.get().getHeaderString(StorageCustomConfig.TENANTID_HEADER);
            if (StringUtils.isEmpty(tenantId)) {
                throw new StorageTenantException();
            }
            return SelectModeObj.builder().target(tenantId).createdType(CreatedType.ALLOCATED.getValue()).build();
        }
    },
    TENANT_CREATED_FIRST("tenant-created-first") {
        @Override
        public Object selectMode() {
            String tenantId = RequestFilter.requestContextHolder.get().getHeaderString(StorageCustomConfig.TENANTID_HEADER);
            if (StringUtils.isEmpty(tenantId)) {
                throw new StorageTenantException();
            }
            return SelectModeObj.builder().target(tenantId).createdType(null).build();
        }
    };
    private String pathName;

    SelectMode(String pathName) {
        this.pathName = pathName;
    }

    public String getPathName() {
        return pathName;
    }
}

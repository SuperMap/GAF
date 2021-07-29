package com.supermap.gaf.storage.spimpl;

import com.supermap.gaf.common.storage.spi.TenantInfoI;
import com.supermap.gaf.common.storage.spi.exceptions.StorageTenantException;
import com.supermap.gaf.storage.filter.RequestFilter;
import com.supermap.gaf.storage.config.StorageCustomConfig;
import org.springframework.stereotype.Component;

@Component
public class CustomTenantInfoI implements TenantInfoI {

    @Override
    public String getTenantId() throws StorageTenantException {
        String tenantId =  RequestFilter.requestContextHolder.get().getHeaderString(StorageCustomConfig.TENANTID_HEADER);
        if(tenantId==null){
            tenantId = "default";
//            throw new StorageTenantException();
        }
        return tenantId.replace("Bearer ","");
    }
}

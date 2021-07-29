/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.shiro.handler;

import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.common.storage.spi.TenantInfoI;
import com.supermap.gaf.common.storage.spi.exceptions.StorageTenantException;

/**
 * The type Tenant info.
 * @date:2021/3/25
 * @author heykb
 */
public class TenantInfoImpl implements TenantInfoI {

    /**
     * Gets tenant id.
     *
     * @return the tenant id
     * @throws StorageTenantException the authentication exception
     */
    @Override
    public String getTenantId() throws StorageTenantException {
        try{
            return SecurityUtilsExt.getUser().getAuthUser().getTenantId().replace("_","-");
        }catch (Exception e){
            throw new StorageTenantException();
        }
    }

}

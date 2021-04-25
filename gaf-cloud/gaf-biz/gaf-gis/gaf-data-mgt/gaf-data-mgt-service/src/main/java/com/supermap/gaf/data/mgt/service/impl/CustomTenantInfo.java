/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.service.impl;

import com.supermap.gaf.shiro.handler.TenantInfoImpl;
import com.supermap.gaf.storage.spi.TenantInfoI;

import javax.security.sasl.AuthenticationException;

/**
 * The type Custom tenant info.
 *
 * @date:2021/3/25
 * @author heykb
 */
public class CustomTenantInfo implements TenantInfoI {
    @Override
    public String getTenantId() throws AuthenticationException {
        return "default";
    }
}

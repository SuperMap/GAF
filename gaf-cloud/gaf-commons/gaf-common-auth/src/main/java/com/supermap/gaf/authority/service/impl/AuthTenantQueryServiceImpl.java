/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthTenant;
import com.supermap.gaf.authority.dao.AuthTenantQueryMapper;
import com.supermap.gaf.authority.service.AuthTenantQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dqc
 * @date:2021/3/25
 * 租户服务类
 */
@Service
public class AuthTenantQueryServiceImpl implements AuthTenantQueryService {
    @Autowired
    private AuthTenantQueryMapper authTenantQueryMapper;


    /**
     * id查询租户
     *
     * @return
     */
    @Override
    public AuthTenant getById(String tenantId) {
        return authTenantQueryMapper.select(tenantId);
    }

}

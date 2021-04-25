/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthRoleApi;
import com.supermap.gaf.authority.constant.CacheGroupConstant;
import com.supermap.gaf.authority.dao.AuthRoleApiQueryMapper;
import com.supermap.gaf.authority.service.AuthRoleApiQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author yangdong
 * @date:2021/3/25
 * 角色授权接口服务类
 */
@Service
public class AuthRoleApiQueryServiceImpl implements AuthRoleApiQueryService {
    @Autowired
    private AuthRoleApiQueryMapper authRoleApiQueryMapper;

    @Cacheable(value = CacheGroupConstant.ROLE_API, key = "#roleId", unless = "#result == null")
    @Override
    public List<AuthRoleApi> listByRole(String roleId) {
        return authRoleApiQueryMapper.listByRole(roleId);
    }


}

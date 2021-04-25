/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthRoleModule;
import com.supermap.gaf.authority.constant.CacheGroupConstant;
import com.supermap.gaf.authority.constant.DbFieldNameConstant;
import com.supermap.gaf.authority.dao.AuthRoleModuleQueryMapper;
import com.supermap.gaf.authority.service.AuthRoleModuleQueryService;
import com.supermap.gaf.authority.vo.AuthRoleModuleSelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author dqc
 * @date:2021/3/25
 * 角色授权模块服务类
 */
@Service
public class AuthRoleModuleQueryServiceImpl implements AuthRoleModuleQueryService {
    @Autowired
    private AuthRoleModuleQueryMapper authRoleModuleQueryMapper;



    @Cacheable(value = CacheGroupConstant.ROLE_MODULE, key = "#roleId", unless = "#result == null")
    @Override
    public List<AuthRoleModule> listByRole(String roleId) {
        return authRoleModuleQueryMapper.listByRole(roleId);
    }


}

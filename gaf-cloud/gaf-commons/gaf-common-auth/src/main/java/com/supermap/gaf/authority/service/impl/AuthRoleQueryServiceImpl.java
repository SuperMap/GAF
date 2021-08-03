/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthRole;
import com.supermap.gaf.authority.dao.AuthRoleQueryMapper;
import com.supermap.gaf.authority.service.AuthRoleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author dqc
 * @date:2021/3/25 角色服务类
 */
@Service
public class AuthRoleQueryServiceImpl implements AuthRoleQueryService {
    @Autowired
    private AuthRoleQueryMapper authRoleQueryMapper;


    /**
     * id查询角色
     *
     * @return
     */
    @Override
    public AuthRole getById(String roleId) {
        return authRoleQueryMapper.select(roleId);
    }


}

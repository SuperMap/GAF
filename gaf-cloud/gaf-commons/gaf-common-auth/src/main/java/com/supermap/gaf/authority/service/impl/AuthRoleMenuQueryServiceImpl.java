/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthRoleMenu;
import com.supermap.gaf.authority.dao.AuthRoleMenuQueryMapper;
import com.supermap.gaf.authority.service.AuthRoleMenuQueryService;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yangdong
 * @date:2021/3/25
 * 角色授权菜单服务类
 */
@Service
public class AuthRoleMenuQueryServiceImpl implements AuthRoleMenuQueryService {
    @Autowired
    private AuthRoleMenuQueryMapper authRoleMenuMapper;

    private static Logger logger = LogUtil.getLocLogger(AuthRoleMenuQueryServiceImpl.class);

    @Override
    public List<AuthRoleMenu> listByRole(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return new ArrayList<>();
        }
        return authRoleMenuMapper.listByRole(roleId);
    }
}

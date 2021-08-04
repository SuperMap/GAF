/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthRoleMenu;

import java.util.List;

/**
 * @author yangdong
 * @date:2021/3/25 角色授权菜单服务类
 */
public interface AuthRoleMenuQueryService {

    /**
     * roleId查询角色授权菜单
     *
     * @return
     */
    List<AuthRoleMenu> listByRole(String roleId);

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.*;

import java.util.List;

/**
 * 权限相关查询接口
 *
 * @author : duke
 * @date:2021/3/25
 * @since 2020/10/25 9:36 PM
 */
public interface AuthAuthorizationQueryService {
    /**
     * 查询所有api接口权限
     *
     * @return
     */
    List<AuthResourceApi> listAuthorizationApi(String userId);

    /**
     * 查询所有module接口权限
     *
     * @return
     */
    List<AuthResourceModule> listAuthorizationModule(String userId);

    /**
     * 查询人员菜单
     *
     * @return
     */
    List<AuthResourceMenu> listAuthorizationMenu(String userId);

    /**
     * 查询所有角色权限
     *
     * @param userId
     * @return
     */
    List<AuthRole> listAuthorizationRole(String userId);

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.portal.menu.service;

import com.supermap.gaf.portal.menu.commontypes.CustomationInfo;
import com.supermap.gaf.portal.menu.commontypes.MenuInfo;

import java.util.List;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
public interface CustomationServices {
    /**
     * 根据租户id查询租户门户定制信息
     *
     * @param tenantId 租户id
     * @return json
     */
    String queryCustomation(String tenantId);

    /**
     * 查询配置信息
     *
     * @return
     */
    CustomationInfo queryConfig();

    /**
     * 查看默认定制信息
     * @return
     */
    CustomationInfo queryDefaultConfig();
    /**
     * 查询菜单列表
     *
     * @return
     */
    List<MenuInfo> queryMenus();
}

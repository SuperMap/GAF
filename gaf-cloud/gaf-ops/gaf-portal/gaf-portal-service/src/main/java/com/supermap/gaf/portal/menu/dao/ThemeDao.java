/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.portal.menu.dao;

import com.supermap.gaf.portal.menu.commontypes.ThemeInfo;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
public interface ThemeDao {
    /**
     * 新增布局设置数据信息
     *
     * @param theme 布局设置数据对象
     * @return
     */
    int addTheme(ThemeInfo theme);

    /**
     * 修改布局设置数据信息
     *
     * @param theme
     * @return
     */
    int updateTheme(ThemeInfo theme);

    /**
     * 查询用户的布局设置数据信息
     *
     * @param user
     * @return
     */
    ThemeInfo queryTheme(String user);
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.portal.menu.service;

import com.supermap.gaf.portal.menu.commontypes.ThemeInfo;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
public interface ThemeService {
    /**
     * 新增|更新主题配置
     * @param theme 主题配置数据对象
     * @return
     */
    String saveTheme(ThemeInfo theme); //新增|更新主题配置

    /**
     * 查询用户的主题配置信息
     * @return
     */
    String queryTheme();
}

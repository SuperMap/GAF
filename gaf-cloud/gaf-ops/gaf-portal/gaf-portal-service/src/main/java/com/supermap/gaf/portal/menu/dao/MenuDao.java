/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.portal.menu.dao;

import com.supermap.gaf.portal.menu.commontypes.MenuInfo;

import java.util.List;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
public interface MenuDao {
    /**
     * 新增菜单数据
     *
     * @param menu 菜单数据对象
     * @return
     */
    int addMenu(MenuInfo menu);

    /**
     * 根据菜单id 删除菜单
     *
     * @param menuId 菜单信息ID
     * @return
     */
    int deleteMenuById(String menuId);

    /**
     * 根据flag信息删除菜单
     *
     * @param flag
     * @return
     */
    int deleteMenuByFlag(String flag);

    /**
     * 根据id批量删除菜单
     *
     * @param menuIds id数组
     * @return
     */
    int deleteMenuByIds(List<String> menuIds);

    /**
     * 修改菜单信息
     *
     * @param menu 菜单数据对象
     * @return
     */
    int updateMenu(MenuInfo menu);

    /**
     * 根据id查询菜单详细信息
     *
     * @param menuId 菜单id
     * @return 菜单数据对象
     */
    MenuInfo queryMenuById(String menuId);

    /**
     * 查询所有菜单信息
     *
     * @return 菜单数据对象数组
     */
    List<MenuInfo> queryMenus();
}

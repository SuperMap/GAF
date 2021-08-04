/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.portal.menu.service;

import com.supermap.gaf.portal.menu.commontypes.MenuInfo;

import java.util.List;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
public interface MenuService {
    /**
     * 新增菜单
     *
     * @param menu 菜单数据对象
     * @return
     */
    String addMenu(MenuInfo menu);

    /**
     * 根据菜单id删除菜单信息
     *
     * @param menuId 菜单id
     * @return
     */
    String deleteMenu(String menuId);

    /**
     * 修改菜单信息
     *
     * @param menuId 菜单id
     * @param menu   菜单数据对象
     * @return
     */
    String updateMenu(String menuId, MenuInfo menu);

    /**
     * 根据菜单id 查看菜单详情信息
     *
     * @param menuId 菜单id
     * @return
     */
    String queryMenu(String menuId);

    /**
     * 查询该登录人所有菜单信息
     *
     * @return json
     */
    String queryMenu();

    /**
     * 查看用户菜单信息
     *
     * @return
     */
    List<MenuInfo> queryMenuFromAuthority();
//    String importAllSpecial(String parentId);

    /**
     * 导入菜单信息
     *
     * @param flag 导入信息
     * @param arr  导入菜单信息
     * @return
     */
    String importMenus(String flag, List<MenuInfo> arr);
}

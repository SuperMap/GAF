/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.portal.menu.service.impl;

import com.alibaba.fastjson.JSON;
import com.supermap.gaf.authority.commontype.AuthResourceMenu;
import com.supermap.gaf.authority.commontype.AuthResourceModule;
import com.supermap.gaf.authority.service.AuthAuthorizationQueryService;
import com.supermap.gaf.portal.menu.commontypes.MenuInfo;
import com.supermap.gaf.portal.menu.dao.MenuDao;
import com.supermap.gaf.portal.menu.service.MenuService;
import com.supermap.gaf.portal.menu.utils.MenuManager;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
@Service
@Qualifier("portal")
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private AuthAuthorizationQueryService authAuthorizationQueryService;

    @Override
    public String addMenu(MenuInfo menu) {
        return null;
    }

    @Override
    public String deleteMenu(String menuId) {
        return null;
    }

    @Override
    public String updateMenu(String menuId, MenuInfo menu) {
        return null;
    }

    @Override
    public String queryMenu(String menuId) {
        return null;
    }

    private List<MenuInfo> getMenuArrPermession(ShiroUser shiroUser) {
        List<String> permessions = shiroUser.getPermerssion();
        List<MenuInfo> menuArr = menuDao.queryMenus();
        Map<String, String> menuMap = MenuManager.getMenuMapping(menuArr);
        Set<String> permessionMenus = new HashSet<>();
        boolean permessionAll = false;
        for (String permession : permessions) {
            if (permession.contains("menu")) {
                String[] items = permession.trim().split(":");
                if (items != null && items.length > 0) {
                    for (int i = 0; i < items.length; i++) {
                        String item = items[i].trim();
                        if ("menu".equals(item)) {
                            int j = i + 1;
                            // eg: menu:A: menu:A:a 意义是冲突的(一个要求是A的所有子集菜单，一个要求只是A下的a菜单)，所以增加menus:A:*表达A下所有子菜单，menu:A:含义只是表面要加载下面菜单
                            if (j == items.length || "*".equals(items[j])) {
                                permessionAll = true;
                            } else {
                                while (j < items.length) {
                                    // item是菜单Name
                                    item = items[j].trim();
                                    if (j == items.length - 1 && "*".equals(item)) {
                                        String id = menuMap.get(items[j - 1]);
                                        if (id != null && !id.isEmpty()) {
                                            List<String> menuIds = MenuManager.getSubMenuIds(menuArr, id);
                                            permessionMenus.addAll(menuIds);
                                        }
                                    } else {
                                        String id = menuMap.get(item);
                                        permessionMenus.add(id);
                                    }
                                    j++;
                                }
                            }
                            break;
                        }
                    }
                }
                if (permessionAll == true) {
                    break;
                }
            }
        }

        // 这个逻辑可以优化，上边直接获取菜单添加，而不是获取id
        List<MenuInfo> menuArrPermession = new ArrayList<>();
        // menu:
        if (permessionAll) {
            menuArrPermession.addAll(menuArr);
        } else {
            for (MenuInfo menu : menuArr) {
                if (permessionMenus.contains(menu.getId())) {
                    menuArrPermession.add(menu);
                }
            }
        }
        return menuArrPermession;
    }
    @Override
    public String queryMenu() {
        String msg;
        boolean success = false;
        Map<String, Object> res = new HashMap<String, Object>(16);

        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        // 未经过shiro授权 先返回全部菜单
        if (shiroUser == null) {
            List<MenuInfo> menuArr = menuDao.queryMenus();
            if (menuArr.size() > 0) {
                success = true;
                msg = "查询菜单成功";
            } else {
                success = false;
                msg = "查询不到菜单信息";
            }
            res.put("menus", menuArr);
            res.put("success", success);
            res.put("msg", msg);
            return JSON.toJSONString(res);
        }
        List<MenuInfo> menuArrPermession = getMenuArrPermession(shiroUser);
        if (menuArrPermession.size() > 0) {
            success = true;
            msg = "查询菜单成功";
        } else {
            success = false;
            msg = "查询不到菜单信息";
        }
        res.put("menus", menuArrPermession);
        res.put("success", success);
        res.put("msg", msg);
        return JSON.toJSONString(res);
    }

    @Override
    public List<MenuInfo> queryMenuFromAuthority() {
        String msg;
        boolean success;
        List<MenuInfo> menuInfos = new ArrayList<>();
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        if (null == shiroUser) {
            return menuInfos;
        }
        List<AuthResourceMenu> authResourceMenus = authAuthorizationQueryService.listAuthorizationMenu(shiroUser.getId());
        if (CollectionUtils.isNotEmpty(authResourceMenus)) {
            // 去掉根节点 并将根节点下的节点的menuCatalogId设置为""
            Set<String> rootMenuIds = new HashSet<>();
            Iterator<AuthResourceMenu> iterator = authResourceMenus.iterator();
            for (; iterator.hasNext();) {
                AuthResourceMenu menu = iterator.next();
                if ("0".equals(menu.getMenuCatalogId())) {
                    rootMenuIds.add(menu.getResourceMenuId());
                    iterator.remove();
                }
            }
            authResourceMenus.forEach(authResourceMenu -> {
                if (rootMenuIds.contains(authResourceMenu.getMenuCatalogId())) {
                    authResourceMenu.setMenuCatalogId("");
                }
                menuInfos.add(convertMenu(authResourceMenu));
            });
            success = true;
            msg = "查询菜单成功";
        } else {
            success = false;
            msg = "查询不到菜单信息";
        }
        return menuInfos;
    }

    private MenuInfo convertMenu(AuthResourceMenu authResourceMenu) {
        MenuInfo menuInfo = new MenuInfo();
        menuInfo.setPath("");
        menuInfo.setTarget("0");
        menuInfo.setEmbedUrl("");

        menuInfo.setId(authResourceMenu.getResourceMenuId());
        menuInfo.setPid(authResourceMenu.getMenuCatalogId());

        menuInfo.setName(authResourceMenu.getName());
        if (StringUtils.isEmpty(authResourceMenu.getIcon())) {
            menuInfo.setIcon("");
        } else {
            menuInfo.setIcon(authResourceMenu.getIcon());
        }
        if (null == authResourceMenu.getSortSn()) {
            menuInfo.setOrder(0);
        } else {
            menuInfo.setOrder(authResourceMenu.getSortSn());
        }

        //菜单的模块信息
        AuthResourceModule authResourceModule = authResourceMenu.getAuthResourceModule();
        if (authResourceModule == null) {
            return menuInfo;
        }
        String containsParam = "?";
        //菜单实际地址（带模块入口参数）
        if(!StringUtils.isBlank(authResourceMenu.getParams())) {
            if(!StringUtils.isBlank(authResourceModule.getModuleUrl()) && !authResourceModule.getModuleUrl().contains(containsParam)) {
                authResourceModule.setModuleUrl(authResourceModule.getModuleUrl() + "?");
            }
            authResourceModule.setModuleUrl(authResourceModule.getModuleUrl() + authResourceMenu.getParams().trim());

        }
        menuInfo.setEmbedUrl(authResourceModule.getModuleUrl());
        if (!StringUtils.isEmpty(authResourceModule.getPath())) {
            menuInfo.setPath(authResourceModule.getPath());
        }
        String equalParam = "1";
        if (equalParam.equals(authResourceModule.getTarget())) {
            menuInfo.setTarget(equalParam);
        }
        String param ="2";
        if (param.equals(authResourceModule.getType())) {
            menuInfo.setEmbed(true);
        }

        return menuInfo;
    }

    @Deprecated
    /** 菜单数据结构转换 */
    private MenuInfo convertMenu(AuthResourceModule authResourceModule) {
        MenuInfo menuInfo = new MenuInfo();
        menuInfo.setId(authResourceModule.getResourceModuleId());
        String equalsParam = "0";
        if (equalsParam.equals(authResourceModule.getModuleCatalogId())) {
            menuInfo.setPid("");
        } else {
            menuInfo.setPid(authResourceModule.getModuleCatalogId());
        }
        menuInfo.setName(authResourceModule.getName());
        if (StringUtils.isEmpty(authResourceModule.getIconUrl())) {
            menuInfo.setIcon("");
        } else {
            menuInfo.setIcon(authResourceModule.getIconUrl());
        }
        if (null == authResourceModule.getSortSn()) {
            menuInfo.setOrder(0);
        } else {
            menuInfo.setOrder(authResourceModule.getSortSn());
        }
        if (!StringUtils.isEmpty(authResourceModule.getPath())) {
            menuInfo.setPath(authResourceModule.getPath());
        } else {
            menuInfo.setPath("");
        }
        String param = "2";
        if (param.equals(authResourceModule.getType())) {
            menuInfo.setEmbed(true);
            menuInfo.setEmbedUrl(authResourceModule.getModuleUrl());
        } else {
            menuInfo.setEmbedUrl("");
        }
        menuInfo.setTarget("0");
        return menuInfo;
    }

    @Override
    public String importMenus(String flag, List<MenuInfo> arr) {
        return null;
    }
}

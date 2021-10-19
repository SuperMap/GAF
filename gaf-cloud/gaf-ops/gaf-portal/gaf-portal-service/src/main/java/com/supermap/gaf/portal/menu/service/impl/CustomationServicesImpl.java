/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.portal.menu.service.impl;

import com.alibaba.fastjson.JSON;
import com.supermap.gaf.portal.menu.commontypes.CustomationInfo;
import com.supermap.gaf.portal.menu.commontypes.MenuInfo;
import com.supermap.gaf.portal.menu.dao.CustomationDao;
import com.supermap.gaf.portal.menu.dao.MenuDao;
import com.supermap.gaf.portal.menu.service.CustomationServices;
import com.supermap.gaf.portal.menu.utils.MenuManager;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import com.supermap.gaf.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author Yw
 * @date:2021/3/25
 */
@Service
@Slf4j
public class CustomationServicesImpl implements CustomationServices {
    @Autowired
    private CustomationDao customationDao;
    private static Logger logger = LogUtil.getLocLogger(CustomationServicesImpl.class);
    @Autowired
    private MenuDao menuDao;
    @Value("${gaf.portal.defaultTenantId:tenant_000000}")
    private String defaultTenantId;

    @Override
    public String queryCustomation(String tenantId) {
        String msg;
        boolean success = true;

        CustomationInfo customation = getCustomizedPortalConfig(tenantId);

        Map<String, Object> res = new HashMap<String, Object>(16);
        res.put("customization", customation);
        msg = customation != null ? "查询定制信息成功;" : "查询不到定制信息;0";

        List<MenuInfo> menuArr = menuDao.queryMenus();
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        log.info("customation:{}", shiroUser);
        // 未经过shiro授权 先返回全部菜单
        if (shiroUser == null) {
            msg += menuArr.size() > 0 ? "查询菜单成功" : "查询不到菜单信息1";
            res.put("menus", menuArr);
            res.put("success", success);
            res.put("msg", msg);
            return JSON.toJSONString(res);
        }

        //获取授权菜单
        List<MenuInfo> menuArrPermession = getPermissionMenusNsip(menuArr);
        msg += menuArrPermession.size() > 0 ? "查询菜单成功" : "查询不到菜单信息2";
        res.put("menus", menuArrPermession);
        res.put("success", success);
        res.put("msg", msg);
        return JSON.toJSONString(res);
    }

    public String queryCustomationDiscard(String userName) {
        String msg;
        boolean success = false;

        Map<String, Object> res = new HashMap<String, Object>(16);
        //"admin";
        String user = userName;
        CustomationInfo customation = customationDao.queryCustomation(user);
        if (customation != null) {
            success = true;
        }
        msg = customation != null ? "查询定制信息成功;" : "查询不到定制信息;0";
        res.put("customization", customation);

        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        List<MenuInfo> menuArr = menuDao.queryMenus();
        // 未经过shiro授权 先返回全部菜单
        if (shiroUser == null) {
            msg += menuArr.size() > 0 ? "查询菜单成功" : "查询不到菜单信息1";
            res.put("menus", menuArr);
            res.put("success", success);
            res.put("msg", msg);
            return JSON.toJSONString(res);
        }


        List<MenuInfo> menuArrPermession = getPermissionMenus(menuArr);

        msg += menuArrPermession.size() > 0 ? "查询菜单成功" : "查询不到菜单信息2";
        res.put("menus", menuArrPermession);
        res.put("success", success);
        res.put("msg", msg);
        return JSON.toJSONString(res);
    }

    @Override
    public CustomationInfo queryConfig() {
        //String userName = "admin";
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        if (shiroUser == null) {
            return null;
        }
        String tenantId = shiroUser.getTenantId();
        return getCustomizedPortalConfig(tenantId);
    }
    @Override
    public CustomationInfo queryDefaultConfig() {
        if(defaultTenantId == null || "".equals(defaultTenantId)) {
            return null;
        }
        CustomationInfo customizedPortalConfig = getCustomizedPortalConfig(defaultTenantId);
        customizedPortalConfig.setUser(null);
        customizedPortalConfig.setTenantId(null);
        return customizedPortalConfig;
    }
    @Override
    public List<MenuInfo> queryMenus() {
        List<MenuInfo> menuInfos = menuDao.queryMenus();
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        if (shiroUser == null) {
            return menuInfos;
        } else {
            return getPermissionMenus(menuInfos);
        }
    }


    private List<MenuInfo> getPermissionMenusNsip(List<MenuInfo> menuArr) {
        List<MenuInfo> accessedMenuInfos = new ArrayList<>();
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        log.info("customation:{}", shiroUser);
        if (shiroUser == null) {
            return accessedMenuInfos;
        }

        List<String> permessions = shiroUser.getPermerssion();
        log.info("permessions:{}", JSON.toJSONString(permessions));
        if (CollectionUtils.isEmpty(permessions)) {
            return accessedMenuInfos;
        }
        String containsParam = "menu:*:*";
        if (permessions.contains(containsParam)) {
            return menuArr;
        } else {
            MenuManager.concatName(menuArr);
            List<String> menuPermessions = permessions.stream().filter(permession -> permession.startsWith("menu:")).collect(Collectors.toList());
            accessedMenuInfos = menuArr.stream().filter(menu -> {
                        for (String permession : menuPermessions) {
                            String[] menuNames = menu.getName().split(":");
                            String menuName = StringUtils.join(menuNames, ":", 1, menuNames.length);
                            String permessionMenuName = permession.substring(permession.indexOf(":") + 1, permession.lastIndexOf(":"));
                            if (permessionMenuName.startsWith(menuName)) {
                                System.out.println("matched//" + menuName + "====" + permessionMenuName);
                                menu.setName(menuNames[0]);
                                return true;
                            }
                        }
                        return false;
                    }
            ).collect(Collectors.toList());

        }
        return accessedMenuInfos;
    }

    private List<MenuInfo> getPermissionMenus(List<MenuInfo> menuArr) {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        List<String> permessions = shiroUser.getPermerssion();

        log.info("customation:{}", shiroUser);
        logger.info("=======================");
        logger.info(JSON.toJSONString(permessions));

        /**
         * menu:*:* 代表最大权限，其中第一个*到n-1为菜单名称，最后一个*为接口权限，如果是菜单则为空
         * 例如：
         * menu:users:则具有导航到用户管理的权限
         * menus:personCenter:password:则具有导航到用户中心下的个人修改密码功能
         * 具体操作是：获取最后一个冒号的名称即可
         */
        List<MenuInfo> accessedMenuInfos = new ArrayList<>();
        if (CollectionUtils.isEmpty(permessions)) {
            return accessedMenuInfos;
        }

        String containsParam = "menu:*:*";
        if (permessions.contains(containsParam)) {
            return menuArr;
        } else {
            for (String permession : permessions) {
                if (StringUtils.startsWithIgnoreCase(permession, "menu:")) {
                    String[] items = permession.trim().split(":");
                    if (items.length > 1) {
                        //名字
                        String menuName = items[items.length - 1];
                        List<MenuInfo> menuInfos = menuArr.stream().filter(
                                menuInfo -> StringUtils.equalsIgnoreCase(menuInfo.getName(), menuName)
                        ).collect(Collectors.toList());
                        accessedMenuInfos.addAll(menuInfos);
                    }
                }
            }
            return accessedMenuInfos;
        }
//        //SecurityUtils.getSubject().checkPermission("menu:j0ifs3rfa:");
//        //授权可见的菜单才返回，授权的菜单实例以菜单id唯一标识（eg: menu:id:）
//        //先不考虑多级菜单授权的格式，假定父级子级菜单统一按唯一id授权 （如果考虑支持类似 menu:pid:这种授权格式，基础方法需要提供getChildren等的方法实现）
//        Set<String> permessionMenus = new HashSet<>();
//        boolean permessionAll = false;
//        for (String permession : permessions) {
//            if (permession.contains("menu")) {
//                String[] items = permession.trim().split(":");
//                if (items != null && items.length > 0) {
//                    for (int i = 0; i < items.length; i++) {
//                        String item = items[i].trim();
//                        if (item.equals("menu")) {
//                            int j = i+1;
//                            // eg: menu:A: menu:A:a 意义是冲突的(一个要求是A的所有子集菜单，一个要求只是A下的a菜单)，所以增加menus:A:*表达A下所有子菜单，menu:A:含义只是表面要加载下面菜单
//                            if (j == items.length || items[j].equals("*")) {
//                                permessionAll = true;
//                            } else {
//                                while (j < items.length) {
//                                    item = items[j].trim(); // item是菜单Name
//                                    //permessionMenus.add(item);
//                                    if (j == items.length-1 && item.equals("*")) {
//                                        String id = (String)menuMap.get(items[j-1]);
//                                        if (id != null && !id.isEmpty()) {
//                                            List<String> menuIds = MenuManager.getSubMenuIds(menuArr, id);
//                                            permessionMenus.addAll(menuIds);
//                                        }
//                                    } else {
//                                        String id = (String)menuMap.get(item);
//                                        permessionMenus.add(id);
//                                    }
//                                    j++;
//                                }
//                            }
////                            // 严谨的正则表达解析 menu:A:要求是A的所有子集菜单，不能与menu:A:a同时存在
////                            if (j == items.length) {
////                                permessionAll = true;
////                            } else {
////                                while (j < items.length) {
////                                    item = items[j].trim(); // item是菜单Name
////                                    //permessionMenus.add(item);
////                                    String id = (String)menuMap.get(item);
////                                    if (id != null && !id.isEmpty()) {
////                                        if (j == items.length-1) {
////                                            List<String> menuIds = MenuManager.getSubMenuIds(menuArr, id);
////                                            permessionMenus.addAll(menuIds);
////                                        } else {
////                                            permessionMenus.add(id);
////                                        }
////                                    }
////                                    j++;
////                                }
////                            }
//                            break;
//                        }
//                    }
//                }
//                if (permessionAll == true) {
//                    break;
//                }
//            }
//        }
//
//        // 这个逻辑可以优化，上边直接获取菜单添加，而不是获取id
//        List<MenuInfo> menuArrPermession = new ArrayList<>();
//        if (permessionAll) { // menu:
//            menuArrPermession.addAll(menuArr);
//        } else {
//            for (MenuInfo menu : menuArr) {
//                if (permessionMenus.contains(menu.getId())) {
//                    menuArrPermession.add(menu);
//                }
//            }
//        }
//        return menuArrPermession;
    }

    private CustomationInfo getCustomizedPortalConfig(String tenantId) {
        CustomationInfo customation = null;
        if (StringUtils.isBlank(tenantId)) {
            tenantId = CustomationInfo.DEFUALT_TENANT_ID;
        }
        customation = customationDao.queryCustomation(tenantId);
        if (customation == null) {
            customation = customationDao.queryCustomation(CustomationInfo.DEFUALT_TENANT_ID);
        }

        if (customation != null && StringUtils.isBlank(customation.getConfigInfo())) {
            if (StringUtils.isBlank(customation.getDefaultConfigInfo())) {
                CustomationInfo defaultCustomation = customationDao.queryCustomation(CustomationInfo.DEFUALT_TENANT_ID);
                String defaultConfigInfo = defaultCustomation.getConfigInfo() == null ? defaultCustomation.getDefaultConfigInfo() : defaultCustomation.getConfigInfo();

                customation.setDefaultConfigInfo(defaultConfigInfo);
            }

            customation.setConfigInfo(customation.getDefaultConfigInfo());
        }

        return customation;
    }
}

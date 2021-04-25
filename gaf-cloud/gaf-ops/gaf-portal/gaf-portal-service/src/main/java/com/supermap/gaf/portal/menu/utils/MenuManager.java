/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.portal.menu.utils;

import com.supermap.gaf.portal.menu.commontypes.MenuInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
public class MenuManager {

    public static List<String> getSubMenuIds(List<MenuInfo> menus, String parentId) {
        List<String> menuIds = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        stack.push(parentId);
        while (!stack.empty()) {
            String id = (String)stack.pop();
            menuIds.add(id);
            for (MenuInfo menu : menus) {
                if (menu.getPid().equals(id)) {
                    stack.push(menu.getId());
                }
            }
        }
        return menuIds;
    }

    public static Map<String,String> getMenuMapping(List<MenuInfo> menus) {
        Map<String, String> menuMap = new HashMap<String, String>(16);
        for (MenuInfo menu : menus) {
            menuMap.put(menu.getName(), menu.getId());
        }
        return menuMap;
    }

    public static void concatName(List<MenuInfo> menuArr){
        Map<String,MenuInfo> map = new HashMap<String, MenuInfo>(16);
        menuArr.stream().forEach(menu -> map.put(menu.getId(), menu));
        Set<MenuInfo> parents = new HashSet<>();
        List<MenuInfo> lost = new ArrayList<>();
        for (MenuInfo menuInfo : menuArr) {
            if (StringUtils.isEmpty(menuInfo.getPid()) || map.get(menuInfo.getPid()) == null) {
                menuInfo.setName(menuInfo.getName() + ":" + menuInfo.getId());
                parents.add(menuInfo);
            } else {
                lost.add(menuInfo);
            }
        }
        Set<MenuInfo> nextParents = parents;
        List<MenuInfo> nextLost = lost;
        while (!lost.isEmpty()) {
            parents = nextParents;
            lost = nextLost;
            nextParents = new HashSet<>();
            nextLost = new ArrayList<>();
            for (MenuInfo menuInfo : lost) {
                MenuInfo parent = map.get(menuInfo.getPid());
                if (parents.contains(parent)) {
                    menuInfo.setName(menuInfo.getName() + ":" + parent.getName().substring(parent.getName().indexOf(":") + 1) + ":" + menuInfo.getId());
                    nextParents.add(menuInfo);
                } else {
                    nextLost.add(menuInfo);
                }
            }
            //有环
            if (lost.size() == nextLost.size()) {
                break;
            }
            lost = nextLost;
        }
        System.out.println(menuArr);
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.portal.menu.service.impl;

import com.alibaba.fastjson.JSON;
import com.supermap.gaf.portal.menu.commontypes.MenuInfo;
import com.supermap.gaf.portal.menu.dao.MenuDao;
import com.supermap.gaf.portal.menu.service.MenuService;
import com.supermap.gaf.portal.menu.utils.MenuManager;
import com.supermap.gaf.utils.PrimaryKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
@Service
@Qualifier("manager")
public class ManagerMenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;

    @Override
    public String addMenu(MenuInfo menu) {
        // 当前菜单add到父级菜单的children？？菜单都在库里，没有维护在内存
        boolean success = false;
        String msg;
        String id = "";
        Map<String, Object> res = new HashMap<String, Object>(16);
        if (menu != null) {
            menu.setNull2Default();
            if (menu.getId() == null || menu.getId().isEmpty() || menuDao.queryMenuById(menu.getId()) == null) {
                id = PrimaryKeyUtil.getInstance().GetPrimaryKeyValue();
                menu.setId(id);
                success = menuDao.addMenu(menu) > 0;
                if (success) {
                    if ("".equals(menu.getPid())) {
                        menu.setFlag(menu.getId() + ".");
                    } else {
                        menu.setFlag(menu.getFlag() + menu.getId() + ".");
                    }
                    menuDao.updateMenu(menu);
                }
                msg = success ? "添加成功" : "添加失败";
                res.put("backId",id);
            } else {
                success = menuDao.updateMenu(menu) > 0;
                msg = success ? "修改成功" : "修改失败";
            }
        } else {
            msg = "参数错误";
        }
        res.put("success", success);
        res.put("msg", msg);
        return JSON.toJSONString(res);
    }

    @Override
    public String deleteMenu(String menuId) {
        // 删除子级菜单？？ 数据库查询所有子级菜单并删除
        // 删除角色绑定的菜单？？
        boolean success = false;
        String msg;
        if (menuId != null && !menuId.isEmpty()) {
            // 删除子级菜单
            List<MenuInfo> menuArr= menuDao.queryMenus();
            List<String> menuIds = MenuManager.getSubMenuIds(menuArr, menuId);
            success = menuDao.deleteMenuByIds(menuIds) > 0;
            //success = menuDao.deleteMenuById(menuId) > 0;
            msg = success ? "删除成功" : "删除失败";
        } else {
            msg = "参数错误";
        }
        Map<String, Object> res = new HashMap<String, Object>(16);
        res.put("success", success);
        res.put("msg", msg);
        return JSON.toJSONString(res);
    }

    @Override
    public String updateMenu(String menuId, MenuInfo menu) {
        String msg;
        boolean success = false;
        if (menu != null) {
            menu.setNull2Default();
            if (menuId != null && !menuId.isEmpty()) {
                if (menuDao.queryMenuById(menuId) == null) {
                    success = false;
                    msg = "修改目标不存在";
                } else {
                    menu.setId(menuId);
                    success = menuDao.updateMenu(menu) > 0;
                    msg = success ? "修改成功" : "修改失败";
                }
            } else {
                msg = "参数错误";
            }
        } else {

            msg = "参数错误";
        }
        Map<String, Object> res = new HashMap<String, Object>(16);
        res.put("success", success);
        res.put("msg", msg);
        return JSON.toJSONString(res);
    }

    @Override
    public String queryMenu(String menuId) {
        String msg;
        boolean success = false;
        Map<String, Object> res = new HashMap<String, Object>(16);
        if (menuId != null && !menuId.isEmpty()) {
            MenuInfo menu= menuDao.queryMenuById(menuId);
            success = true;
            msg = menu != null ? "查询成功" : "查询不到信息";
            res.put("data", menu);
        } else {
            msg = "参数错误";
        }
        res.put("success", success);
        res.put("msg", msg);
        return JSON.toJSONString(res);
    }

    @Override
    public String queryMenu() {
        String msg;
        boolean success = false;
        Map<String, Object> res = new HashMap<String, Object>(16);
        List<MenuInfo> menuArr= menuDao.queryMenus();
        if (menuArr.size() > 0) {
            success = true;
        }
        msg = menuArr.size() > 0 ? "查询成功" : "查询不到信息";
        res.put("data", menuArr);
        res.put("success", success);
        res.put("msg", msg);
        return JSON.toJSONString(res);
    }

    @Override
    public List<MenuInfo> queryMenuFromAuthority() {
        return null;
    }
    /**
     * 批量导入
     * @param flag
     * @param arr
     * @return
     */
    @Override
    public String importMenus(String flag, List<MenuInfo> arr) {
        Map<String, Object> res = new HashMap<String, Object>(16);
        String msg = "";
        boolean success = false;
        String resString = "";
        try {
            if(!"".equals(flag)){
                //调删除方法
                int i = menuDao.deleteMenuByFlag(flag);
            }
            if (!arr.isEmpty()) {
//                Integer order = 1;
                for (MenuInfo menuInfo : arr) {
                    int i = menuDao.addMenu(menuInfo);
//                    order += 1;
                }
            }
            success = true;
            msg = "关联成功";
        }catch (Exception e){
            success = false;
            msg = "关联失败";
        }finally {
            res.put("success", success);
            res.put("msg", msg);
            resString = JSON.toJSONString(res);
        }
        return resString;
    }
}

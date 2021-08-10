/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.portal.menu.service.impl;

import com.alibaba.fastjson.JSON;
import com.supermap.gaf.portal.menu.commontypes.ThemeInfo;
import com.supermap.gaf.portal.menu.dao.ThemeDao;
import com.supermap.gaf.portal.menu.service.ThemeService;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import com.supermap.gaf.utils.PrimaryKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
@Service
public class ThemeServiceImpl implements ThemeService {
    @Autowired
    private ThemeDao themeDao;

    @Override
    public String saveTheme(ThemeInfo theme) {
        boolean success = false;
        String msg;
        if (theme != null) {
            String user = "";
            ShiroUser shiroUser = SecurityUtilsExt.getUser();
            if (shiroUser != null && !shiroUser.getAuthUser().getName().isEmpty()) {
                user = shiroUser.getAuthUser().getName();
            } else {
                user = "anyone";
            }
            theme.setUser(user);
            theme.setNull2Default();
            if (user == null || user.isEmpty()) {
                msg = "无法获取当前用户信息";
            } else {
                if (themeDao.queryTheme(user) == null) {
                    theme.setId(PrimaryKeyUtil.getInstance().GetPrimaryKeyValue());
                    success = themeDao.addTheme(theme) > 0;
                    msg = success ? "添加成功" : "添加失败";
                } else {
                    success = themeDao.updateTheme(theme) > 0;
                    msg = success ? "修改成功" : "修改失败";
                }
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
    public String queryTheme() {
        String msg;
        boolean success = false;

        // 获取当前用户
        String user = "";
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
        if (shiroUser != null && !shiroUser.getAuthUser().getName().isEmpty()) {
            user = shiroUser.getAuthUser().getName();
        } else {
            user = "anyone";
        }

        Map<String, Object> res = new HashMap<String, Object>(16);
        if (!user.isEmpty()) {
            ThemeInfo theme = themeDao.queryTheme(user);
            success = true;
            msg = theme != null ? "查询成功" : "查询不到信息";
            res.put("data", theme);
        } else {
            msg = "参数错误";
        }
        res.put("success", success);
        res.put("msg", msg);
        return JSON.toJSONString(res);
    }
}

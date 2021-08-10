/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.portal.menu.service.impl;

import com.alibaba.fastjson.JSON;
import com.supermap.gaf.portal.menu.service.UserProfileService;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
@Service
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {

    @Override
    public String queryUserProfile() {
        Map<String, Object> res = new HashMap<String, Object>(16);
        Map<String, Object> user = getUser();
        String userName = user.get("userName").toString();
        res.put("success", StringUtils.equalsIgnoreCase(userName, "") ? false : true);
        res.put("user", user);
        return JSON.toJSONString(res);
    }

    @Override
    public Map<String, Object> getLoginUserInfo() {
        return getUser();
    }

    private Map<String, Object> getUser() {
        Map<String, Object> variables = new HashMap<String, Object>(16);
        ShiroUser shiroUser = SecurityUtilsExt.getUser();

        log.info("user profile:{}", shiroUser);
        List<String> emptyList = new ArrayList<>();
        String userName = shiroUser != null && StringUtils.isNotEmpty(shiroUser.getAuthUser().getName()) ? shiroUser.getAuthUser().getName() : "";

        variables.put("userName", userName);
        variables.put("realName", shiroUser != null && StringUtils.isNotEmpty(shiroUser.getAuthUser().getRealName()) ? shiroUser.getAuthUser().getRealName() : "");
        variables.put("roles", shiroUser != null && CollectionUtils.isNotEmpty(shiroUser.getAuthRoles()) ? shiroUser.getAuthRoles() : emptyList);
        variables.put("sysRoles", shiroUser != null && CollectionUtils.isNotEmpty(shiroUser.getSysRoles()) ? shiroUser.getSysRoles() : emptyList);
        variables.put("groups", shiroUser != null && CollectionUtils.isNotEmpty(shiroUser.getGroups()) ? shiroUser : emptyList);
        variables.put("tenants", emptyList);
        variables.put("currentTenantId", shiroUser != null && StringUtils.isNotEmpty(shiroUser.getTenantId()) ? shiroUser.getTenantId() : "");
        variables.put("token", "");
        return variables;
    }
}

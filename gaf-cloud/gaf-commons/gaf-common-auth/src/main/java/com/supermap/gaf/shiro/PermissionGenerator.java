/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.shiro;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.pac4j.core.authorization.generator.AuthorizationGenerator;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;

import com.alibaba.fastjson.JSON;
import com.supermap.gaf.shiro.commontypes.Authorization;
import com.supermap.gaf.utils.LogUtil;

import net.minidev.json.JSONObject;


/**
 * @author:yj
 * @date:2021/3/25
*/
public class PermissionGenerator<U extends CommonProfile> implements AuthorizationGenerator<U> {

    private static final Logger log = LogUtil.getLocLogger(CustomShiroFilter.class);
    
    public static String AUTHORIZATION_KEY = "authorization";
    public static String PERMISSION_KEY = "permissions";

    @Override
    public U generate(WebContext context, U profile) {
        
        try {
            Object authorizationInfo = profile.getAttribute(AUTHORIZATION_KEY);
            if (authorizationInfo != null && authorizationInfo instanceof JSONObject) {
                Authorization info = JSON.parseObject(authorizationInfo.toString(), Authorization.class);
                if (info != null && CollectionUtils.isNotEmpty(info.getPermissions())) {
                    Set<String> stringPermissions = info.getPermissions().stream().map(item -> item.getResourceName()).collect(Collectors.toSet());
                    profile.setPermissions(stringPermissions);
                }
            }    
        }catch(Exception ex) {
            log.info("授权信息解析异常：" + ex.getMessage(), ex);
        }
        return profile;
    }

}

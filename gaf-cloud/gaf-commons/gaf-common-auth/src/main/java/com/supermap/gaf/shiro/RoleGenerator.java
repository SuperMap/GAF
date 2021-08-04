/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.shiro;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.authorization.generator.AuthorizationGenerator;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.profile.CommonProfile;

import com.supermap.gaf.shiro.config.KeycloakConfig;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class RoleGenerator<U extends CommonProfile> implements AuthorizationGenerator<U> {

    private KeycloakConfig config;

    public static String SYS_ROLES_KEY = "sys-roles";
    public static String CURRENT_SYS_ROLES_KEY = "sysRoles";
    public static String DEFAULT_SYSTEM_CLIENT = "system";

    public RoleGenerator() {
    }

    public KeycloakConfig getConfig() {
        return config;
    }

    public void setConfig(KeycloakConfig config) {
        this.config = config;
    }

    public RoleGenerator(KeycloakConfig config) {
        this.config = config;
    }

    @Override
    public U generate(WebContext context, U profile) {
        if (config == null) {
            return profile;
        }
        Object obj = profile.getAttribute(config.getRoleAttribute());
        String roleStrs = obj == null ? "[]" : obj.toString();
        if (!StringUtils.isEmpty(roleStrs)) {
            String[] roleArr = StringUtils.split(roleStrs, "[ ],\"\"");
            Map<String, String> roleMapper = config.getRoleMapping();
            if (roleMapper == null || roleMapper.size() == 0) {
                profile.addRoles(Arrays.asList(roleArr));
            } else {
                for (String role : roleArr) {
                    if (roleMapper.containsKey(role)) {
                        profile.addRole(roleMapper.get(role));
                    }
                }
            }
        }

        Object sysRoleobj = profile.getAttribute(SYS_ROLES_KEY);
        String sysRoleStrs = sysRoleobj == null ? "[]" : sysRoleobj.toString();
        if (!StringUtils.isEmpty(sysRoleStrs)) {
            String[] roleArr = StringUtils.split(sysRoleStrs, "[ ],\"\"");
            profile.addAttribute(CURRENT_SYS_ROLES_KEY, Arrays.asList(roleArr));
            if (StringUtils.equals(config.getClientId(), DEFAULT_SYSTEM_CLIENT)) {
                profile.addRoles(Arrays.asList(roleArr));
            }
        }
        return profile;
    }

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.shiro;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.pac4j.core.authorization.generator.AuthorizationGenerator;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.profile.CommonProfile;

import com.alibaba.fastjson.JSON;
import com.supermap.gaf.shiro.config.KeycloakConfig;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class GroupGenerator<U extends CommonProfile> implements AuthorizationGenerator<U> {

    private KeycloakConfig config;

    public KeycloakConfig getConfig() {
        return config;
    }

    public void setConfig(KeycloakConfig config) {
        this.config = config;
    }

    public static String GROUP_KEY = "sys-groups";
    public static String CURRENT_GROUP_KEY = "groups";
    public static String GROUP_SEPREATOR = "/";

    public GroupGenerator() {
    }

    public GroupGenerator(KeycloakConfig config) {
        this.config = config;
    }

    @Override
    public U generate(WebContext context, U profile) {
        if (config == null) {
            return profile;
        }
        Object obj = profile.getAttribute(GROUP_KEY);
        List<String> groupArrs = obj == null ? new ArrayList<>() : JSON.parseArray(obj.toString()).toJavaList(String.class);
        if (CollectionUtils.isEmpty(groupArrs)) {
            return profile;
        } else {
            List<String> groups = new ArrayList<>();
            for (String group : groupArrs) {
                String groupStr = group;
                if (groupStr.startsWith("/" + config.getClientId())) {
                    groups.add(groupStr.substring(("/" + config.getClientId()).length()));
                }
            }
            profile.addAttribute(CURRENT_GROUP_KEY, groups);
            return profile;
        }
    }

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.shiro.config;

import java.util.Map;

import org.pac4j.oidc.config.KeycloakOidcConfiguration;

import com.nimbusds.jose.JWSAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author:yj
 * @date:2021/3/25
 */
@ConfigurationProperties(prefix = "shiro.keycloak")
public class KeycloakConfig extends KeycloakOidcConfiguration {

    /**
     * 开启shiro
     */
    private boolean enable = true;
    /**
     * 角色属性
     */
    private String roleAttribute = "roles";

    /**
     * 角色映射
     */
    private Map<String, String> roleMapping;

    /**
     * 部门属性
     */
    private String groupAttributes = "groups";

    /**
     * 部门映射
     */
    private Map<String, String> groupMapping;


    private String tokenHeader;

    private String tokenPrefix;

    public KeycloakConfig() {
        this.setPreferredJwsAlgorithm(JWSAlgorithm.RS256);
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getRoleAttribute() {
        return roleAttribute;
    }

    public void setRoleAttribute(String roleAttribute) {
        this.roleAttribute = roleAttribute;
    }

    public Map<String, String> getRoleMapping() {
        return roleMapping;
    }

    public void setRoleMapping(Map<String, String> roleMapping) {
        this.roleMapping = roleMapping;
    }

    public String getGroupAttributes() {
        return groupAttributes;
    }

    public void setGroupAttributes(String groupAttributes) {
        this.groupAttributes = groupAttributes;
    }

    public Map<String, String> getGroupMapping() {
        return groupMapping;
    }

    public void setGroupMapping(Map<String, String> groupMapping) {
        this.groupMapping = groupMapping;
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    public void setTokenHeader(String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

}

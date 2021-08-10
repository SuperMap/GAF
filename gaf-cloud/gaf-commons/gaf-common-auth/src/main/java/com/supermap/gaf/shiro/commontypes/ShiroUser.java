/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.shiro.commontypes;

import com.supermap.gaf.authority.commontype.AuthResourceApi;
import com.supermap.gaf.authority.commontype.AuthResourceModule;
import com.supermap.gaf.authority.commontype.AuthRole;
import com.supermap.gaf.authority.commontype.AuthUser;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author ${Author}
 * @version ${Version}
 * @date:2021/3/25
 * @since 1.0.0
 */
public class ShiroUser extends com.supermap.gaf.commontypes.ShiroUser implements Serializable {
    /**
     * <p>
     *
     * </p>
     *
     * @since 1.0.0
     */
    private static final long serialVersionUID = -1707902121655207540L;

    /**
     * 用户id
     */
    private String id;

    /**
     * 用户名，用户唯一标志
     */
    private String username;

    /**
     * 用户中文名，昵称。用于更友好的展示
     */
    private String realName;

    /**
     * 用户密码，只针对配置用户有效
     */
    private String password;

    /**
     * 用户角色
     */
    private List<String> roles;

    /**
     * 用户系统角色
     */
    private List<String> sysRoles;

    /**
     * 用户权限列表
     */
    private List<String> permerssions;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户分组、部门情况
     */
    private List<String> groups;

    /**
     * 其他扩展属性
     */
    private Map<String, String> extendAtts;

    /**
     * 租户唯一标识id
     */
    private String tenantId;

    /**
     * 用户信息
     */
    private AuthUser authUser;
    /**
     * 用户api权限
     */
    private List<AuthResourceApi> authResourceApis;
    /**
     * 用户模块列表
     */
    private List<AuthResourceModule> authResourceModules;
    /**
     * 用户角色列表
     */
    private List<AuthRole> authRoles;

    public AuthUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }

    public List<AuthResourceApi> getAuthResourceApis() {
        return authResourceApis;
    }

    public void setAuthResourceApis(List<AuthResourceApi> authResourceApis) {
        this.authResourceApis = authResourceApis;
    }

    public List<AuthResourceModule> getAuthResourceModules() {
        return authResourceModules;
    }

    public void setAuthResourceModules(List<AuthResourceModule> authResourceModules) {
        this.authResourceModules = authResourceModules;
    }

    public List<AuthRole> getAuthRoles() {
        return authRoles;
    }

    public void setAuthRoles(List<AuthRole> authRoles) {
        this.authRoles = authRoles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    @Deprecated
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    @Deprecated
    public List<String> getRoles() {
        return roles;
    }

    @Override
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Deprecated
    public List<String> getPermerssions() {
        return permerssions;
    }

    public void setPermerssions(List<String> permerssion) {
        this.permerssions = permerssion;
    }

    @Override
    @Deprecated
    public List<String> getGroups() {
        return groups;
    }

    @Override
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    @Override
    @Deprecated
    public Map<String, String> getExtendAtts() {
        return extendAtts;
    }

    @Override
    public void setExtendAtts(Map<String, String> extendAtts) {
        this.extendAtts = extendAtts;
    }

    @Override
    @Deprecated
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    @Deprecated
    public List<String> getSysRoles() {
        return sysRoles;
    }

    @Override
    public void setSysRoles(List<String> sysRoles) {
        this.sysRoles = sysRoles;
    }

    @Override
    @Deprecated
    public String getRealName() {
        return realName;
    }

    @Override
    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.commontypes;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户 
 * </p>
 * @author ${Author}
 * @version ${Version}
 * @since 1.0.0
 * @date:2021/3/25
 *
 */
public class ShiroUser implements Serializable {
    /**
     * <p>
     * 
     * </p>
     * @since 1.0.0
     */
    private static final long serialVersionUID = -1707902121655207540L;

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
	private List<String> permerssion;
	
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
	private Map<String,String> extendAtts;

	/**
	 * 租户唯一标识id
	 */
	private String tenantId;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public List<String> getPermerssion() {
		return permerssion;
	}
	public void setPermerssion(List<String> permerssion) {
		this.permerssion = permerssion;
	}
    public List<String> getGroups() {
        return groups;
    }
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
    public Map<String,String> getExtendAtts() {
        return extendAtts;
    }
    public void setExtendAtts(Map<String,String> extendAtts) {
        this.extendAtts = extendAtts;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<String> getSysRoles() {
        return sysRoles;
    }
    public void setSysRoles(List<String> sysRoles) {
        this.sysRoles = sysRoles;
    }
    public String getRealName() {
        return realName;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}

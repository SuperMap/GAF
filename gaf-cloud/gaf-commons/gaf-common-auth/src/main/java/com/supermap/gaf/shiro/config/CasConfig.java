/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.shiro.config;

import org.pac4j.cas.config.CasConfiguration;


/**
 * @author:yj
 * @date:2021/3/25
*/
public class CasConfig extends CasConfiguration{
    
    /**
     * 是否启用配置
     */
    private boolean enable = false;
    
    /**
     * cas服务地址
     */
    private String server;
    
    /**
     * cas登录成功后，客户端app地址
     */
    private String serviceUrl;
    
    /**
     * pa4j客户端标记，可随意定制
     */
    private String clientName;
    
    /**
     * 针对不存在用户，是否同步增加到用户授权中心
     */
    private boolean addUser = false;
    
    /**
     * 开启默认授权逻辑
     */
    private boolean authzEnable = true;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public boolean isAddUser() {
        return addUser;
    }
    
    public void setAddUser(boolean isAddUser) {
        this.addUser = isAddUser;
    }

    public boolean isAuthzEnable() {
        return authzEnable;
    }

    public void setAuthzEnable(boolean authzEnable) {
        this.authzEnable = authzEnable;
    }
}

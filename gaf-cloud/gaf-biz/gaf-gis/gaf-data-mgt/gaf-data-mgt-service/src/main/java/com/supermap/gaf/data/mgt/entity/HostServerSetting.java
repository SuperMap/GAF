/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @program: gaf-datacenter-modules
 * @description: iserver设置类
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/05/21
 */

@Configuration
@ConfigurationProperties(prefix = "hostservers.hostserver")
@ApiModel("iserver设置类")
public class HostServerSetting implements Serializable {
    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private boolean enable;
    /**
     * 主机ID,用于标识不同iserver服务器
     */
    @ApiModelProperty("主机ID")
    private String hostId;
    /**
     * iserver登录账号
     */
    @ApiModelProperty("iserver登录账号")
    private String username;
    /**
     * iserver登录密码
     */
    @ApiModelProperty("iserver登录密码")
    private String password;
    /**
     * iserver基础地址
     */
    @ApiModelProperty("iserver基础地址")
    private String hostServerUrl;

    /**
     * iserver域名地址
     */
    @ApiModelProperty("iserver域名地址")
    private String domainUrl;

    /**
     * iserver服务类型
     */
    @ApiModelProperty("iserver服务类型")
    private Type type;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

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

    public String getHostServerUrl() {
        return hostServerUrl;
    }

    public void setHostServerUrl(String hostServerUrl) {
        this.hostServerUrl = hostServerUrl;
    }

    public String getDomainUrl() {
        if (StringUtils.isEmpty(domainUrl)) {
            domainUrl = hostServerUrl;
        }
        return domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public enum Type {
        /**
         * Iserver微服务
         */
        MICROSERVICE,
        /**
         * 普通IServer服务
         */
        GENERALSERVICE
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
}

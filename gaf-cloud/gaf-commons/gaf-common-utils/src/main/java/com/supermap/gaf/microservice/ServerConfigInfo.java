/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.microservice;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
 * @version 1.0 2019-8-9
 * @date:2021/3/25
 * @description 配置信息对象
 */
public class ServerConfigInfo implements Serializable {


    /**
     * <p>
     *
     * </p>
     *
     * @since 1.0.0
     */
    private static final long serialVersionUID = -8442035178734834682L;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 应用名称
     */
    private String application;
    /**
     * 环境
     */
    private String profile;
    /**
     * 分支
     */
    private String label;

    /**
     * 配置键值对
     */
    private List<ConfigurationInfo> properties;


    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public List<ConfigurationInfo> getProperties() {
        return properties;
    }

    public void setProperties(List<ConfigurationInfo> properties) {
        this.properties = properties;
    }

    public ServerConfigInfo() {
    }

    public ServerConfigInfo(String tenantId, String application, String profile, String label, List<ConfigurationInfo> properties) {
        this.tenantId = tenantId;
        this.application = application;
        this.profile = profile;
        this.label = label;
        this.properties = properties;
    }
}

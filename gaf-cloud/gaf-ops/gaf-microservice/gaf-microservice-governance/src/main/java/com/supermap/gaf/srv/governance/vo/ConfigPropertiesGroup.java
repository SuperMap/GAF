/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.srv.governance.vo;

import java.io.Serializable;

/**
 * @author wxl
 * @date 2021/1/4
 */
public class ConfigPropertiesGroup implements Serializable {
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
     * 租户id
     */
    private String tenantId;

    /**
     *
     */
    private Integer propertiesCount;

    public ConfigPropertiesGroup() {
    }

    public ConfigPropertiesGroup(String application, String profile, String label, String tenantId, Integer propertiesCount) {
        this.application = application;
        this.profile = profile;
        this.label = label;
        this.tenantId = tenantId;
        this.propertiesCount = propertiesCount;
    }

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

    public Integer getPropertiesCount() {
        return propertiesCount;
    }

    public void setPropertiesCount(Integer propertiesCount) {
        this.propertiesCount = propertiesCount;
    }
}

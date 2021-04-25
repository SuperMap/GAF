/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.configmgt.commontypes;

import java.io.Serializable;

/**
 * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
 * @version 1.0 2019-8-9
 * @date:2021/3/25
 * @description 配置对象
 */
public class ConfigProperties implements Serializable{

    /**
     *  应用名称
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
    * 主键
    */
    private String id;

    /**
    * 租户id
    */
    private String tenantId;

    /**
    * 配置键
    */
    private String propertyKey;

    /**
    * 配置值
    */
    private String propertyValue;


    /**
     * 描述
     */
    private String description;

    public String getApplication() {
        return application;
    }

    public String getLabel() {
        return label;
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

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

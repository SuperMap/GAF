/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.commontypes;

import org.apache.commons.lang3.StringUtils;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class ServiceBean {
    private String id;
    private String name;
    private String tenantId;
    private String deploymentStatus;
    private String parentName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getDeploymentStatus() {
        return deploymentStatus;
    }

    public void setDeploymentStatus(String deploymentStatus) {
        this.deploymentStatus = deploymentStatus;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public boolean isNotEmpty() {
        return StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(parentName);
    }

    public boolean equals(ServiceBean bean) {
        return name.equals(bean.name) && parentName.equals(bean.parentName);
    }

}

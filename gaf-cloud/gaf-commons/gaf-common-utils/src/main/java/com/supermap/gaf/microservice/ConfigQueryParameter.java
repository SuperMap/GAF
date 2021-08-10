/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.microservice;

import com.supermap.gaf.commontypes.SearchParameter;

/**
 * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
 * @version 1.0 2019-8-9
 * @date:2021/3/25
 * @description
 */
public class ConfigQueryParameter extends SearchParameter {

    /**
     * <p>
     *
     * </p>
     *
     * @since 1.0.0
     */
    private static final long serialVersionUID = 850419391874875369L;

    private String application;

    private String profilesActive;

    private String label;

    private String tenantId;

    public String getApplication() {
        return application;
    }

    public String getProfilesActive() {
        return profilesActive;
    }

    public String getLabel() {
        return label;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setProfilesActive(String profilesActive) {
        this.profilesActive = profilesActive;
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

    public ConfigQueryParameter() {
    }

    public ConfigQueryParameter(String application, String profilesActive, String label, String tenantId) {
        this.application = application;
        this.profilesActive = profilesActive;
        this.label = label;
        this.tenantId = tenantId;
    }
}

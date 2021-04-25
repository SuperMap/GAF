/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.configmgt.commontypes;

import com.supermap.gaf.configmgt.vo.ConfigPropertiesVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @date:2021/3/25
 * @author dqc
 */
@ApiModel("服务配置属性分组及属性")
public class GroupWithProperties implements Serializable {

    /**
     *  应用名称
     */
    @ApiModelProperty(value = "服务名",example = "GAF-XX")
    private String application;
    /**
     * 环境
     */
    @ApiModelProperty(value = "环境",example = "prod")
    private String profile;
    /**
     * 分支
     */
    @ApiModelProperty(value = "分支",example = "master")
    private String label;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id",example = "ABC")
    private String tenantId;

    /**
     * 配置属性
     */
    @ApiModelProperty( "所有配置属性")
    private List<ConfigPropertiesVo> configPropertiesVos;


    public GroupWithProperties() {
    }

    public GroupWithProperties(String application, String profile, String label, String tenantId, List<ConfigPropertiesVo> configPropertiesVos) {
        this.application = application;
        this.profile = profile;
        this.label = label;
        this.tenantId = tenantId;
        this.configPropertiesVos = configPropertiesVos;
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

    public List<ConfigPropertiesVo> getConfigPropertiesVos() {
        return configPropertiesVos;
    }

    public void setConfigPropertiesVos(List<ConfigPropertiesVo> configPropertiesVos) {
        this.configPropertiesVos = configPropertiesVos;
    }
}

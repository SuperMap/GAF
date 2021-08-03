/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.configmgt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author dqc
 * @date:2021/3/25
 */
@ApiModel("配置属性vo")
public class ConfigPropertiesVo implements Serializable {
    @ApiModelProperty(value = "配置属性id", example = "ABC")
    private String id;
    @ApiModelProperty(value = "配置属性键名", example = "key")
    private String propertyKey;
    @ApiModelProperty(value = "配置属性值名", example = "value")
    private String propertyValue;
    @ApiModelProperty(value = "配置属性描述", example = "示例配置")
    private String description;

    public ConfigPropertiesVo() {
    }

    public ConfigPropertiesVo(String id, String propertyKey, String propertyValue, String description) {
        this.id = id;
        this.propertyKey = propertyKey;
        this.propertyValue = propertyValue;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

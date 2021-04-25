/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.microservice;

import java.util.Objects;

/**
 * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
 * @version 1.0 2019-8-14
 * @date:2021/3/25
 * @description
 */
public class ConfigurationInfo {
   private String id;
   private String propertyKey;

   private String propertyValue;

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

    public ConfigurationInfo() {
    }

    public ConfigurationInfo(String id, String propertyKey, String propertyValue) {
        this.id = id;
        this.propertyKey = propertyKey;
        this.propertyValue = propertyValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigurationInfo)) {
            return false;
        }
        ConfigurationInfo that = (ConfigurationInfo) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getPropertyKey(), that.getPropertyKey()) && Objects
                .equals(getPropertyValue(), that.getPropertyValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPropertyKey(), getPropertyValue());
    }
}

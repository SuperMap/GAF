/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.license.autoconfigure;

import org.springframework.boot.context.properties.NestedConfigurationProperty;


/**
 * @author:yj
 * @date:2021/3/25
*/

//@ConfigurationProperties(prefix = "gaf-license")
public class LicenseProperties {
    /**
     * 直接放行的Path
     */
    private String[] publicPaths = new String[0];

    @NestedConfigurationProperty
    private LicenseItem[] licenseItemArray = new LicenseItem[0];

    /**
     * 缓存刷新时间 单位秒
     */
    private long period = 7200;

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public String[] getPublicPaths() {
        return publicPaths;
    }

    public void setPublicPaths(String[] publicPaths) {
        this.publicPaths = publicPaths;
    }

    public LicenseItem[] getLicenseItemArray() {
        return licenseItemArray;
    }

    public void setLicenseItemArray(LicenseItem[] licenseItemArray) {
        this.licenseItemArray = licenseItemArray;
    }
}

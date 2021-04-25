/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.license.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 使用默认配置，若没有配置spring.jersey.application-path则全部请求都拦截
 * @author:yj
 * @date:2021/3/25
 * 默认的许可模块id是gaf-pro的专业版id 17620
 */
@ConditionalOnMissingBean(LicenseProperties.class)
@Configuration
public class LicenseDefaultConfig {

    @Value("${spring.jersey.application-path:}")
    private String applicationPath;

    /**
     * 单位 秒
     */
    @Value("${gaf.license.cache-flush-time:7200}")
    private long cacheFlushTime;
    @Bean
    public LicenseProperties licenseProperties() {
        LicenseProperties licenseProperties = new LicenseProperties();
        if(cacheFlushTime > 1 && cacheFlushTime < 7200) {
            licenseProperties.setPeriod(cacheFlushTime);
        }
        String[] publicPaths = {applicationPath + "/view/**",applicationPath + "/swagger**"};
        licenseProperties.setPublicPaths(publicPaths);
        LicenseItem[] licenseItemArray = new LicenseItem[1];
        LicenseItem licenseItem = new LicenseItem();
        // 授权id字符串转int  字符串混淆效果好
        licenseItem.setFeatureId(Integer.parseInt("17620"));
        licenseItem.setPaths(new String[]{applicationPath + "/**"});
        licenseItemArray[0] = licenseItem;
        licenseProperties.setLicenseItemArray(licenseItemArray);
        return licenseProperties;
    }

}

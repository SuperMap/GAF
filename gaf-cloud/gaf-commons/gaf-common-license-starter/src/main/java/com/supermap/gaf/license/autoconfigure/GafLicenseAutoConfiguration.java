/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.license.autoconfigure;

import com.supermap.gaf.license.core.LicenseManager;
import com.supermap.gaf.license.filter.LicenseFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author:yj
 * @date:2021/3/25
*/
@Configuration
//@ConditionalOnProperty(name = "gaf-license.enable", havingValue = "true", matchIfMissing = true)
//@EnableConfigurationProperties(LicenseProperties.class)
public class GafLicenseAutoConfiguration {

    private final LicenseProperties properties;

    public GafLicenseAutoConfiguration(LicenseProperties properties) {
        this.properties = properties;
    }

    @Bean
    public FilterRegistrationBean<LicenseFilter> licenseFilterRegistrationBean(){
        FilterRegistrationBean<LicenseFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new LicenseFilter(new LicenseManager(properties)));
        bean.addUrlPatterns("/*");
        bean.setOrder(5);
        bean.setName("licenseFilter");
        return bean;
    }

}

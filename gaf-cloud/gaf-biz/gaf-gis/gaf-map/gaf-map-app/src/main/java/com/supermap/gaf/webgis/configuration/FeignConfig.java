/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.configuration;

import com.supermap.gaf.shiro.spring.JWTTokenClientFeignInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author:yj
 * @date:2021/3/25
*/
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor headerInterceptor() {
        return new JWTTokenClientFeignInterceptor();
    }
}

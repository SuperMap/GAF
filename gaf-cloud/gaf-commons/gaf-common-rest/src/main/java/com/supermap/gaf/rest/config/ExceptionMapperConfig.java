/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.rest.config;

import com.supermap.gaf.rest.exceptions.handler.*;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author:yj
 * @date:2021/3/25
 */

@Configuration
@ConditionalOnBean(ResourceConfig.class)
@ConditionalOnProperty(name = "gaf.jersey.exception.enable", havingValue = "true", matchIfMissing = true)
public class ExceptionMapperConfig {

    @Bean
    public ExceptionMapperConfig configExceptionMapper(@Autowired ResourceConfig resouceConfig) {
        resouceConfig.register(IllegalArgumentExceptionMapper.class);
        resouceConfig.register(IllegalStateExceptionMapper.class);
        resouceConfig.register(ScNotModifiedExceptionMapper.class);
        resouceConfig.register(JaxrsHttpExceptionMapper.class);
        resouceConfig.register(WebApplicationExceptionMapper.class);
        resouceConfig.register(ShiroExceptionMapper.class);
        return new ExceptionMapperConfig();
    }


}

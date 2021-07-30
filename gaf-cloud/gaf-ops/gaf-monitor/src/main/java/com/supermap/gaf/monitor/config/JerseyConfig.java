/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.monitor.config;

import com.supermap.gaf.monitor.resources.root.RootResources;
import com.supermap.gaf.rest.config.CorsFilter;
import io.swagger.jaxrs.config.BeanConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @date:2021/3/25
 * @author dqc
 */
@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(RootResources.class);
        //跨域过滤器
        register(CorsFilter.class);
    }

    @PostConstruct
    public void initSwagger() {
        initBeanConfig();
    }

    public BeanConfig initBeanConfig() {
        BeanConfig config = new BeanConfig();
        config.setTitle("监控组件API");
        config.setVersion("v1");
        config.setContact("supermap gaf");
        config.setSchemes(new String[] { "http", "https" });
        config.setResourcePackage("com.supermap.gaf.monitor.resources.root");
        config.setPrettyPrint(true);
        config.setScan(true);
        return config;
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.portal.configuration;

import javax.annotation.PostConstruct;

import com.supermap.gaf.rest.config.CorsFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.support.jaxrs.FastJsonProvider;
import com.supermap.gaf.rest.jersey.JaxrsStaticResource;
import com.supermap.gaf.portal.resources.root.PortalRootResoure;

import io.swagger.jaxrs.config.BeanConfig;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
@Component
public class JerseyConfig extends ResourceConfig {

    @Value("${swagger.enable:false}")
    private boolean swaggerEnable;

    public JerseyConfig() {
        register(PortalRootResoure.class);
        register(FastJsonProvider.class);
        register(JaxrsStaticResource.class);
        //跨域过滤器
        register(CorsFilter.class);
    }
    
    @PostConstruct
    public void initSwagger() {
        if (swaggerEnable) {
            initBeanConfig();
        }
    }
    
    public BeanConfig initBeanConfig() {
        BeanConfig config = new BeanConfig();
        config.setTitle("API");
        config.setVersion("v1");
        config.setContact("supermap gaf");
        config.setResourcePackage("com.supermap.gaf.portal.resources.root");
        config.setPrettyPrint(true);
        config.setScan(true);
        return config;
    }
}

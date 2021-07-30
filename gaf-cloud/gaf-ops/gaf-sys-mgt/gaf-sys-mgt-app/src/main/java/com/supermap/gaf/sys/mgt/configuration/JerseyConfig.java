/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.configuration;

import com.supermap.gaf.exception.GafExceptionMapper;
import com.supermap.gaf.exception.ValidExceptionMapper;
import com.supermap.gaf.rest.config.CorsFilter;
import com.supermap.gaf.sys.mgt.resource.root.RootResource;
import io.swagger.jaxrs.config.BeanConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @date:2021/3/25
 * @author wxl
 */
@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(RootResource.class);
        register(ValidExceptionMapper.class);
        register(GafExceptionMapper.class);
        //跨域过滤器
        register(CorsFilter.class);
    }


    @PostConstruct
    public void initSwagger() {
        initBeanConfig();
    }

    public void initBeanConfig() {
        BeanConfig config = new BeanConfig();
        config.setTitle("系统管理API");
        config.setVersion("v1");
        config.setContact("supermap gaf");
        config.setResourcePackage("com.supermap.gaf.sys.mgt.resource.root");
        config.setPrettyPrint(true);
        config.setScan(true);
    }
}

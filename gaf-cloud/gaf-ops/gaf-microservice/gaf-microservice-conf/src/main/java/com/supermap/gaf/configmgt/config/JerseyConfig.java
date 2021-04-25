/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.configmgt.config;

import com.supermap.gaf.configmgt.resources.root.ConfMgtRootResoure;
import io.swagger.jaxrs.config.BeanConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
 * @version 1.0 2019-8-12
 * @date:2021/3/25
 * @description
 */
@Configuration
public class JerseyConfig extends ResourceConfig {


    public JerseyConfig() {
        register(ConfMgtRootResoure.class);
    }

    @PostConstruct
    public void initSwagger() {
        initBeanConfig();
    }

    public BeanConfig initBeanConfig() {
        BeanConfig config = new BeanConfig();
        config.setTitle("服务管理API");
        config.setVersion("v1");
        config.setContact("supermap gaf");
        config.setSchemes(new String[] { "http", "https" });
        config.setResourcePackage("com.supermap.gaf.configmgt.resources.root");
        config.setPrettyPrint(true);
        config.setScan(true);
        return config;
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.api.config;

import com.supermap.gaf.rest.config.CorsFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.support.jaxrs.FastJsonProvider;
import com.supermap.gaf.api.resources.ApiRootResoure;
import com.supermap.gaf.rest.jersey.JaxrsStaticResource;

/**
 * @date:2021/3/25
 * @author dqc
 */
@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(ApiRootResoure.class);
        register(FastJsonProvider.class);
        register(JaxrsStaticResource.class);
        //跨域过滤器
        register(CorsFilter.class);
    }
}

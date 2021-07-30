/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.configuration;


import com.supermap.gaf.exception.GafExceptionMapper;
import com.supermap.gaf.exception.ValidExceptionMapper;
import com.supermap.gaf.rest.config.CorsFilter;
import com.supermap.gaf.webgis.resource.root.WebgisRootResource;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Swagger;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.In;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletConfigAware;

import javax.annotation.PostConstruct;
import javax.servlet.ServletConfig;
import java.util.Arrays;


/**
 * @author:yj
 * @date:2021/3/25
*/
@Component
public class JerseyConfig extends ResourceConfig implements ServletConfigAware {
    private ServletConfig servletConfig;
    public JerseyConfig() {
        register(WebgisRootResource.class);
        register(GafExceptionMapper.class);
        register(ValidExceptionMapper.class);
        //跨域过滤器
        register(CorsFilter.class);
    }
    /**
            * 配置swagger
     */
    @PostConstruct
    public void configureSwagger() {
        register(ApiListingResource.class);
        register(SwaggerSerializers.class);
        BeanConfig config = new BeanConfig();
        config.setResourcePackage("com.supermap.gaf.webgis.resource");
        config.setPrettyPrint(true);
        config.setBasePath("/map");
        config.setScan(true);
        Swagger swagger = new Swagger();
        swagger.securityDefinition("basicAuth", new BasicAuthDefinition());
        swagger.securityDefinition("token", new ApiKeyAuthDefinition("Authorization", In.HEADER));
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.requirement("token");
        SecurityRequirement securityRequirement2 = new SecurityRequirement();
        securityRequirement.requirement("basicAuth");
        swagger.setSecurity(Arrays.asList(securityRequirement,securityRequirement2));
        new SwaggerContextService().withServletConfig(servletConfig).updateSwagger(swagger);
    }

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }
}

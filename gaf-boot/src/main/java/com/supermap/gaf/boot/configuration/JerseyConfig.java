package com.supermap.gaf.boot.configuration;


import com.supermap.gaf.boot.resources.root.RootResource;
import com.supermap.gaf.exception.GafExceptionMapper;
import com.supermap.gaf.exception.ValidExceptionMapper;
import io.swagger.jaxrs.config.BeanConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(RootResource.class);
        register(GafExceptionMapper.class);
        register(ValidExceptionMapper.class);
    }

    @PostConstruct
    public void initSwagger() {
        initBeanConfig();
    }

    public BeanConfig initBeanConfig() {
        BeanConfig config = new BeanConfig();
        config.setTitle("gaf-microservice-api");
        config.setVersion("v1");
        config.setContact("supermap gaf");
        config.setResourcePackage("com.supermap.gaf.boot.resources.root");
        config.setPrettyPrint(true);
        config.setScan(true);
        return config;
    }

}
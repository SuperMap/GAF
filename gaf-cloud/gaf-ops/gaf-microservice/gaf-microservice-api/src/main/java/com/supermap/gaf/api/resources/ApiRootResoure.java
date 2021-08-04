/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.api.resources;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.supermap.gaf.rest.jersey.JaxrsStaticViewResource;

/**
 * @author dqc
 * @date:2021/3/25
 */
@Component
@Path("/")
public class ApiRootResoure {

    @Path("/api")
    public Class<JaxrsStaticViewResource> staticViewResoruce() {
        return JaxrsStaticViewResource.class;
    }

    @Path("/swagger")
    public Class<SwaggerApiResource> swaggerApiResourceClass() {
        return SwaggerApiResource.class;
    }
}

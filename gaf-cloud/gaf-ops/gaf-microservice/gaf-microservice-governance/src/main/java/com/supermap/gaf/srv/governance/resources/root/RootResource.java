/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.srv.governance.resources.root;

import com.supermap.gaf.srv.governance.resources.*;
import io.swagger.annotations.Api;

import javax.ws.rs.Path;

/**
 * @date:2021/3/25
 * @author dqc
 */
@Path("/")
@Api("微服务治理接口")
public class RootResource {



    @Path("/log")
    public Class<LogQueryResource> logQueryResourceClass() {
        return LogQueryResource.class;
    }

    @Path("/trace")
    public Class<TraceQueryResource> traceQueryResourceClass() {
        return TraceQueryResource.class;
    }

    @Path("/routes")
    public Class<GatewayRouteResource> gatewayRouteResourceClass() {
        return GatewayRouteResource.class;
    }

    @Path("/config")
    public Class<ConfigServerConfigResource> configServerConfigResourceClass() {
        return ConfigServerConfigResource.class;
    }

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.monitor.resources.root;

import com.supermap.gaf.monitor.resources.WebhookResources;
import com.supermap.gaf.rest.jersey.JaxrsStaticViewResource;
import io.swagger.annotations.Api;

import javax.ws.rs.Path;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/12/22 9:49 AM
 */
@Path("/")
@Api("监控服务接口")
public class RootResources {


    @Path("/webhook")
    public Class<WebhookResources> webhookResourcesClass() {
        return WebhookResources.class;
    }

}

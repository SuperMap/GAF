/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.analysis.resource.spatial;


import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;


/**
 * @author dqc
 * 分析服务根结点
 * @date:2021/3/25 /analysis/spatial
 */
@Path("/")
@Api(value = "空间分析接口")
@Component
public class SpatialRootResource {


    @Path("/overlay")
    public Class<OverlayResource> overlayResourceClass() {
        return OverlayResource.class;
    }

    @Path("/overlay-iserver")
    public Class<OverlayIserverResource> overlayTestResourceClass() {
        return OverlayIserverResource.class;
    }

    @Path("/overlay-iobject")
    public Class<OverlayIobjectResource> overlayIobjectResourceClass() {
        return OverlayIobjectResource.class;
    }

    @Path("/overlay-geoprocessing")
    public Class<OverlayGeoprocessingResource> overlayGeoprocessingResourceClass() {
        return OverlayGeoprocessingResource.class;
    }

    @Path("/area")
    public Class<AreaResource> areaResourceClass() {
        return AreaResource.class;
    }


}

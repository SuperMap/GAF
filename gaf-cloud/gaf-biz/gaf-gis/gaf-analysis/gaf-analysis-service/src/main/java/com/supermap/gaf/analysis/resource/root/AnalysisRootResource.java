/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.analysis.resource.root;


import com.supermap.gaf.analysis.resource.spatial.SpatialRootResource;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;


/**
 * @author dqc
 * 分析服务根结点
 * @date:2021/3/25 /analysis
 */
@Path("/")
@Api(value = "专题分析接口")
@Component
public class AnalysisRootResource {


    @Path("/spatial")
    public Class<SpatialRootResource> spatialRootResourceClass() {
        return SpatialRootResource.class;
    }


}

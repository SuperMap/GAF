/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.graph.resource.root;


import com.supermap.gaf.data.graph.resource.DataGraphImportResource;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;


/**
 * @author dqc
 * @date:2021/5/6
 * /data-graph
 */
@Path("/")
@Api(value = "数据图谱接口")
@Component
public class DataGraphRootResource {


    @Path("/import")
    public Class<DataGraphImportResource> dataGraphImportResourceClass() {
        return DataGraphImportResource.class;
    }


}

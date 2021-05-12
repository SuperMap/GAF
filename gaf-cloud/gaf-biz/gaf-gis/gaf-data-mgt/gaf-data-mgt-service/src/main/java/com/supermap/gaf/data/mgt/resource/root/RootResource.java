/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.resource.root;

import com.supermap.gaf.data.mgt.resource.*;
import com.supermap.gaf.rest.jersey.JaxrsStaticViewResource;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

//import com.supermap.gaf.storage.resources.MinioServiceResource;

/**
 * @date:2021/3/25
 * @author heykb
 */

@Api("数据管理")
@Path("/")
@Component("dataMgtRootResource")
public class RootResource {

    @Path("/view")
    public Class<JaxrsStaticViewResource> jaxrsStaticViewResourceClass() {
        return JaxrsStaticViewResource.class;
    }

    @Path("/data-workspaces")
    public Class<DataWorkspaceResource> dataWorkspaceResource() {
        return DataWorkspaceResource.class;
    }

    @Path("/iserver")
    public Class<IServerResource> iServerResource() {
        return IServerResource.class;
    }

    @Path("/datasource")
    public Class<DataSourceResource> dataSourceResource() {
        return DataSourceResource.class;
    }

    @Path("/services")
    public Class<ServiceResource> serviceResource() {
        return ServiceResource.class;
    }

    @Path("/data-datasets")
    public Class<DataDatasetResource> dataDatasetResource(){
        return  DataDatasetResource.class;
    }


//    @Path("/minio-service")
//    public Class<MinioServiceResource> minioServiceResource() {
//        return MinioServiceResource.class;
//    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.storage.resource.root;

//import com.supermap.gaf.storage.resources.MinioServiceResource;
import com.supermap.gaf.storage.resource.FileStorageResource;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

/**
 * @date:2021/3/25
 * @author heykb
 */

@Api("存储管理")
@Path("/")
@Component("storageRootResource")
public class RootResource {

//    @Path("/minio-service")
//    public Class<MinioServiceResource> minioServiceResource() {
//        return MinioServiceResource.class;
//    }

    @Path("/file-storage")
    public Class<FileStorageResource> fileStorageResource() {
        return FileStorageResource.class;
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.sys.mgt.resource.root;

import com.supermap.gaf.sys.mgt.resource.SysCatalogResource;
import com.supermap.gaf.sys.mgt.resource.SysDictResource;
import com.supermap.gaf.sys.mgt.resource.SysResourceDatasourceResource;
import io.swagger.annotations.Api;

import javax.ws.rs.Path;

/**
 * @author wxl
 * @date:2021/3/25
 */
@Path("/")
@Api("系统与权限控制")
public class RootResource {

    @Path("/sys-catalogs")
    public Class<SysCatalogResource> sysCatalogResource() {
        return SysCatalogResource.class;
    }

    @Path("/sys-dicts")
    public Class<SysDictResource> sysDictResource() {
        return SysDictResource.class;
    }

    @Path("/sys-resource-datasources")
    public Class<SysResourceDatasourceResource> sysResourceDatasourceResource() {
        return SysResourceDatasourceResource.class;
    }

}

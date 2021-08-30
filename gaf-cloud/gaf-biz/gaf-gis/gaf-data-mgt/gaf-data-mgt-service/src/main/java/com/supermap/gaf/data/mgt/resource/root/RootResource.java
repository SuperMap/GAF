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

/**
 * @author heykb
 * @date:2021/3/25
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
    public Class<SpaceDatasourceResource> dataSourceResource() {
        return SpaceDatasourceResource.class;
    }

    @Path("/services")
    public Class<ServiceResource> serviceResource() {
        return ServiceResource.class;
    }
    @Path("/metadatas")
    public Class<MetadataManagementResource> metadataManagementResource() {
        return MetadataManagementResource.class;
    }
    @Path("/sys-resource-datasources")
    public Class<SysResourceDatasourceResource> sysResourceDatasourceResource() {
        return SysResourceDatasourceResource.class;
    }

    @Path("/model-manage/field-associates")
    public Class<MmFieldAssociateResource> mmFieldAssociateResource(){
        return  MmFieldAssociateResource.class;
    }

    @Path("/model-manage/fields")
    public Class<MmFieldResource> mmFieldResource(){
        return  MmFieldResource.class;
    }

    @Path("/model-manage/models")
    public Class<MmModelResource> mmModelResource(){
        return  MmModelResource.class;
    }

    @Path("/model-manage/physics")
    public Class<MmPhysicsResource> mmPhysicsResource(){
        return  MmPhysicsResource.class;
    }

    @Path("/model-manage/logic-tables")
    public Class<MmTableResource> mmTableResource(){
        return  MmTableResource.class;
    }



}

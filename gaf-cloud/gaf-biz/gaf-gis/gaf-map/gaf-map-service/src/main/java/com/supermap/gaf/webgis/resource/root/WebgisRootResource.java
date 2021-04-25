/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.resource.root;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.rest.jersey.JaxrsStaticViewResource;
//import com.supermap.gaf.storage.resources.MinioServiceResource;
import com.supermap.gaf.webgis.domain.WebgisConfigData;
import com.supermap.gaf.webgis.resource.*;
import com.supermap.gaf.webgis.service.WebgisConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * webgis jersey资源根类
 * @Copyright
 * @author wangxiaolong
 * @date 2020-12-05
 */
@Component
@Path("/")
@Api("webgis")
public class WebgisRootResource {

    @Autowired
    private WebgisConfigService webgisConfigService;
    @Path("/view")
    public Class<JaxrsStaticViewResource> staticViewResoruce() {
        return JaxrsStaticViewResource.class;
    }
    @Path("/webgis-services")
    public Class<WebgisServiceResource> webgisServiceResource(){
        return  WebgisServiceResource.class;
    }

    @Path("/iservices")
    public Class<WebgisIserverResource> webgisIserverResource(){
        return  WebgisIserverResource.class;
    }


    @Path("/webgis-buttons")
    public Class<WebgisButtonResource> webgisButtonResource(){
        return  WebgisButtonResource.class;
    }

    @Path("/webgis-toolbar-buttons")
    public Class<WebgisToolbarButtonResource> webgisToolbarButtonResource(){
        return  WebgisToolbarButtonResource.class;
    }

    @Path("/webgis-toolbars")
    public Class<WebgisToolbarResource> webgisToolbarResource(){
        return  WebgisToolbarResource.class;
    }

    @Path("/webgis-catalog-layers")
    public Class<WebgisCatalogLayerResource> webgisCatalogLayerResource(){
        return  WebgisCatalogLayerResource.class;
    }

    @Path("/webgis-service-associations")
    public Class<WebgisServiceAssociationResource> webgisServiceAssociationResource(){
        return  WebgisServiceAssociationResource.class;
    }
    @Path("/webgis-data-service-fields")
    public Class<WebgisDataServiceFieldResource> webgisDataServiceFieldResource(){
        return  WebgisDataServiceFieldResource.class;
    }

    @Path("/webgis-roam-routes")
    public Class<WebgisRoamRouteResource> webgisRoamRouteResource(){
        return  WebgisRoamRouteResource.class;
    }

    @Path("/webgis-roam-stops")
    public Class<WebgisRoamStopResource> webgisRoamStopResource(){
        return  WebgisRoamStopResource.class;
    }

//    @Path("/minio-service")
//    public Class<MinioServiceResource> minioServiceResource() {
//        return MinioServiceResource.class;
//    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "工具条配置", notes = "工具条配置")
    @Path("/resource-catalog/{catalogId}/config")
    @ApiImplicitParam(name = "catalogId",value = "catalogId",paramType = "path",dataType = "string",required = true )
    public MessageResult<WebgisConfigData> getConfig(@PathParam("catalogId")String catalogId){
        WebgisConfigData appConfigData = webgisConfigService.convert2ResourceTreeConfig(catalogId);
        return MessageResult.data(appConfigData).status(200).message("查询成功").build();
    }

}

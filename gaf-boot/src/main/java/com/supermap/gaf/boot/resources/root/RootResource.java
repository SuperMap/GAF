package com.supermap.gaf.boot.resources.root;

import io.swagger.annotations.Api;

import javax.ws.rs.Path;

@Path("/")
@Api("GAF-BOOT接口")
public class RootResource {

    @Path("/authority")
    public Class<com.supermap.gaf.authority.resources.root.RootResource> authorityRootResource() {
        return com.supermap.gaf.authority.resources.root.RootResource.class;
    }

    @Path("/authentication")
    public Class<com.supermap.gaf.authentication.resources.root.RootResource> authenticationRootResource() {
        return com.supermap.gaf.authentication.resources.root.RootResource.class;
    }

    @Path("/portal")
    public Class<com.supermap.gaf.portal.resources.root.PortalRootResoure> portalRootResource() {
        return com.supermap.gaf.portal.resources.root.PortalRootResoure.class;
    }

//    @Path("/proj")
//    public Class<com.supermap.gaf.project.resources.root.ProjRootResource> projRootResource() {
//        return com.supermap.gaf.project.resources.root.ProjRootResource.class;
//    }

    @Path("/map")
    public Class<com.supermap.gaf.webgis.resource.root.WebgisRootResource> webgisRootResourceClass() {
        return com.supermap.gaf.webgis.resource.root.WebgisRootResource.class;
    }

    @Path("/data-mgt")
    public Class<com.supermap.gaf.data.mgt.resource.root.RootResource> dataMgtRootResourceClass() {
        return com.supermap.gaf.data.mgt.resource.root.RootResource.class;
    }

    @Path("/sys-mgt")
    public Class<com.supermap.gaf.sys.mgt.resource.root.RootResource> sysMgtRootResourceClass() {
        return com.supermap.gaf.sys.mgt.resource.root.RootResource.class;
    }

    @Path("/storage")
    public Class<com.supermap.gaf.storage.resource.root.RootResource> storageRootResourceClass() {
        return com.supermap.gaf.storage.resource.root.RootResource.class;
    }

    @Path("/analysis")
    public Class<com.supermap.gaf.analysis.resource.root.AnalysisRootResource> analysisRootResourceClass() {
        return com.supermap.gaf.analysis.resource.root.AnalysisRootResource.class;
    }

}

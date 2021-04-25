/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.rest.jersey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;


/**
 * @author:yj
 * @date:2021/3/25
*/

public class JaxrsStaticViewResource {

    @GET
    @Path("{prePath:.*}/static/{path:.*}")
    @Produces( { "text/html", "text/javascript", "application/javascript", "text/css", "image/png", "image/gif", "image/bmp", "image/jpeg", "application/x-woff", "application/vnd.ms-fontobject", "image/svg+xml", "application/x-font-ttf","application/octet-stream" })
    public Response get(@PathParam("path") String path, @Context HttpServletRequest requset, @Context HttpServletResponse response) {
        return new JaxrsStaticResource().get(path, requset, response);
    }
    
    @GET
    @Path("/{path:.*}")
    @Produces( {"*/*", "text/html", "text/javascript", "application/javascript", "text/css", "image/png", "image/gif", "image/bmp", "image/jpeg", "application/x-woff", "application/vnd.ms-fontobject", "image/svg+xml", "application/x-font-ttf","application/octet-stream" })
    public Response get2(@PathParam("path") String path, @Context HttpServletRequest requset, @Context HttpServletResponse response) {
        if (!path.endsWith(".html")){
            response.addHeader("cache-control","max-age=600");
        }
        return new JaxrsStaticResource().get(path, requset, response);
    }
    @GET
    @Path("/swagger/{path:.*}")
    @Produces( { "text/html", "text/javascript", "application/javascript", "text/css", "image/png", "image/gif", "image/bmp", "image/jpeg", "application/x-woff", "application/vnd.ms-fontobject", "image/svg+xml", "application/x-font-ttf","application/octet-stream" })
    public Response get3(@PathParam("path") String path,@Context HttpServletRequest requset, @Context HttpServletResponse response) {
//        response.addHeader("cache-control","max-age=600");
        return new JaxrsStaticResource().getSwagger(path,requset,response);
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.api.resources;

import com.supermap.gaf.api.scanner.entity.ApiDoc;
import com.supermap.gaf.api.scanner.service.SwaggerApiDocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author dqc
 * @date:2021/3/25 /docs/swagger
 */
@Component
@Path("/")
@Api("Apidoc文件的读取接口")
public class SwaggerApiResource {
    @Autowired
    private SwaggerApiDocService apiDocService;


    @ApiOperation(value = "读取对应服务的Apidoc", notes = "读取某个服务的swagger.json文件内容，用于前端展示api界面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceName", value = "服务名", paramType = "path", dataType = "string", required = true, example = "GAF-XX")
    })
    @Path("/{serviceName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getSwagger(@PathParam("serviceName") String serviceName) throws Exception {
        ApiDoc apiDoc = apiDocService.getApiDoc(serviceName);
        if (null != apiDoc) {
            return apiDoc.getData();
        }
        return null;
    }

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.portal.resources;


import com.supermap.gaf.commontypes.MessageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
@Api(value = "环境变量接口")
@Component
public class PropResource {
    @Autowired
    private Environment env;

    @ApiOperation(value = "根据名称查询环境变量",notes = "根据名称查询环境变量")
    @ApiImplicitParam(name = "name",value = "环境变量名称",paramType = "path",dataType = "String",example = "xxxx")
    @Path("/{name}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult queryProp(@PathParam("name") String name) {
        return MessageResult.successe(String.class).data(env.getProperty(name)).build();
    }

}

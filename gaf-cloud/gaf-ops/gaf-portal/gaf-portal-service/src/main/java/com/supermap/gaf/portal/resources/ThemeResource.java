/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.portal.resources;


import com.supermap.gaf.portal.menu.commontypes.ThemeInfo;
import com.supermap.gaf.portal.menu.service.ThemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
@Api(value = "主题配置接口")
public class ThemeResource {
    @Autowired
    private ThemeService themeService;

    @ApiOperation(value = "新增主题配置",notes = "新增主题配置")
    @ApiImplicitParam(name = "info",value = "主题配置信息",paramType = "body",dataTypeClass = ThemeInfo.class)
    @POST
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    public String saveTheme(ThemeInfo info) {
        return themeService.saveTheme(info);
    }

    @ApiOperation(value = "查询当前用户主题配置",notes = "查询当前用户主题配置")
    @Path("")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String queryTheme() {
        return themeService.queryTheme();
    }
}

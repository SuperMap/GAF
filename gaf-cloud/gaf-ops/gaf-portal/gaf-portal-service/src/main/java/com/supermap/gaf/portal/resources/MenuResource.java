/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.portal.resources;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.supermap.gaf.portal.menu.commontypes.MenuInfo;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.supermap.gaf.portal.menu.service.MenuService;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
@Api(value = "菜单管理接口", hidden = true)
public class MenuResource {
    @Autowired
    @Qualifier("portal")
    private MenuService menuService;

    @ApiOperation(value = "查询所有菜单信息", notes = "查询所有菜单信息返回JSON格式的菜单信息", hidden = true)
    @Path("/")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String queryMenu() {
        return menuService.queryMenu();
    }

    @ApiOperation(value = "根据用户查询所有菜单信息", hidden = true, notes = "根据用户查询所有菜单信息返回数组形式的菜单信息")
    @Path("/new")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<MenuInfo> queryMenuNew() {
        return menuService.queryMenuFromAuthority();
    }

}

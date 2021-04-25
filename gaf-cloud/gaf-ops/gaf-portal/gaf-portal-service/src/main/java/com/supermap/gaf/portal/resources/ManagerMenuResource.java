/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.portal.resources;


import com.alibaba.fastjson.JSONArray;
import com.supermap.gaf.portal.menu.commontypes.CustomationInfo;
import com.supermap.gaf.portal.menu.commontypes.MenuInfo;
import com.supermap.gaf.portal.menu.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Array;
import java.util.List;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
@Api(value = "菜单管理接口",hidden = true)
public class ManagerMenuResource {
    @Autowired
    @Qualifier("manager")
    private MenuService menuService;

    @ApiOperation(value = "新增菜单",notes = "新增菜单",hidden = true)
    @ApiImplicitParam(name = "info",value = "菜单实体类",paramType = "body",dataTypeClass = MenuInfo.class)
    @POST
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON})
    public String addMenu(MenuInfo info) {
        return menuService.addMenu(info);
    }

    @ApiOperation(value = "批量导入菜单",notes = "批量导入菜单",hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flag",value = "是否删除原有菜单空串，''就不删除，其他就删除",paramType = "path",dataType = "String"),
            @ApiImplicitParam(name = "menus",value = "JSON格式的菜单数组",paramType = "path",dataType = "String")
    })
    @POST
    @Path("/importMenus/{flag}")
    @Produces({MediaType.APPLICATION_JSON})
    public String importAllMenu(@PathParam("flag")String flag, @PathVariable("menus") String menus) {
        List<MenuInfo> array = JSONArray.parseArray(menus,MenuInfo.class);
        System.out.println(array);
        return menuService.importMenus(flag,array);
    }

    @ApiOperation(value = "修改菜单",notes = "根据菜单ID修改菜单",hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId",value = "当前菜单ID",paramType = "path",dataType = "String"),
            @ApiImplicitParam(name = "MenuInfo",value = "菜单实体类信息",paramType = "body",dataTypeClass = MenuInfo.class)
    })
    @PUT
    @Path("/{menuId}")
    @Produces({MediaType.APPLICATION_JSON})
    public String updateMenu(@PathParam("menuId")String menuId, MenuInfo info) {
        return menuService.updateMenu(menuId,info);
    }

    @ApiOperation(value = "删除菜单",notes = "根据菜单ID删除菜单",hidden = true)
    @ApiImplicitParam(name = "menuId",value = "当前菜单ID",paramType = "path",dataType = "String")
    @DELETE
    @Path("/{menuId}")
    @Produces({MediaType.APPLICATION_JSON})
    public String deleteMenu(@PathParam("menuId")String menuId) {
        return menuService.deleteMenu(menuId);
    }

    @ApiOperation(value = "查询单个菜单信息",notes = "根据菜单ID查询菜单",hidden = true)
    @ApiImplicitParam(name = "menuId",value = "当前菜单ID",paramType = "path",dataType = "String")
    @Path("/{menuId}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String queryMenu(@PathParam("menuId")String menuId) {
        return menuService.queryMenu(menuId);
    }

    @ApiOperation(value = "查询所有菜单信息",notes = "查询所有菜单信息",hidden = true)
    @Path("/")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String queryMenu() {
        return menuService.queryMenu();
    }
}

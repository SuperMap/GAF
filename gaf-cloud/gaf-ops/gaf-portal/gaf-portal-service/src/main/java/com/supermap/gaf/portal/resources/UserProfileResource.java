/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.portal.resources;

import com.supermap.gaf.portal.menu.service.CustomationServices;
import com.supermap.gaf.portal.menu.service.MenuService;
import com.supermap.gaf.portal.menu.service.UserProfileService;
import com.supermap.gaf.utils.LogUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
@Api(value = "用户简要信息接口")
public class UserProfileResource {

    private static Logger logger = LogUtil.getLocLogger(UserProfileResource.class);

    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private CustomationServices customationServices;
    @Autowired
    @Qualifier("portal")
    private MenuService menuService;

    @ApiOperation(value = "查看用户简要信息", notes = "查看用户简要信息")
    @Path("/")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String queryUserProfile() {
        return userProfileService.queryUserProfile();
    }

    @ApiOperation(value = "查看用户个人信息", notes = "查看用户个人信息，包括门户定制信息、个人信息、授权菜单")
    @Path("/detail")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Map<String, Object> queryUserProfileDetail() {
        try {
            Map<String, Object> variables = new HashMap<String, Object>(16);
            variables.put("config", customationServices.queryConfig());
            variables.put("user", userProfileService.getLoginUserInfo());
            variables.put("navs", menuService.queryMenuFromAuthority());
            return variables;
        } catch (Exception e) {
            logger.error("查询用户画像详情异常", e);
            return new HashMap<String, Object>(16);
        }
    }
    @ApiOperation(value = "查看默认定制信息", notes = "查看默认定制信息")
    @Path("/detail/default")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Map<String, Object> queryUserDefaultProfileDetail() {
        try {
            Map<String, Object> variables = new HashMap<String, Object>(16);
            variables.put("config", customationServices.queryDefaultConfig());
            return variables;
        } catch (Exception e) {
            logger.error("查询用户画像详情异常", e);
            return new HashMap<String, Object>(16);
        }
    }
}

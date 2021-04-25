/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.portal.resources;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.supermap.gaf.commontypes.ShiroUser;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.utils.LogUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.portal.menu.commontypes.CustomationInfo;
import com.supermap.gaf.portal.menu.service.CustomationService;

import io.swagger.annotations.Api;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
@Api(value = "门户订制配置接口")
public class ManagerCustomationResource {
    private static Logger logger = LogUtil.getLocLogger(ManagerCustomationResource.class);

    @Autowired
    private CustomationService customationService;

    @ApiOperation(value = "保存租户门户订制配置",notes = "保存租户门户订制配置，如果存在则是修改操作")
    @ApiImplicitParam(name = "customationInfo",value = "主题配置实体类",paramType = "body",dataTypeClass =CustomationInfo.class)
    @POST
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    public String saveCustomation(CustomationInfo customationInfo) {
        ShiroUser currentUser = SecurityUtilsExt.getUser();
        String userId = ((com.supermap.gaf.shiro.commontypes.ShiroUser) currentUser).getId();

        customationInfo.setUser(currentUser.getUsername());
        customationInfo.setTenantId(currentUser.getTenantId());
        customationInfo.setCreatedBy(userId);
        customationInfo.setUpdatedBy(userId);
        return customationService.saveCustomation(customationInfo);
    }

    @ApiOperation(value = "修改租户门户订制配置",notes = "修改租户门户订制配置")
    @ApiImplicitParam(name = "customationInfo",value = "主题配置实体类",paramType = "body",dataTypeClass =CustomationInfo.class)
    @PUT
    @Path("")
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Boolean> modifyCustomation(CustomationInfo customationInfo) {
        ShiroUser currentUser = SecurityUtilsExt.getUser();
        String userId = ((com.supermap.gaf.shiro.commontypes.ShiroUser) currentUser).getId();

        customationInfo.setUser(currentUser.getUsername());
        customationInfo.setTenantId(currentUser.getTenantId());
        customationInfo.setUpdatedBy(userId);
        return customationService.updateCustomation(customationInfo);
    }
    @ApiOperation(value = "查询租户门户订制配置",notes = "根据登录人查询该租户门户订制配置")
    @Path("")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String queryCustomation() {
        ShiroUser currentUser = SecurityUtilsExt.getUser();
        return customationService.queryCustomation(currentUser.getTenantId());
    }

    /**
     * 保存首页设置
     * @param customationInfo
     * @return
     */
    @ApiOperation(value = "保存首页设置",notes = "保存首页设置")
    @ApiImplicitParam(name = "customationInfo",value = "首页配置实体类",paramType = "body",dataTypeClass = CustomationInfo.class)
    @POST
    @Path("/home")
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Boolean> saveConfigInfo(CustomationInfo customationInfo) {
        ShiroUser currentUser = SecurityUtilsExt.getUser();
        String userId = ((com.supermap.gaf.shiro.commontypes.ShiroUser) currentUser).getId();

        customationInfo.setUser(currentUser.getUsername());
        customationInfo.setTenantId(currentUser.getTenantId());
        customationInfo.setUpdatedBy(userId);
        return customationService.updateConfigInfo(customationInfo);
    }

    /**
     * 同步首页设置到默认
     * @param customationInfo
     * @return
     */
    @ApiOperation(value = "同步首页设置到默认",notes = "同步首页设置到默认")
    @ApiImplicitParam(name = "customationInfo",value = "主题配置实体类",paramType = "body",dataTypeClass =CustomationInfo.class)
    @POST
    @Path("/home/default")
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Boolean> saveConfigInfo2Default(CustomationInfo customationInfo) {
        ShiroUser currentUser = SecurityUtilsExt.getUser();
        String userId = ((com.supermap.gaf.shiro.commontypes.ShiroUser) currentUser).getId();

        customationInfo.setUser(currentUser.getUsername());
        customationInfo.setTenantId(currentUser.getTenantId());
        customationInfo.setUpdatedBy(userId);
        return customationService.updateConfigInfo2Default(customationInfo);
    }

    /**
     * 从默认恢复首页设置
     * @return
     */
    @ApiOperation(value = "从默认恢复首页设置",notes = "从默认恢复首页设置")
    @POST
    @Path("/home/config")
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<Boolean> saveDefault2ConfigInfo() {
        ShiroUser currentUser = SecurityUtilsExt.getUser();
        String userId = ((com.supermap.gaf.shiro.commontypes.ShiroUser) currentUser).getId();

        CustomationInfo customationInfo = new CustomationInfo();
        customationInfo.setUser(currentUser.getUsername());
        customationInfo.setTenantId(currentUser.getTenantId());
        customationInfo.setUpdatedBy(userId);
        return customationService.updateDefault2ConfigInfo(customationInfo);
    }
}

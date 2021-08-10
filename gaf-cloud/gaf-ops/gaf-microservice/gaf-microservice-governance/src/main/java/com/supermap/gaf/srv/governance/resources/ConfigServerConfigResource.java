/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.srv.governance.resources;


import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.commontypes.Page;
import com.supermap.gaf.srv.governance.entity.rest.configserver.MicroServiceInfo;
import com.supermap.gaf.srv.governance.rest.client.GafConfigServerClient;
import com.supermap.gaf.srv.governance.vo.ConfigPropertiesGroup;
import com.supermap.gaf.srv.governance.vo.ConfigPropertiesVo;
import com.supermap.gaf.srv.governance.vo.GroupWithProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


/**
 * @author dqc
 * @date:2021/3/25 /srv-governance/config
 */
@Component
@Api(value = "微服务配置管理接口")
public class ConfigServerConfigResource {
    @Autowired
    private GafConfigServerClient configServerClient;

    @ApiOperation(value = "查询应用配置文件信息", notes = "获取租户的所有应用配置,也可以通过参数获取对应应用的配置，返回分页结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "serviceName", value = "微服务名", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "profile", value = "环境", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "label", value = "分支", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "pageIndex", value = "分页索引", paramType = "query", dataType = "integer")
    })
    @GET
    @Path("/configurations")
    @Produces(APPLICATION_JSON)
    public MessageResult<Page<ConfigPropertiesGroup>> getServerConfigInfos(@QueryParam(value = "tenantId") String tenantId,
                                                                           @QueryParam(value = "serviceName") String serviceName,
                                                                           @QueryParam(value = "profile") String profile,
                                                                           @QueryParam(value = "label") String label,
                                                                           @DefaultValue("10") @QueryParam(value = "pageSize") Integer pageSize,
                                                                           @DefaultValue("1") @QueryParam(value = "pageIndex") Integer pageIndex) {
        return configServerClient.getServerConfigInfos(tenantId, serviceName, profile, label, pageIndex, pageSize);
    }

    /**
     * 根据租户id和应用程序名称和分支及环境查询配置信息
     *
     * @param serviceName 应用程序名称
     * @param label       分支
     * @param profile     环境
     * @return 返回应用配置信息不分页
     * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
     */
    @ApiOperation(value = "返回应用配置信息不分页", notes = "根据租户id和应用程序名称和分支及环境查询配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "serviceName", value = "应用程序名称", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "profile", value = "环境", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "label", value = "分支", paramType = "query", dataType = "string", required = true),
    })
    @GET
    @Path("/configurations/properties")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResult<List<ConfigPropertiesVo>> listConfigPropertiesVo(@QueryParam(value = "tenantId") String tenantId, @QueryParam(value = "serviceName") String serviceName,
                                                                          @QueryParam(value = "profile") String profile, @QueryParam(value = "label") String label) {
        return configServerClient.listConfigPropertiesVo(tenantId, serviceName, profile, label);
    }


    @ApiOperation(value = "批量添加配置属性", notes = "批量添加配置属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupWithProperties", value = "批量添加配置属性请求参数", paramType = "body", dataTypeClass = GroupWithProperties.class)
    })
    @POST
    @Path("/configurations/batchadd")
    @Produces({APPLICATION_JSON})
    public MessageResult<String> addServerConfigInfo(GroupWithProperties parameter) {
        return configServerClient.addServerConfigInfo(parameter);
    }


//    @POST
//    @Path("/configurations/batchmodify")
//    @Produces(APPLICATION_JSON)
//    public MessageResult <String> updateServerConfigInfo(ServerConfigInfo params) {
//        return configServerClient.updateServerConfigInfo(params);
//    }

//    @DELETE
//    @Path("/configurations")
//    @Produces(MediaType.APPLICATION_JSON)
//    @ApiOperation(value = "根据id集合批量删除配置", notes = "根据id集合批量删除配置")
//    public MessageResult <String> batchDeleteConfig(@NotEmpty List<String> ids) {
//        return configServerClient.batchDeleteConfig(ids);
//    }

    @ApiOperation(value = "根据条件批量删除配置", notes = "条件是application(服务名称),label(分支), profile(环境), tenantId(租户id)的集合")
    @DELETE
    @Path("/configurations")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResult<String> batchDeleteConfig(@NotEmpty List<ConfigPropertiesGroup> configPropertiesGroups) {
        return configServerClient.batchDeleteConfig(configPropertiesGroups);
    }

    @ApiOperation(value = "删除配置属性", notes = "通过Id删除某项配置属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "配置属性ID", paramType = "path", dataType = "string", required = true)
    })
    @DELETE
    @Path("/configurations/{id}")
    @Produces(APPLICATION_JSON)
    public MessageResult<String> deleteServerConfigInfo(@PathParam("id") String id) {
        return configServerClient.deleteServerConfigInfo(id);
    }

    @ApiOperation(value = "根据条件批量删除配置", notes = "通过服务配置属性分组及属性批量删除配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupWithProperties", value = "服务配置属性分组及属性", paramType = "body", dataTypeClass = GroupWithProperties.class)
    })
    @PUT
    @Path("/batchupdate")
    @Produces({APPLICATION_JSON})
    public MessageResult<String> batchUpdateServerConfigInfo(GroupWithProperties parameter) {
        return configServerClient.batchUpdateServerConfigInfo(parameter);
    }

    @ApiOperation(value = "检查服务名占用", notes = "通过服务名参数，检查服务名是否被占用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicationName", value = "应用服务名", paramType = "query", dataType = "string", required = true)
    })
    @GET
    @Path("/checkappname")
    @Produces({APPLICATION_JSON})
    public MessageResult<String> getApplicationNames(@QueryParam(value = "applicationName") String applicationName) {
        return configServerClient.getApplicationNames(applicationName);
    }


    @ApiOperation(value = "刷新服务配置", notes = "热更新服务配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceName", value = "应用服务名", paramType = "query", dataType = "string", required = true)
    })
    @POST
    @Path("/configurations/refresh")
    @Produces(APPLICATION_JSON)
    public MessageResult<String> refreshConfigurations(@QueryParam(value = "serviceName") String serviceName) {
        return configServerClient.refreshConfigurations(serviceName);
    }

    @ApiOperation(value = "获取注册中心全部服务实例信息", notes = "通过注册中心地址，获取注册中心全部服务实例信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "eurekaIp", value = "注册中心ip", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "eurekaPort", value = "注册中心端口", paramType = "query", dataType = "string", required = true)
    })
    @GET
    @Path("/servicenames")
    @Produces({APPLICATION_JSON})
    public MessageResult<List<MicroServiceInfo>> getServiceInstanceInfos(@QueryParam("eurekaIp") String eurekaIp,
                                                                         @QueryParam("eurekaPort") String eurekaPort) {
        return configServerClient.getServiceInstanceInfos(eurekaIp, eurekaPort);
    }

    @ApiOperation(value = "获取注册中心服务实例信息", notes = "通过服务名，获取对应注册服务实例的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceName", value = "微服务应用名称", paramType = "path", dataType = "string", required = true)
    })
    @GET
    @Path("/servicenames/{serviceName}")
    @Produces({APPLICATION_JSON})
    public MessageResult<MicroServiceInfo> getServiceInstanceInfo(@PathParam("serviceName") String serviceName) {
        return configServerClient.getServiceInstanceInfo(serviceName);
    }
}

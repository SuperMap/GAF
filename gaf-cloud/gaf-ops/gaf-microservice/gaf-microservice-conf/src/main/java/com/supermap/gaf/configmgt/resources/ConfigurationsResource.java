/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.configmgt.resources;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.commontypes.Page;
import com.supermap.gaf.configmgt.commontypes.ConfigPropertiesGroup;
import com.supermap.gaf.configmgt.commontypes.GroupWithProperties;
import com.supermap.gaf.configmgt.vo.ConfigPropertiesVo;
import com.supermap.gaf.microservice.ConfigQueryParameter;
import com.supermap.gaf.configmgt.services.ConfigServerMgtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
 * @version 1.0 2019-8-9
 * @description 应用程序配置资源
 * @date:2021/3/25 /configcenter/manager
 */
@Api(value = "配置中心配置接口")
@Component
@Path("/")
public class ConfigurationsResource {

    @Autowired
    private ConfigServerMgtService configServerMgtService;


    @Path("/servicenames")
    public Class<MicroServiceMgtResoure> testApp() {
        return MicroServiceMgtResoure.class;
    }


    /**
     * @param serviceName 应用程序名称
     * @param label       分支
     * @param profile     环境
     * @param tenantId    租户id
     * @return 返回应用配置文件信息
     * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
     * @description 获取租户的所有应用配置, 也可以通过参数获取对应应用的配置
     */
    @ApiOperation(value = "查询应用配置文件信息", notes = "获取租户的所有应用配置,也可以通过参数获取对应应用的配置，返回分页结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", paramType = "query", dataType = "string", example = "ABC"),
            @ApiImplicitParam(name = "serviceName", value = "微服务名", paramType = "query", dataType = "string", required = true, example = "GAF-XX"),
            @ApiImplicitParam(name = "profile", value = "环境", paramType = "query", dataType = "string", required = true, example = "prod"),
            @ApiImplicitParam(name = "label", value = "分支", paramType = "query", dataType = "string", required = true, example = "master"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", paramType = "query", dataType = "integer", example = "10"),
            @ApiImplicitParam(name = "pageIndex", value = "分页索引", paramType = "query", dataType = "integer", example = "0")
    })
    @GET
    @Path("/configurations")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResult<Page<ConfigPropertiesGroup>> pageConfigPropertiesGroups(@QueryParam(value = "tenantId") String tenantId,
                                                                                 @QueryParam(value = "serviceName") String serviceName,
                                                                                 @QueryParam(value = "profile") String profile,
                                                                                 @QueryParam(value = "label") String label,
                                                                                 @DefaultValue("10") @QueryParam(value = "pageSize") Integer pageSize,
                                                                                 @DefaultValue("1") @QueryParam(value = "pageIndex") Integer pageIndex) {
        ConfigQueryParameter queryParameter = new ConfigQueryParameter(serviceName, profile, label, tenantId);
        queryParameter.setPageIndex(pageIndex);
        queryParameter.setPageSize(pageSize);
        Page<ConfigPropertiesGroup> page = configServerMgtService.pageConfigPropertiesGroups(queryParameter);
        MessageResult<Page<ConfigPropertiesGroup>> result = new MessageResult<>();
        result.setSuccessed(true);
        result.setData(page);
        return result;
    }

    /**
     * @param serviceName 应用程序名称
     * @param label       分支
     * @param profile     环境
     * @return 返回应用配置信息不分页
     * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
     * @description 根据租户id和应用程序名称和分支及环境查询配置信息
     */
    @ApiOperation(value = "返回应用配置信息不分页", notes = "根据租户id和应用程序名称和分支及环境查询配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户id", paramType = "query", dataType = "string", example = "ABC"),
            @ApiImplicitParam(name = "serviceName", value = "应用程序名称", paramType = "query", dataType = "string", required = true, example = "GAF-XX"),
            @ApiImplicitParam(name = "profile", value = "环境", paramType = "query", dataType = "string", required = true, example = "prod"),
            @ApiImplicitParam(name = "label", value = "分支", paramType = "query", dataType = "string", required = true, example = "master"),
    })
    @GET
    @Path("/configurations/properties")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResult<List<ConfigPropertiesVo>> listConfigPropertiesVo(@QueryParam(value = "tenantId") String tenantId,
                                                                          @QueryParam(value = "serviceName") String serviceName,
                                                                          @QueryParam(value = "profile") String profile,
                                                                          @QueryParam(value = "label") String label) {
        ConfigQueryParameter queryParameter = new ConfigQueryParameter(serviceName, profile, label, tenantId);
        List<ConfigPropertiesVo> configPropertiesVoList = configServerMgtService.listConfigPropertiesVo(queryParameter);
        MessageResult<List<ConfigPropertiesVo>> result = new MessageResult<>();
        result.setSuccessed(true);
        result.setData(configPropertiesVoList);
        return result;
    }

    @ApiOperation(value = "批量添加配置属性", notes = "批量添加配置属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupWithProperties", value = "批量添加配置属性请求参数", paramType = "body", dataTypeClass = GroupWithProperties.class)
    })
    @POST
    @Path("/configurations/batchadd")
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<String> addConfig(GroupWithProperties groupWithProperties) {
        MessageResult<String> messageResult = new MessageResult<>();
        try {
            boolean flag = configServerMgtService.saveConfig(groupWithProperties);
            messageResult.setSuccessed(flag);
        } catch (Exception e) {
            messageResult.setMessage(e.getMessage());
        }
        return messageResult;
    }

    @ApiOperation(value = "删除配置属性", notes = "通过Id删除某项配置属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "配置属性ID", paramType = "path", dataType = "string", required = true, example = "ABCDEF")
    })
    @DELETE
    @Path("/configurations/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResult<String> deleteServerConfigInfo(@NotEmpty @PathParam("id") String id) {
        MessageResult<String> messageResult = new MessageResult<>();
        try {
            boolean flag = configServerMgtService.removeById(id);
            messageResult.setSuccessed(flag);
        } catch (Exception e) {
            messageResult.setMessage(e.getMessage());
        }
        return messageResult;
    }

    @ApiOperation(value = "根据条件批量删除配置", notes = "条件是application(服务名称),label(分支), profile(环境), tenantId(租户id)的集合")
    @DELETE
    @Path("/configurations")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResult<String> batchDeleteConfig(@NotEmpty List<ConfigPropertiesGroup> conditions) {
        MessageResult<String> messageResult = new MessageResult<>();
        // 条件校验
        for (ConfigPropertiesGroup condition : conditions) {
            if (StringUtils.isEmpty(condition.getApplication())
                    || StringUtils.isEmpty(condition.getLabel())
                    || StringUtils.isEmpty(condition.getProfile())
                    || StringUtils.isEmpty(condition.getTenantId())) {
                messageResult.setSuccessed(false);
                messageResult.setMessage("所有条件都不能为空");
                return messageResult;
            }
        }
        try {
            boolean flag = configServerMgtService.remove(conditions);
            messageResult.setSuccessed(flag);
        } catch (Exception e) {
            messageResult.setMessage(e.getMessage());
        }
        return messageResult;
    }

    @ApiOperation(value = "根据条件批量编辑配置", notes = "通过服务配置属性分组及属性批量编辑配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupWithProperties", value = "服务配置属性分组及属性", paramType = "body", dataTypeClass = GroupWithProperties.class)
    })
    @PUT
    @Path("/batchupdate")
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<String> batchUpdateConfig(GroupWithProperties groupWithProperties) {
        MessageResult<String> messageResult = new MessageResult<>();
        try {
            boolean flag = configServerMgtService.batchUpdateConfig(groupWithProperties);
            messageResult.setSuccessed(flag);
        } catch (Exception e) {
            messageResult.setMessage(e.getMessage());
        }
        return messageResult;
    }

    @ApiOperation(value = "检查服务名占用", notes = "通过服务名参数，检查服务名是否被占用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicationName", value = "应用服务名", paramType = "query", dataType = "string", required = true, example = "GAF-XX")
    })
    @GET
    @Path("/checkappname")
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult<String> getApplicationNames(@QueryParam(value = "applicationName") String applicationName) {
        boolean isExisted = configServerMgtService.checkApplicationName(applicationName);
        if (isExisted) {
            return MessageResult.failed(String.class).message("该服务名已存在，请重新指定").successed(false).build();
        }
        return MessageResult.successe(String.class).message("服务名可用").successed(true).data(applicationName).build();
    }

    @ApiOperation(value = "刷新服务配置", notes = "热更新服务配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceName", value = "应用服务名", paramType = "query", dataType = "string", required = true, example = "GAF-XX")
    })
    @POST
    @Path("/configurations/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResult<String> refreshConfigurations(@QueryParam(value = "serviceName") String serviceName) {
        MessageResult<String> messageResult = new MessageResult<>();
        try {
            boolean flag = configServerMgtService.refreshConfiguration(serviceName);
            messageResult.setSuccessed(flag);
        } catch (Exception e) {
            messageResult.setMessage(e.getMessage());
        }
        return messageResult;
    }


}

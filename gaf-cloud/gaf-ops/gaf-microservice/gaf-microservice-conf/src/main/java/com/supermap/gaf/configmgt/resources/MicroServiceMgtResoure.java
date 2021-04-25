/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.configmgt.resources;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.configmgt.commontypes.MicroServiceInfo;
import com.supermap.gaf.configmgt.services.EurekaServiceMgtService;
import com.supermap.gaf.utils.LogUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author dqc
 * @date:2021/3/25
 * /configcenter/manager/servicenames
 */
@Component
@Api("微服务治理资源API")
@Path("/")
public class MicroServiceMgtResoure {

    private static Logger logger = LogUtil.getLocLogger(MicroServiceMgtResoure.class);

    @Autowired
    private EurekaServiceMgtService microServiceMgtService;


    @ApiOperation(value = "获取注册中心全部服务实例信息", notes = "通过注册中心地址，获取注册中心全部服务实例信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "eurekaIp",value = "注册中心ip",paramType = "query",dataType = "string",required = true,example = "192.168.1.1"),
            @ApiImplicitParam(name = "eurekaPort",value = "注册中心端口",paramType = "query",dataType = "string",required = true,example = "8761")
    })
    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult <List <MicroServiceInfo>> getServiceInstanceInfos(@QueryParam("eurekaIp") String eurekaIp,
                                                                           @QueryParam("eurekaPort") String eurekaPort) {
        MessageResult <List <MicroServiceInfo>> result = new MessageResult <>();
        try {
            List <MicroServiceInfo> serviceInstanceInfos;
            if (StringUtils.isEmpty(eurekaIp) || StringUtils.isEmpty(eurekaPort)) {
                serviceInstanceInfos = microServiceMgtService.getAllMicroServices();
            } else {
                serviceInstanceInfos = microServiceMgtService.getAllMicroServices(eurekaIp, eurekaPort);
            }
            if (CollectionUtils.isNotEmpty(serviceInstanceInfos)) {
                result.setSuccessed(true);
                result.setMessage("成功获取到" + serviceInstanceInfos.size() + "条记录");
                result.setData(serviceInstanceInfos);
            } else {
                result.setMessage("获取失败");
                result.setSuccessed(false);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setMessage("获取失败：" + e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "获取注册中心服务实例信息", notes = "通过服务名，获取对应注册服务实例的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceName",value = "微服务应用名称",paramType = "path",dataType = "string",required = true,example = "GAF-XX")
    })
    @GET
    @Path("/{serviceName}")
    @Produces({MediaType.APPLICATION_JSON})
    public MessageResult <MicroServiceInfo> getServiceInstanceInfo(@PathParam("serviceName") String serviceName) {
        try {
            MicroServiceInfo microServiceInfo = microServiceMgtService.getMicroService(serviceName);
            if (microServiceInfo != null) {
                return MessageResult.successe(MicroServiceInfo.class).data(microServiceInfo).successed(true).message("获取成功").build();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return MessageResult.failed(MicroServiceInfo.class).successed(false).message("获取失败:" + e.getMessage()).build();
        }
        return MessageResult.failed(MicroServiceInfo.class).successed(false).message("未能正确获取到服务信息").build();
    }

}

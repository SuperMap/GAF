/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.srv.governance.rest.client;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.commontypes.Page;
import com.supermap.gaf.srv.governance.entity.rest.configserver.MicroServiceInfo;
import com.supermap.gaf.srv.governance.vo.ConfigPropertiesGroup;
import com.supermap.gaf.srv.governance.vo.ConfigPropertiesVo;
import com.supermap.gaf.srv.governance.vo.GroupWithProperties;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/12/05 5:43 PM
 */
@FeignClient(name = "GAF-MICROSERVICE-CONF")
@Component
public interface GafConfigServerClient {
    /**
     * 获取租户的所有应用配置,也可以通过参数获取对应应用的配置
     *
     * @param tenantId
     * @param serviceName
     * @param profile
     * @param label
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/configcenter/manager/configurations")
    MessageResult<Page<ConfigPropertiesGroup>> getServerConfigInfos(@RequestParam(value = "tenantId") String tenantId,
                                                                    @RequestParam(value = "serviceName") String serviceName,
                                                                    @RequestParam(value = "profile") String profile,
                                                                    @RequestParam(value = "label") String label,
                                                                    @RequestParam(value = "pageIndex") Integer pageIndex,
                                                                    @RequestParam(value = "pageSize") Integer pageSize);

    /**
     * 根据租户id和应用程序名称和分支及环境查询配置信息
     *
     * @param tenantId
     * @param serviceName
     * @param profile
     * @param label
     * @return
     */
    @GetMapping("/configcenter/manager/configurations/properties")
    MessageResult<List<ConfigPropertiesVo>> listConfigPropertiesVo(@RequestParam(value = "tenantId") String tenantId, @RequestParam(value = "serviceName") String serviceName,
                                                                   @RequestParam(value = "profile") String profile, @RequestParam(value = "label") String label);

    /**
     * 批量添加配置属性
     *
     * @param parameter
     * @return
     */
    @PostMapping("/configcenter/manager/configurations/batchadd")
    MessageResult<String> addServerConfigInfo(@RequestBody GroupWithProperties parameter);


    /**
     * 删除配置属性
     *
     * @param id
     * @return
     */
    @DeleteMapping("/configcenter/manager/configurations/{id}")
    MessageResult<String> deleteServerConfigInfo(@PathVariable("id") String id);

    /**
     * 根据条件批量编辑配置
     *
     * @param parameter
     * @return
     */
    @PutMapping("/configcenter/manager/batchupdate")
    MessageResult<String> batchUpdateServerConfigInfo(@RequestBody GroupWithProperties parameter);

    /**
     * 检查服务名占用
     *
     * @param applicationName
     * @return
     */
    @GetMapping("/configcenter/manager/checkappname")
    MessageResult<String> getApplicationNames(@RequestParam(value = "applicationName") String applicationName);

    /**
     * 刷新服务配置
     *
     * @param serviceName
     * @return
     */
    @PostMapping("/configcenter/manager/configurations/refresh")
    MessageResult<String> refreshConfigurations(@RequestParam(value = "serviceName") String serviceName);


    /**
     * 获取注册中心全部服务实例信息
     *
     * @param eurekaIp
     * @param eurekaPort
     * @return
     */
    @GetMapping("/configcenter/manager/servicenames")
    MessageResult<List<MicroServiceInfo>> getServiceInstanceInfos(@RequestParam("eurekaIp") String eurekaIp,
                                                                  @RequestParam("eurekaPort") String eurekaPort);

    /**
     * 获取注册中心服务实例信息
     *
     * @param serviceName
     * @return
     */
    @GetMapping("/configcenter/manager/servicenames/{serviceName}")
    MessageResult<MicroServiceInfo> getServiceInstanceInfo(@PathVariable("serviceName") String serviceName);

    /**
     * 根据条件批量删除配置
     *
     * @param onfigPropertiesGroup
     * @return
     */
    @DeleteMapping("/configcenter/manager/configurations")
    MessageResult<String> batchDeleteConfig(@RequestBody @NotEmpty List<ConfigPropertiesGroup> onfigPropertiesGroup);
}




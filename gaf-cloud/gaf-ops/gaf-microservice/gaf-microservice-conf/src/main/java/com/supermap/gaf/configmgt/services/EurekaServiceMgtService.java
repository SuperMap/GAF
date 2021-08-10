/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.configmgt.services;


import com.supermap.gaf.configmgt.commontypes.MicroServiceInfo;

import java.util.List;

/**
 * @program: gaf-microservice-mgt
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/08/12
 */
public interface EurekaServiceMgtService {
    /**
     * 获取所有微服务信息
     *
     * @param eurekaIp
     * @param eurekaPort
     * @return
     */
    List<MicroServiceInfo> getAllMicroServices(String eurekaIp, String eurekaPort);

    /**
     * 根据服务名获取服务信息
     *
     * @param eurekaIp
     * @param eurekaPort
     * @param serviceName
     * @return
     */
    MicroServiceInfo getMicroService(String eurekaIp, String eurekaPort, String serviceName);

    /**
     * 获取所有微服务信息
     *
     * @return
     */
    List<MicroServiceInfo> getAllMicroServices();

    /**
     * 根据服务名获取服务信息
     *
     * @param serviceName
     * @return
     */
    MicroServiceInfo getMicroService(String serviceName);
}

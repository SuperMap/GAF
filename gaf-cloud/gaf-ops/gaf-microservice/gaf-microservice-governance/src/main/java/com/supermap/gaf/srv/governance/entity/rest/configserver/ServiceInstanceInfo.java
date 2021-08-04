/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.srv.governance.entity.rest.configserver;

import java.util.Map;

/**
 * @program: gaf-microservice-mgt
 * @description: 服务实例信息
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/08/22
 */
public class ServiceInstanceInfo {
    /**
     * 应用名
     */
    public String appName;
    /**
     * 理论上等同host
     */
    public String hostName;
    /**
     * ip地址
     */
    public String ipAddr;
    /**
     * 实例端口
     */
    public int port;
    /**
     * 源数据
     */
    public Map<String, String> metadata;
    /**
     * 实例状态
     */
    public InstanceStatus status;
    /**
     * 实例id
     */
    public String instanceId;
    /**
     * 服务id
     */
    public String serviceId;
    /**
     * uri
     */
    public String uri;

    public ServiceInstanceInfo() {
    }

    public ServiceInstanceInfo(String appName, String hostName, String ipAddr, int port, Map<String, String> metadata, InstanceStatus status,
                               String instanceId, String serviceId, String uri) {
        this.appName = appName;
        this.hostName = hostName;
        this.ipAddr = ipAddr;
        this.port = port;
        this.metadata = metadata;
        this.status = status;
        this.instanceId = instanceId;
        this.serviceId = serviceId;
        this.uri = uri;
    }
}

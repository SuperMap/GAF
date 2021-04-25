/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.configmgt.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netflix.appinfo.InstanceInfo;
import com.supermap.gaf.configmgt.commontypes.InstanceStatus;
import com.supermap.gaf.configmgt.commontypes.MicroServiceInfo;
import com.supermap.gaf.configmgt.commontypes.ServiceInstanceInfo;
import com.supermap.gaf.utils.JsonUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: gaf-microservice-mgt
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/08/22
 */
public class MicroServiceConversion {
    /**
     * eureka的服务实例转换为gaf的服务实例信息
     *
     * @param serviceInstances
     * @return
     */
    public static List <ServiceInstanceInfo> getMicroServiceInfo(List <ServiceInstance> serviceInstances) {
        List <ServiceInstanceInfo> serviceInstanceInfos = new ArrayList <>();
        for (ServiceInstance serviceInstance : serviceInstances) {
            EurekaDiscoveryClient.EurekaServiceInstance eurekaServiceInstance = (EurekaDiscoveryClient.EurekaServiceInstance) serviceInstance;
            InstanceInfo instanceInfo = eurekaServiceInstance.getInstanceInfo();

            ServiceInstanceInfo serviceInstanceInfo = new ServiceInstanceInfo();
            serviceInstanceInfo.appName = instanceInfo.getAppName();
            serviceInstanceInfo.hostName = instanceInfo.getHostName();
            serviceInstanceInfo.ipAddr = instanceInfo.getIPAddr();
            serviceInstanceInfo.port = instanceInfo.getPort();
            serviceInstanceInfo.metadata = instanceInfo.getMetadata();
            serviceInstanceInfo.status = InstanceStatus.toEnum(instanceInfo.getStatus().toString());
            serviceInstanceInfo.instanceId = instanceInfo.getInstanceId();
            serviceInstanceInfo.serviceId = serviceInstance.getServiceId();
            serviceInstanceInfo.uri = serviceInstance.getUri().toString();
            serviceInstanceInfos.add(serviceInstanceInfo);
        }
        return serviceInstanceInfos;
    }

    public static MicroServiceInfo getMicroServiceInfo(String microServiceInfoJsonStr){
        MicroServiceInfo microServiceInfo = new MicroServiceInfo();
        JSONObject applicatoinJsonObj = (JSONObject) JSONObject.parseObject(microServiceInfoJsonStr).get("application");
        if (applicatoinJsonObj != null) {
            microServiceInfo.name = applicatoinJsonObj.get("name").toString();
            JSONArray instanceJsonArr = (JSONArray) applicatoinJsonObj.get("instance");
            List <ServiceInstanceInfo> serviceInstanceInfos = new ArrayList <>();
            for (Object obj : instanceJsonArr) {
                ServiceInstanceInfo serviceInstanceInfo = new ServiceInstanceInfo();
                serviceInstanceInfo.appName = JsonUtils.getValue((JSONObject) obj, "app", String.class);
                serviceInstanceInfo.hostName = JsonUtils.getValue((JSONObject) obj, "hostName", String.class);
                serviceInstanceInfo.ipAddr = JsonUtils.getValue((JSONObject) obj, "ipAddr", String.class);
                JSONObject portJsonObj = JsonUtils.getValue((JSONObject) obj, "port", JSONObject.class);
                serviceInstanceInfo.port = JsonUtils.getValue(portJsonObj, "$", Integer.class);
                serviceInstanceInfo.metadata = JsonUtils.getValue((JSONObject) obj, "metadata", Map.class);
                serviceInstanceInfo.status = InstanceStatus.toEnum(JsonUtils.getValue((JSONObject) obj, "status", String.class));
                serviceInstanceInfo.instanceId = JsonUtils.getValue((JSONObject) obj, "instanceId", String.class);
                serviceInstanceInfo.uri = JsonUtils.getValue((JSONObject) obj, "homePageUrl", String.class);
                serviceInstanceInfos.add(serviceInstanceInfo);
            }
            microServiceInfo.serviceInstanceInfos = serviceInstanceInfos;
        }
        return microServiceInfo;
    }
}

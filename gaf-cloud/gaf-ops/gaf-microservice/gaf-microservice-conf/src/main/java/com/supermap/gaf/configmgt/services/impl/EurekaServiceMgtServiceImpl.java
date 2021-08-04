/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.configmgt.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.gaf.configmgt.commontypes.MicroServiceInfo;
import com.supermap.gaf.configmgt.commontypes.ServiceInstanceInfo;
import com.supermap.gaf.configmgt.services.EurekaServiceMgtService;
import com.supermap.gaf.configmgt.util.MicroServiceConversion;
import com.supermap.gaf.rest.utils.OKHttpUtil;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: gaf-microservice-mgt
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/08/22
 */
@Service
public class EurekaServiceMgtServiceImpl implements EurekaServiceMgtService {
    private Logger logger = LogUtil.getLocLogger(EurekaServiceMgtServiceImpl.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 获取所有微服务信息
     *
     * @return
     */
    @Override
    public List<MicroServiceInfo> getAllMicroServices(String eurekaIp, String eurekaPort) {
        List<MicroServiceInfo> microServiceInfos = new ArrayList<>();
        String url = "http://" + eurekaIp + ":" + eurekaPort + "/eureka/apps";
        String responseContent = OKHttpUtil.getUseOkhttp(url);
        JSONObject applicatoinsJsonObj = (JSONObject) JSONObject.parseObject(responseContent).get("applications");
        if (applicatoinsJsonObj != null) {
            JSONArray applicationJsonArr = (JSONArray) applicatoinsJsonObj.get("application");
            for (Object o : applicationJsonArr) {
                //
                MicroServiceInfo microServiceInfo = MicroServiceConversion.getMicroServiceInfo(JSONObject.toJSONString(o));
                microServiceInfos.add(microServiceInfo);
            }
        }
        return microServiceInfos;
    }

    /**
     * 根据服务名获取服务信息
     *
     * @param eurekaIp
     * @param eurekaPort
     * @param serviceName
     * @return
     */
    @Override
    public MicroServiceInfo getMicroService(String eurekaIp, String eurekaPort, String serviceName) {
        MicroServiceInfo microServiceInfo = new MicroServiceInfo();
        String url = "http://" + eurekaIp + ":" + eurekaPort + "/eureka/apps/" + serviceName;
        String responseContent = OKHttpUtil.getUseOkhttp(url);
        microServiceInfo = MicroServiceConversion.getMicroServiceInfo(responseContent);
        return microServiceInfo;
    }

    /**
     * 获取所有微服务信息
     *
     * @return
     */
    @Override
    public List<MicroServiceInfo> getAllMicroServices() {
        List<String> services = discoveryClient.getServices();
        List<MicroServiceInfo> microServiceInfos = new ArrayList<>();
        for (String serviceName : services) {
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
            List<ServiceInstanceInfo> serviceInstanceInfos = MicroServiceConversion.getMicroServiceInfo(serviceInstances);
            microServiceInfos.add(new MicroServiceInfo(serviceName, serviceInstanceInfos));
        }
        return microServiceInfos;
    }

    /**
     * 根据服务名获取服务信息
     *
     * @param serviceName
     * @return
     */
    @Override
    public MicroServiceInfo getMicroService(String serviceName) {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
        List<ServiceInstanceInfo> serviceInstanceInfos = MicroServiceConversion.getMicroServiceInfo(serviceInstances);
        return new MicroServiceInfo(serviceName, serviceInstanceInfos);
    }
}

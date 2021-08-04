/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.configmgt.commontypes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @program: gaf-microservice-mgt
 * @description: 微服务信息
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/08/22
 */
@ApiModel("微服务信息对象")
public class MicroServiceInfo {
    @ApiModelProperty(value = "微服务名称", example = "GAF-XX")
    public String name;
    @ApiModelProperty(value = "微服务实例对象列表", example = "GAF-XX")
    public List<ServiceInstanceInfo> serviceInstanceInfos;

    public MicroServiceInfo(String name, List<ServiceInstanceInfo> serviceInstanceInfos) {
        this.name = name;
        this.serviceInstanceInfos = serviceInstanceInfos;
    }

    public MicroServiceInfo() {
    }
}

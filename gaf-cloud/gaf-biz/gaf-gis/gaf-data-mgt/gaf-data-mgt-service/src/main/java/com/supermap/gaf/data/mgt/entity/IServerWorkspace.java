/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : yd
 * @date:2021/3/25
 * @Date : 2021-3-19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("iserver工作空间")
public class IServerWorkspace implements Serializable {
    @ApiModelProperty("服务类型")
    private String serviceType;
    @ApiModelProperty("工作空间地址")
    private String address;
    @ApiModelProperty("工作空间名称")
    private String name;
    @ApiModelProperty("服务地址是否可达")
    private boolean available;
    @ApiModelProperty("服务名称")
    private String serviceName;
    private String message;
    @ApiModelProperty("服务是否启用")
    private boolean enabled;

}

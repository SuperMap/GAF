/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.gateway;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @program: gaf-service-manager-modules
 * @description: 断言实体
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/07/16
 */
@ApiModel("路由断言自定义对象")
public class GatewayPredicateDefinition {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("断言参数")
    private String args;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public GatewayPredicateDefinition() {
    }

    public GatewayPredicateDefinition(String name, String args) {
        this.name = name;
        this.args = args;
    }
}

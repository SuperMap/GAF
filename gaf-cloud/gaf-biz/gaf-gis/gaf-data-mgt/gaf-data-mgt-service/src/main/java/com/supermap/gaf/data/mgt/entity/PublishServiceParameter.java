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

/**
 * @author:yw
 * @Date 2021-3-12
 * @date:2021/3/25 每个数据源可以发布多种服务
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("服务发布参数")
public class PublishServiceParameter {

    /**
     * 数据源类型，WORKSPACE\HBASE
     */
    @ApiModelProperty("数据源类型")
    private String datasourceType;

    /**
     * 一次发布只能发布一个工作空间
     */
    @ApiModelProperty("工作空间实体类")
    private WorkspaceParameter workspaceParameter;

    /**
     * 一次发布只能发布一个hbase数据源
     */
    @ApiModelProperty("hbase数据源实体类")
    private HBaseParameter hBaseParameter;

    public enum DATASOURCETYPE {
        /**
         * WORKSPACE
         **/
        WORKSPACE,
        /**
         * HBASE
         **/
        HBASE
    }
}

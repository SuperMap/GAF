/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.analysis.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author dqc
 * @date:2021/3/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("空间分析数据连接信息")
public class SpatialAnalysisDataConnInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据库引擎类型，若为hbase时此字段要为空", example = "POSTGRESQL")
    private String engineType;

    @ApiModelProperty(value = "数据库连接url", example = "192.168.192.232:32443")
    private String url;

    @ApiModelProperty(value = "数据库名", example = "gaf")
    private String database;

    @ApiModelProperty(value = "用户名", example = "admin")
    private String username;

    @ApiModelProperty(value = "密码", example = "123456")
    private String password;

    @ApiModelProperty(value = "别名", example = "gaf")
    private String alias;

}

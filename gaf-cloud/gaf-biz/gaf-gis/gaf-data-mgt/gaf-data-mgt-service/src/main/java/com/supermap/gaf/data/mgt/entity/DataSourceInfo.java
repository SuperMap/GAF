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
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("数据源")
public class DataSourceInfo implements Serializable {
    @ApiModelProperty(value = "数据源名称",example = "测试")
    private String dsName;
    @ApiModelProperty(value = "地址",example = "127.0.0.1")
    private String addr;
    @ApiModelProperty(value = "端口",example = "5432")
    private Integer port;
    @ApiModelProperty(value = "数据库名称",example = "test")
    private String dbName;
    @ApiModelProperty(value = "用户名",example = "root")
    private String userName;
    @ApiModelProperty(value = "密码",example = "root")
    private String password;
    @ApiModelProperty(value = "数据源类型。选自数据字典", example = "PostgreSQL",
            allowableValues = "1.文件型-1:udb,2:udbx…\n2.数据库型-1.PostgreSQL,2.MySQL,3.Hbase,4.MongoDB…")
    private String typeCode;
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据源连接信息
 * @date:2021/3/25
 * @author wangxiaolong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("数据源连接信息")
public class DatasourceConnectionInfo {
    /**
     * 参考
     */
    @ApiModelProperty("数据源类别")
    private String typeCode;
    @ApiModelProperty("数据库服务器名、文件名或服务地址")
    private String addr;
    @ApiModelProperty("数据库名")
    private String dbName;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("数据源连接名称")
    private String dsName;
}

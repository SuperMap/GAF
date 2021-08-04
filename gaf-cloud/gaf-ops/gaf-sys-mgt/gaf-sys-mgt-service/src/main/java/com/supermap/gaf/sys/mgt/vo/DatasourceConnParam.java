/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.sys.mgt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * @author:yj
 * @date:2021/3/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("数据源连接参数")
public class DatasourceConnParam {
    @ApiModelProperty("数据库类型枚举，有POSTGRESQL,HBASE,MYSQL,ORACLE,SQL_SERVER")
    private String datasourceType;
    @ApiModelProperty("主机地址")
    private String host;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("数据库名")
    private String databaseOrServiceName;
    @ApiModelProperty("暂时不使用")
    private String schema;
}

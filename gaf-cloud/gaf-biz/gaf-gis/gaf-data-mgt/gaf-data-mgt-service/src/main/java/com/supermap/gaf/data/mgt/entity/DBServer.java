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
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("数据库型数据源连接信息")
public class DBServer {
    @ApiModelProperty(value = "ip地址", example = "127.0.0.1")
    private String ip;
    @ApiModelProperty(value = "端口号", example = "5432")
    private String port;
    @ApiModelProperty(value = "用户名", example = "root")
    private String username;
    @ApiModelProperty(value = "密码", example = "root")
    private String password;
    @ApiModelProperty(value = "类型", example = "POSTGRESQL/MYSQL/ORACLE选一")
    private DBType type;

    /**
     * 别名
     */
    @ApiModelProperty("别名")
    private String name;

    /**
     * 数据库名
     */
    @ApiModelProperty(value = "数据库名", example = "test")
    private String databaseName;

    public enum DBType {
        /**
         * ORACLE数据库
         **/
        ORACLE,
        /**
         * MYSQL数据库
         **/
        MYSQL,
        /**
         * POSTGRESQL数据库
         **/
        POSTGRESQL
    }
}

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

import java.util.List;


/**
* @author:yw
* @Date 2021-3-12
 * @date:2021/3/25
 * 注意hbase现只支持发布地图服务、数据服务
**/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("hbase服务发布的具体参数")
public class HBaseParameter {
    @ApiModelProperty("zookeeper")
    private String zookeeper;
    @ApiModelProperty("catalog")
    private String catalog;

    /**
     * 允许发布成服务的图层，有设置则每个数据集发布为一个单独的服务，为空则直接发布
     */
    @ApiModelProperty("允许发布成服务的图层")
    private List<String> allowDatasetNames;

    /**
     * 一次发布多种服务类型，RESTMAP\RESTDATA
     */
    @ApiModelProperty("一次发布多种服务类型")
    private List<String> serviceTypes;


    /**
     * 样式文件路径，相对与iserver服务磁盘路径
     */
    @ApiModelProperty("样式文件路径")
    private String stylePath;

    /**
     * 是否开启hbase的Kerberos认证
     */
    @ApiModelProperty("是否开启hbase的Kerberos认证")
    private boolean authentication;

    /**
     * Hadoop集群core-site.xml文件路径
     */
    @ApiModelProperty("Hadoop集群core-site.xml文件路径")
    private String coreXml;

    /**
     * HDFS集群hdfs-site.xml文件路径
     */
    @ApiModelProperty("HDFS集群hdfs-site.xml文件路径")
    private String hbaseXml;

    /**
     * HBase集群hbase-site.xml文件路径
     */
    @ApiModelProperty("HBase集群hbase-site.xml文件路径")
    private String hdfsXml;

    /**
     * Kerberos客户端配置文件路径
     */
    @ApiModelProperty("Kerberos客户端配置文件路径")
    private String krb5ConfPath;

    /**
     * 支持的服务类型枚举
     */
    public enum SUPPORTSERVICETYPE {
        /**
        * 地图服务
        **/
        RESTMAP,
        /**
         * 数据服务
         **/
        RESTDATA
    }

}

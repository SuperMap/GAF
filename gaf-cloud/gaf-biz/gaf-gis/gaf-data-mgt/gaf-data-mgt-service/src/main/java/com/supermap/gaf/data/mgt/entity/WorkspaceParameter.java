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
import java.util.Map;
import java.util.Set;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("工作空间服务发布的具体参数")
public class WorkspaceParameter {

    /**
     * 工作空间类型，DB\FILE
     */
    @ApiModelProperty("工作空间类型，DB或者FILE")
    private String workspaceType;

    /**
     * 数据库型数据源连接信息
     */
    @ApiModelProperty("数据库型数据源连接信息")
    private DBServer dbServer;

    /**
     * 文件型数据源连接信息
     */
    @ApiModelProperty("文件型数据源连接信息")
    private FileServer fileServer;

    /**
     * 一个工作空间可一次发布多种服务类型，RESTMAP\RESTDATA\RESTREALSPACE\RESTSPATIALANALYST
     */
    @ApiModelProperty("多种服务类型 RESTMAP\\RESTDATA\\RESTREALSPACE\\RESTSPATIALANALYST")
    private List<String> serviceTypes;

    /**
     * 允许发布成服务的图层
     */
    @ApiModelProperty("允许发布成服务的图层")
    private List<String> allowDatasetNames;

    /**
     * 地图模板地址，得到工作空间后，将地图模板应用到对应的数据集上
     * 此参数只在发布【数据库型数据源且是地图服务】生效，其他情况无效
     * map<datasetName, Set<url>
     */
    @ApiModelProperty("地图模板地址")
    private Map<String, Set<String>> mapTemplates;

    /**
     * 地图服务资源地址，包含点、线、面资源
     * 得到工作空间后将资源加载进去
     * 此参数只在发布【数据库型数据源且是地图服务】生效，其他情况无效
     * map<resourceType, url>
     * key：MarkerLibrary\LineLibrary\FillLibrary
     */
    @ApiModelProperty("地图服务资源地址，包含点、线、面资源")
    private Map<String, String> resources;

    /**
     * 其他参数，备用
     */
    @ApiModelProperty("其他参数")
    private Map<String, String> otherParameter;

    /**
     * 工作空间类型枚举
     */
    public enum WORKSPACETYPE {
        /**
         * 数据库类型
         */
        DB,
        /**
         * 文件类型
         */
        FILE
    }

    /**
     * 支持的服务类型枚举
     */
    public enum SUPPORTSERVICETYPE {
        /**
         * 地图服务
         */
        RESTMAP,
        /**
         * 数据服务
         */
        RESTDATA,
        /**
         * 空间服务
         */
        RESTREALSPACE,
        /**
         *空间分析服务
         */
        RESTSPATIALANALYST
    }
}

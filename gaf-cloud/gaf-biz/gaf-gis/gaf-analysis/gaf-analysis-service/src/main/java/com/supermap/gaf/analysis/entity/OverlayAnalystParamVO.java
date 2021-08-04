/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.analysis.entity;

import com.supermap.services.components.commontypes.DatasourceConnectionInfo;
import com.supermap.services.components.commontypes.Geometry;
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
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel("iobject叠加分析请求参数实体类")
public class OverlayAnalystParamVO implements Serializable {

    private static final long serialVersionUID = -1628992344233081695L;

    @ApiModelProperty("第一数据集数据源链接信息")
    private DatasourceConnectionInfo sourceDatasourceConnectionInfo;
    @ApiModelProperty("第一数据集名称")
    private String sourceDatasetName;
    @ApiModelProperty("第二数据集数据源链接信息")
    private DatasourceConnectionInfo operationDatasourceConnectionInfo;
    @ApiModelProperty("第二数据集名称")
    private String operationDatasetName;
    @ApiModelProperty("叠加几何对象(第二数据集参数没有的时候，充当第二数据集。)")
    private Geometry[] operationGeometries;
    @ApiModelProperty("第三数据集数据源链接信息；如果没有则使用第一数据集数据源链接信息")
    private DatasourceConnectionInfo resultDatasourceConnectionInfo;
    @ApiModelProperty("第三数据集名称；如果没有则随机生成数据集名称RS_为前缀")
    private String resultDatasetName;

    @ApiModelProperty("第一数据集保留字段")
    private String[] sourceRetainedFields;
    @ApiModelProperty("第二数据集保留字段")
    private String[] operationRetainedFields;
    @ApiModelProperty("设置叠加分析的容限值")
    private Double tolerance;
    @ApiModelProperty("叠加操作，可选值为 clip（裁剪）、erase（擦除）、identity（同一）、intersect（相交）、union（合并）、update（更新）、XOR（对称差）。 \n" +
            "其中，对于 clip（裁剪）、erase（擦除）、identity（同一）和 intersect（相交）四种操作，操作数据集支持的数据类型为面数据集，或者面状几何对象数据，被操作数据集的数据类型为点、线、面数据集。 \n" +
            "而对于 union（合并）、update（更新）、XOR（对称差）三种操作，操作数据集支持的数据类型为面数据集，或者面状几何对象数据，被操作数据集的数据类型只能是面数据集")
    private String operation;

    @ApiModelProperty("是否异步分析。为true则直接返回第三数据集的数据源链接信息和数据集名，为false等待分析完成后返回第三数据集的数据源链接信息和数据集名,以及通过分页参数获取到的结果")
    private Boolean asynchronousReturn;


    @ApiModelProperty("分页参数：请求页索引")
    private Integer pageIndex;
    @ApiModelProperty("分页参数：请求页条数")
    private Integer pageSize;


}

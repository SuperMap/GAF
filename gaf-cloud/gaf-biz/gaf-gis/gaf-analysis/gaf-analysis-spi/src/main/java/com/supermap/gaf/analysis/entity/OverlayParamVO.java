/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.analysis.entity;

import com.supermap.services.components.commontypes.Geometry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author dqc
 * @date:2021/3/25
 * @since : 2020-12-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("空间分析")
public class OverlayParamVO implements Serializable {
    private static final long serialVersionUID = -5542098272960180429L;

    @ApiModelProperty("保留字段")
    private List<String> retainedFields;
    @NotNull
    @ApiModelProperty("空间分析类型")
    private String type;
    @NotNull
    @ApiModelProperty("空间分析方法")
    private SpatialAnalysisOperatorEnum operator;
    @ApiModelProperty("geometry集合参数对象，与sourceDataConnInfo互斥")
    private List<Geometry> sourceGeometries;
    @ApiModelProperty("源数据连接信息，与sourceGeometries互斥")
    private SpatialAnalysisDataConnInfo sourceDataConnInfo;
    @ApiModelProperty("源数据集名称")
    private String sourceDataSetName;
    @ApiModelProperty(value = "源数据集过滤条件", example = "BSM=1")
    private String sourceDatasetFilter;
    @ApiModelProperty("目标数据连接信息")
    private SpatialAnalysisDataConnInfo targetDataConnInfo;
    @ApiModelProperty("目标数据集名称")
    private String targetDataSetName;
    @ApiModelProperty(value = "目标数据集过滤条件", example = "BSM=1")
    private String targetDatasetFilter;
    @ApiModelProperty("分析结果数据连接信息")
    private SpatialAnalysisDataConnInfo resultDataConnInfo;
    @ApiModelProperty("分析结果数据集名称")
    private String resultDataSetName;
    @NotNull
    @ApiModelProperty("分析名称")
    private String analysisName;

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.analysis.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 空间分析结果
 * @author  
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("空间分析结果")
public class SpatialAnalysisResult implements Serializable{
    private static final long serialVersionUID = -1464737828581813304L;

    @ApiModelProperty("空间分析结果id")
    private String resultId;
    @ApiModelProperty("分析名称")
    private String analysisName;
    @ApiModelProperty("分析类型")
    private String analysisType;
    @ApiModelProperty("结果连接信息")
    private String resultConnInfo;
    @ApiModelProperty("结果数据集名称")
    private String resultDataSetName;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;
}

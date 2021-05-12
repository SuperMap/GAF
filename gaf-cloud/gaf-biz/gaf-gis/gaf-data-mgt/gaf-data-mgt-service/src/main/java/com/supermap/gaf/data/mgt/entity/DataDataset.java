/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.entity;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据集
 * @author yw 
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("数据集")
public class DataDataset implements Serializable{
    @NotNull
    @ApiModelProperty("数据集id")
    private String datasetId;
    @NotNull
    @ApiModelProperty("数据集名称")
    private String datasetName;
    @NotNull
    @ApiModelProperty("所属数据源")
    private String datasourceId;
    @ApiModelProperty("排序序号")
    private Integer sortSn;
    @ApiModelProperty("分类要素")
    private String categoryFactorCode;
    @ApiModelProperty("是否空间数据")
    @JSONField(name="isSdx")
    private Boolean isSdx;
    @ApiModelProperty("关联地图")
    private String mapServices;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("状态")
    @JSONField(name="isStatus")
    private Boolean status;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;
}
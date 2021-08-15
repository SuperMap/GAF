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

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 字段关联
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("字段关联")
public class MmFieldAssociate implements Serializable{
    @NotNull
    @ApiModelProperty("主键")
    private String fieldAssociateId;
    @NotNull
    @ApiModelProperty("模型id")
    private String modelId;
    @NotNull
    @ApiModelProperty("字段1逻辑表id")
    private String sourceFieldTableId;
    @NotNull
    @ApiModelProperty("字段2逻辑表id")
    private String targetFieldTableId;
    @NotNull
    @ApiModelProperty("字段1字段表id")
    private String sourceFieldId;
    @NotNull
    @ApiModelProperty("字段2字段表id")
    private String targetFieldId;
    /**
    * 默认值1：now()
    */
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("更新时间")
    private Date updatedTime;
    @ApiModelProperty("更新人")
    private String updatedBy;
}
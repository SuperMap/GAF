/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.entity;

import com.supermap.gaf.annotation.SortSnField;
import com.supermap.gaf.annotation.UpdatedTimeField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 模型
 * @author wxl 
 * @since  yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("模型")
public class MmModel implements Serializable{
    @Id
    @NotNull
    @ApiModelProperty("主键")
    private String modelId;
    @NotNull
    @ApiModelProperty("模型名称")
    private String modelName;
    @NotNull
    @ApiModelProperty("模型类型")
    private String modelType;
    @NotNull
    @ApiModelProperty("模型标识")
    private String modelCode;
    @SortSnField
    @ApiModelProperty("排序")
    private Integer sortSn;
    @ApiModelProperty("描述")
    private String description;
    /**
    * 默认值1：now()
    */
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("更新时间")
    private Date updatedTime;
    @UpdatedTimeField
    @ApiModelProperty("更新人")
    private String updatedBy;
}
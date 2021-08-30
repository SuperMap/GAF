/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.supermap.gaf.annotation.ParentIdField;
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
 * 字段
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("字段")
public class MmField implements Serializable{
    @Id
    @NotNull
    @ApiModelProperty("主键")
    private String fieldId;
    @ParentIdField
    @NotNull
    @ApiModelProperty("逻辑表id")
    private String tableId;
    @NotNull
    @ApiModelProperty("字段名称")
    private String fieldName;
    @ApiModelProperty("字段别名")
    private String fieldAlias;
    @NotNull
    @ApiModelProperty("字段类型")
    private String fieldType;
    @ApiModelProperty("字段长度")
    private Integer fieldLength;
    @ApiModelProperty("字段精度")
    private Integer fieldPrecision;
    @ApiModelProperty("小数位")
    private Integer fieldScale;
    @ApiModelProperty("主键排序，1...")
    private Integer pkSeq;
    @ApiModelProperty("字段默认值")
    private String fieldDefault;
    /**
    * 默认值1：false
    */
    @ApiModelProperty("字段是否非空")
    @JSONField(name="isFieldNotNull")
    private Boolean fieldNotNull;
    /**
    * 默认值1：false
    */
    @ApiModelProperty("字段是否是主键")
    @JSONField(name="isFieldPrimaryKey")
    private Boolean fieldPrimaryKey;
    @ApiModelProperty("描述")
    private String description;
    /**
    * 默认值1：now()
    */
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @UpdatedTimeField
    @ApiModelProperty("更新时间")
    private Date updatedTime;
    @ApiModelProperty("更新人")
    private String updatedBy;
    @SortSnField
    @ApiModelProperty("排序")
    private Integer sortSn;
}
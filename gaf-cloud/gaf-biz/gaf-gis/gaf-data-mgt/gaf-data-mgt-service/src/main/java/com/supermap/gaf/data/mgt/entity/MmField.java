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
    @NotNull
    @ApiModelProperty("主键")
    private String fieldId;
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
    @ApiModelProperty("更新时间")
    private Date updatedTime;
    @ApiModelProperty("更新人")
    private String updatedBy;
}
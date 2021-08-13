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
 * 模型
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("模型")
public class MmModel implements Serializable{
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
    @ApiModelProperty("更新人")
    private String updatedBy;
}
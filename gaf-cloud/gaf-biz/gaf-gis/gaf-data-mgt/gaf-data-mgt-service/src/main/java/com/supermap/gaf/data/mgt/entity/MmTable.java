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
 * 逻辑
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("逻辑")
public class MmTable implements Serializable{
    @NotNull
    @ApiModelProperty("主键")
    private String tableId;
    @NotNull
    @ApiModelProperty("逻辑表名称")
    private String tableName;
    @NotNull
    @ApiModelProperty("模型id")
    private String modelId;
    @ApiModelProperty("空间数据源sdx数据集信息")
    private String sdxInfo;
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
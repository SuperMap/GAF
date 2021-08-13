package com.supermap.gaf.data.mgt.entity.vo;

import com.supermap.gaf.data.mgt.entity.MmField;
import com.supermap.gaf.data.mgt.entity.MmFieldAssociate;
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
 * 字段关联VO
 * @author dqc
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("字段关联VO")
public class MmFieldAssociateVO implements Serializable{
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

    /**
     * vo部分
     */
    @NotNull
    @ApiModelProperty("字段1字段表排序")
    private Integer sourceFieldSortSn;
    @NotNull
    @ApiModelProperty("字段2字段表排序")
    private Integer targetFieldSortSn;

    public static MmFieldAssociateVO build(MmFieldAssociate mmFieldAssociate, MmField sourceField, MmField targetField){
        return MmFieldAssociateVO.builder()
                .createdBy(mmFieldAssociate.getCreatedBy())
                .createdTime(mmFieldAssociate.getCreatedTime())
                .fieldAssociateId(mmFieldAssociate.getFieldAssociateId())
                .modelId(mmFieldAssociate.getModelId())
                .sourceFieldId(mmFieldAssociate.getSourceFieldId())
                .sourceFieldTableId(mmFieldAssociate.getSourceFieldTableId())
                .targetFieldId(mmFieldAssociate.getTargetFieldId())
                .targetFieldTableId(mmFieldAssociate.getTargetFieldTableId())
                .updatedBy(mmFieldAssociate.getUpdatedBy())
                .updatedTime(mmFieldAssociate.getUpdatedTime())
                .sourceFieldSortSn(sourceField.getSortSn())
                .targetFieldSortSn(targetField.getSortSn())
                .build();
    }

}
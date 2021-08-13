package com.supermap.gaf.data.mgt.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 字段关联VO
 * @author dqc
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("模型画布LinksVO")
public class MmLinksVO implements Serializable{

    @ApiModelProperty("表1id")
    private String source;
    @ApiModelProperty("表2id")
    private String target;
    @ApiModelProperty("字段1索引")
    private Integer sourceFieldIndex;
    @ApiModelProperty("字段2索引")
    private Integer targetFieldIndex;



    public static MmLinksVO build(MmFieldAssociateVO fieldAssociateVO){
        return MmLinksVO.builder()
                .source(fieldAssociateVO.getSourceFieldTableId())
                .target(fieldAssociateVO.getTargetFieldTableId())
                .sourceFieldIndex(fieldAssociateVO.getSourceFieldSortSn())
                .targetFieldIndex(fieldAssociateVO.getTargetFieldSortSn())
                .build();
    }

}
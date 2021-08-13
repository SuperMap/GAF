package com.supermap.gaf.data.mgt.entity.vo;

import com.supermap.gaf.data.mgt.entity.MmField;
import com.supermap.gaf.data.mgt.entity.MmTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 逻辑表VO
 * @author dqc
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("逻辑表VO")
public class MmTableVO implements Serializable{
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


    /**
     * vo部分
     */
    @ApiModelProperty("表下所有字段PO列表")
    private List<MmField> fields;


    /**
     * 设置MmTable的对应属性
     * @param mmTable
     * @return
     */
    public static MmTableVO build(MmTable mmTable,List<MmField> fields){
        fields = fields.stream().sorted(Comparator.comparingInt(MmField::getSortSn)).collect(Collectors.toList());
        return MmTableVO.builder()
                .createdBy(mmTable.getCreatedBy())
                .createdTime(mmTable.getCreatedTime())
                .description(mmTable.getDescription())
                .modelId(mmTable.getModelId())
                .sdxInfo(mmTable.getSdxInfo())
                .sortSn(mmTable.getSortSn())
                .tableId(mmTable.getTableId())
                .tableName(mmTable.getTableName())
                .updatedTime(mmTable.getUpdatedTime())
                .updatedBy(mmTable.getUpdatedBy())
                .fields(fields)
                .build();
    }
}
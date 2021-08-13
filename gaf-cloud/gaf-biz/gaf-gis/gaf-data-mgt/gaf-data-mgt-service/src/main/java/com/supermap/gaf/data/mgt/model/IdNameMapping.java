package com.supermap.gaf.data.mgt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 逻辑表id和物理表名 映射
 * @author wxl
 * @since 2021/8/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("逻辑表id和物理表名映射")
public class IdNameMapping {
    @ApiModelProperty("逻辑表id")
    String logicTableId;
    @ApiModelProperty("物理表名")
    String physicsName;
}

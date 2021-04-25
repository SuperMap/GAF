/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.analysis.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @date:2021/3/25
 * @author dqc
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel("分析结果图表数据对象")
public class RecordSetAggregationVO implements Serializable {

    private static final long serialVersionUID = -6375480196029153681L;

    @ApiModelProperty("分组字段对应的聚合结果，key为分组字段，value为对应聚合方法的值")
    private Map<String, Double> groupFieldAndValues;

    @ApiModelProperty("分组字段对应的分组结果的smid集合,key为分组字段，value为对应组所有数据的id")
    private Map<String, String[]> groupFieldAndIds;

}

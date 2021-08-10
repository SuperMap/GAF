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
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/5/14 1:52 PM
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@ApiModel("分析结果数据对象")
public class RecordSetVO implements Serializable {
    private static final long serialVersionUID = -4224558432256480277L;

    @ApiModelProperty("字段名称")
    private String[] fields;
    @ApiModelProperty("字段别名")
    private String[] captions;
    @ApiModelProperty("结果数据集")
    private String[][] values;

    @ApiModelProperty("总数据条数")
    private Integer total;

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author:yd
 * @date:2021/3/25
 * @Date 2021-3-01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("数据集模型")
public class GDataset {

    @ApiModelProperty("数据集名称")
    private String datasetName;

    @ApiModelProperty("数据集类型")
    private String datasetType;

    @ApiModelProperty("数据集别名")
    private String caption;

}

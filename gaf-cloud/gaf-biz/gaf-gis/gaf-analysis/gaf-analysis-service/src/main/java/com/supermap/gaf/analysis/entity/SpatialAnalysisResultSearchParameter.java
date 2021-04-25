/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.analysis.entity;


import com.supermap.gaf.commontypes.SearchParameter;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author dqc
 * @date:2021/3/25
 * 空间分析结果查询参数数据结构
 */
public class SpatialAnalysisResultSearchParameter extends SearchParameter {
    @ApiModelProperty("偏移量")
    private Integer offset;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}

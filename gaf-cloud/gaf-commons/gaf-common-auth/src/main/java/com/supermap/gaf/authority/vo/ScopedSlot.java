/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author:yj
 * @date:2021/3/25 用于前端插槽，为了高亮显示树的搜索结果
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("目录树节点 视图层对象")
public class ScopedSlot implements Serializable {
    @ApiModelProperty("树节点title插槽")
    private String title = "title";
}

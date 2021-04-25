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


/**
 * @author:yj
 * @date:2021/3/25
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("前端选择器可选项")
public class SelectOptionVo {
    @ApiModelProperty(value = "选项值",required=true)
    private Object value;
    @ApiModelProperty(value = "选项显示值",required = true)
    private String label;
    @ApiModelProperty(value = "选项是否禁用")
    private Boolean disabled;
    @ApiModelProperty(value = "选项关键字", notes = "Vue使用的")
    private Object key;
    @ApiModelProperty(value = "标题")
    private String title;
}

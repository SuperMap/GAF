/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wxl
 * @since 2021/8/12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("字段类型信息")
public class FieldTypeInfo {
    @ApiModelProperty("字段类型编码")
    String code;
    @ApiModelProperty("字段类型名")
    String name;
    @ApiModelProperty("字段类型描述")
    String descrption;
}

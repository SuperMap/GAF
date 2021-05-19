/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 字典类别
 * @author wangxiaolong
 * @date 2021-01-08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("字典类别")
public class DictType  implements Serializable {

    @NotNull
    @ApiModelProperty("字典id")
    private String dictTypeId;
    @ApiModelProperty("字典类别编码")
    private String dictTypeCode;
    @NotNull
    @ApiModelProperty("字典类别名称")
    private String dictTypeName;
    @ApiModelProperty("同级序号")
    private Integer seq;
    @ApiModelProperty("描述")
    private String dictTypeDesc;
    @ApiModelProperty("字典类别目录")
    private String catalogId;
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.sys.mgt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 字典数据
 *
 * @author wangxiaolong
 * @date 2021-01-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("字典数据")
public class DictData implements Serializable {

    @NotNull
    @ApiModelProperty("字典数据唯一键，即字典数据id")
    private String key;
    @NotNull
    @ApiModelProperty("字典类别编码")
    private String dictTypeCode;
    @NotNull
    @ApiModelProperty("字典名称")
    private String label;
    @ApiModelProperty("字典值")
    private String value;
    @ApiModelProperty("同级序号")
    private Integer seq;
    @ApiModelProperty("可见性")
    private Boolean visibility;
    @ApiModelProperty("描述")
    private String dictDesc;
    @ApiModelProperty("扩展属性。自定义属性，json格式")
    private String extProperties;
}

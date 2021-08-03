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

import javax.validation.constraints.NotNull;

/**
 * @auther : yd
 * @author:yj
 * @date:2021/3/25
 * @since : 2020-12-02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("第三方资源映射vo")
public class P3SyncVo {

    @NotNull
    @ApiModelProperty("规则标识")
    private String ruleId;

    @ApiModelProperty("模糊查询值")
    private String search;

    @ApiModelProperty("gaf原始资源id")
    private String gafResourceId;

}

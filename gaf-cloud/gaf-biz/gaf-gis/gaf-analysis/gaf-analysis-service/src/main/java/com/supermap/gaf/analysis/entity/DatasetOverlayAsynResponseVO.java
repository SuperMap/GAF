/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.analysis.entity;

import com.supermap.services.rest.PostResultType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author dqc
 * @date:2021/3/25
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel("数据集叠加分析响应实体类,异步返回标识")
public class DatasetOverlayAsynResponseVO implements Serializable {

    private static final long serialVersionUID = -5193367055599246166L;

    @ApiModelProperty("查询是否成功")
    private Boolean succeed;
    @ApiModelProperty("分析结果资源的 ID。")
    private String newResourceID;
    @ApiModelProperty("POST 请求的结果类型，枚举说明 POST 请求对目标资源的影响，即处理结果是什么样的。\n")
    private PostResultType postResultType;
    @ApiModelProperty("创建的新资源的 URI。")
    private String newResourceLocation;

}

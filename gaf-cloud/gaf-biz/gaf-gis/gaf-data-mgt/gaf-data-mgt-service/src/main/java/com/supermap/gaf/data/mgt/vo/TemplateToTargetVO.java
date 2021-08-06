/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 空间数据模板id和目标数据源id
 * 用于从模板创建数据源源
 * @author wxl
 * @since 2021/7/27
 */
@Data
@ApiModel(value = "空间数据模板id和目标数据源id",description = "空间数据源也是空间模板")
public class TemplateToTargetVO {
    @ApiModelProperty("空间数据模板id")
    private String templateId;
    @ApiModelProperty("目标数据源id")
    private String targetId;
}

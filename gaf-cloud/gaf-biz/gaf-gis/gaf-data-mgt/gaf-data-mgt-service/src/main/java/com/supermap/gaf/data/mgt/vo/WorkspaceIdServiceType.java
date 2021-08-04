/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 工作空间id和发布服务的类型集合
 *
 * @author wxl
 * @date:2021/3/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("工作空间id和可支持发布的服务类型集合")
public class WorkspaceIdServiceType {

    @ApiModelProperty("工作空间id")
    private String workspaceId;

    @ApiModelProperty("服务类型集合")
    private List<String> ServiceTypes;

}

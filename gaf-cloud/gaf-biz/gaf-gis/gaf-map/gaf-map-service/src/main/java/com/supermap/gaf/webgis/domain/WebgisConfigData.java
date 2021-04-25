/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @date:2021/3/25
 * @author heykb
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("应用配置")
public class WebgisConfigData {
    @ApiModelProperty("水平工具条配置")
    private List<Object> horizontalToolbars;
    @ApiModelProperty("垂直工具条配置")
    private List<Object> verticalToolbars;
    @ApiModelProperty("资源目录树")
    private Map<String,Object> resourceTree;
    @ApiModelProperty("多地图配置")
    private List bottomLayers;
    @ApiModelProperty("地位配置")
    private Map<String,Object> location;
    @ApiModelProperty("天地图token")
    private String token;
    private long timestamp;
}

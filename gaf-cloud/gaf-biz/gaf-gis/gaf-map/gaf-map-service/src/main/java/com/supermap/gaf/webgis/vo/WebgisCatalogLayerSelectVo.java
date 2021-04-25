/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;
import javax.validation.constraints.*;
import com.alibaba.fastjson.annotation.JSONField;

import javax.ws.rs.QueryParam;

/**
* 图层 条件查询实体
 * @date:2021/3/25
* @author wangxiaolong
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("图层 条件查询实体")
public class WebgisCatalogLayerSelectVo {
	@QueryParam("searchFieldName")
	@ApiModelProperty("查询字段名")
    private String searchFieldName;
	@QueryParam("searchFieldValue")
	@ApiModelProperty("查询字段值")
    private String searchFieldValue;
	@QueryParam("orderFieldName")
	@ApiModelProperty("排序字段名")
    private String orderFieldName;
	@QueryParam("orderMethod")
	@ApiModelProperty("排序方法")
    private String orderMethod;
	@QueryParam("layerCatalogId")
	@ApiModelProperty("图层目录id")
    private String layerCatalogId;
}

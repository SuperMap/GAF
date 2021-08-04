/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.vo;

import com.supermap.gaf.validator.StringRange;
import com.supermap.gaf.webgis.entity.WebgisService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * GIS服务 条件查询实体
 *
 * @author wangxiaolong
 * @date:2021/3/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("GIS服务 条件查询实体")
public class WebgisServiceConditonVo {
    @QueryParam("searchFieldName")
    @ApiModelProperty("查询字段名")
    @StringRange(entityClass = WebgisService.class, message = "不在指定的字段名范围内")
    private String searchFieldName;
    @QueryParam("searchFieldValue")
    @ApiModelProperty("查询字段值")
    private String searchFieldValue;
    @QueryParam("orderFieldName")
    @ApiModelProperty("排序字段名")
    @StringRange(entityClass = WebgisService.class, message = "不在指定的字段名范围内")
    private String orderFieldName;
    @QueryParam("types")
    @ApiModelProperty("服务类型集合")
    private List<String> types;
    @QueryParam("orderMethod")
    @ApiModelProperty("排序方法")
    @StringRange(value = {"asc", "desc"}, message = "不在指定的范围[asc,desc]内")
    private String orderMethod;
}

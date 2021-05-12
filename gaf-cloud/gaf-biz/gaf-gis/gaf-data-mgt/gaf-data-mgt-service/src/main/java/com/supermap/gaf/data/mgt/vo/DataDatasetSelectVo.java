/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.util.Date;

/**
 * 数据集 条件查询实体
 * @author yw
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("数据集 条件查询实体")
public class DataDatasetSelectVo {
        @QueryParam("searchFieldName")
        @ApiModelProperty("查询字段名")
        private String searchFieldName;
        @QueryParam("searchFieldValue")
        @ApiModelProperty("查询字段值")
        private String searchFieldValue;
        @QueryParam("equalFieldName")
        @ApiModelProperty("等值查询字段名")
        private String equalFieldName;
        @QueryParam("equalFieldValue")
        @ApiModelProperty("等值查询字段值")
        private String equalFieldValue;
        @QueryParam("orderFieldName")
        @ApiModelProperty("排序字段名")
        private String orderFieldName;
        @QueryParam("orderMethod")
        @ApiModelProperty("排序方法")
        private String orderMethod;
        @QueryParam("datasetId")
        @ApiModelProperty("数据集id")
        private String datasetId;
        @QueryParam("datasetName")
        @ApiModelProperty("数据集名称")
        private String datasetName;
        @QueryParam("datasourceId")
        @ApiModelProperty("所属数据源")
        private String datasourceId;
        @QueryParam("sortSn")
        @ApiModelProperty("排序序号")
        private Integer sortSn;
        @QueryParam("categoryFactorCode")
        @ApiModelProperty("分类要素")
        private String categoryFactorCode;
        @QueryParam("isSdx")
        @ApiModelProperty("是否空间数据")
        private Boolean isSdx;
        @QueryParam("mapServices")
        @ApiModelProperty("关联地图")
        private String mapServices;
        @QueryParam("description")
        @ApiModelProperty("描述")
        private String description;
        @QueryParam("status")
        @ApiModelProperty("状态")
        private Boolean status;
        @QueryParam("createdTime")
        @ApiModelProperty("创建时间")
        private Date createdTime;
        @QueryParam("createdBy")
        @ApiModelProperty("创建人")
        private String createdBy;
        @QueryParam("updatedTime")
        @ApiModelProperty("修改时间")
        private Date updatedTime;
        @QueryParam("updatedBy")
        @ApiModelProperty("修改人")
        private String updatedBy;
}

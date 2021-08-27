/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.vo;

import com.supermap.gaf.data.mgt.entity.MmModel;
import com.supermap.gaf.validator.StringRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.util.Date;

/**
 * 模型 条件查询实体
 * @author wxl
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("模型 条件查询实体")
public class MmModelSelectVo {
        @StringRange(entityClass = MmModel.class, message = "不在指定的字段名范围内")
        @QueryParam("searchFieldName")
        @ApiModelProperty("查询字段名")
        private String searchFieldName;
        @QueryParam("searchFieldValue")
        @ApiModelProperty("查询字段值")
        private String searchFieldValue;
        @StringRange(entityClass = MmModel.class, message = "不在指定的字段名范围内")
        @QueryParam("equalFieldName")
        @ApiModelProperty("等值查询字段名")
        private String equalFieldName;
        @QueryParam("equalFieldValue")
        @ApiModelProperty("等值查询字段值")
        private String equalFieldValue;
        @QueryParam("orderFieldName")
        @StringRange(entityClass = MmModel.class, message = "不在指定的字段名范围内")
        @ApiModelProperty("排序字段名")
        private String orderFieldName;
        @QueryParam("orderMethod")
        @StringRange(value = {"asc", "desc"}, message = "不在指定的范围[asc,desc]内")
        @ApiModelProperty("排序方法")
        private String orderMethod;
        @QueryParam("modelId")
        @ApiModelProperty("主键")
        private String modelId;
        @QueryParam("modelName")
        @ApiModelProperty("模型名称")
        private String modelName;
        @QueryParam("modelType")
        @ApiModelProperty("模型类型")
        private String modelType;
        @QueryParam("modelCode")
        @ApiModelProperty("模型标识")
        private String modelCode;
        @QueryParam("sortSn")
        @ApiModelProperty("排序")
        private Integer sortSn;
        @QueryParam("description")
        @ApiModelProperty("描述")
        private String description;
        @QueryParam("createdTime")
        @ApiModelProperty("创建时间")
        private Date createdTime;
        @QueryParam("createdBy")
        @ApiModelProperty("创建人")
        private String createdBy;
        @QueryParam("updatedTime")
        @ApiModelProperty("更新时间")
        private Date updatedTime;
        @QueryParam("updatedBy")
        @ApiModelProperty("更新人")
        private String updatedBy;
}

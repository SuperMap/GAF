/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.vo;

import com.supermap.gaf.data.mgt.entity.MmField;
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
 * 字段 条件查询实体
 * @author wxl
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("字段 条件查询实体")
public class MmFieldSelectVo {
        @StringRange(entityClass = MmField.class, message = "不在指定的字段名范围内")
        @QueryParam("searchFieldName")
        @ApiModelProperty("查询字段名")
        private String searchFieldName;
        @QueryParam("searchFieldValue")
        @ApiModelProperty("查询字段值")
        private String searchFieldValue;
        @StringRange(entityClass = MmField.class, message = "不在指定的字段名范围内")
        @QueryParam("equalFieldName")
        @ApiModelProperty("等值查询字段名")
        private String equalFieldName;
        @QueryParam("equalFieldValue")
        @ApiModelProperty("等值查询字段值")
        private String equalFieldValue;
        @StringRange(entityClass = MmField.class, message = "不在指定的字段名范围内")
        @QueryParam("orderFieldName")
        @ApiModelProperty("排序字段名")
        private String orderFieldName;
        @StringRange(value = {"asc", "desc"}, message = "不在指定的范围[asc,desc]内")
        @QueryParam("orderMethod")
        @ApiModelProperty("排序方法")
        private String orderMethod;
        @QueryParam("fieldId")
        @ApiModelProperty("主键")
        private String fieldId;
        @QueryParam("tableId")
        @ApiModelProperty("逻辑表id")
        private String tableId;
        @QueryParam("fieldName")
        @ApiModelProperty("字段名称")
        private String fieldName;
        @QueryParam("fieldAlias")
        @ApiModelProperty("字段别名")
        private String fieldAlias;
        @QueryParam("fieldType")
        @ApiModelProperty("字段类型")
        private String fieldType;
        @QueryParam("fieldLength")
        @ApiModelProperty("字段长度")
        private Integer fieldLength;
        @QueryParam("fieldPrecision")
        @ApiModelProperty("字段精度")
        private Integer fieldPrecision;
        @QueryParam("fieldDefault")
        @ApiModelProperty("字段默认值")
        private String fieldDefault;
        @QueryParam("fieldNotNull")
        @ApiModelProperty("字段是否非空")
        private Boolean fieldNotNull;
        @QueryParam("fieldPrimaryKey")
        @ApiModelProperty("字段是否是主键")
        private Boolean fieldPrimaryKey;
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

        @ApiModelProperty("排序")
        private Integer sortSn;
}

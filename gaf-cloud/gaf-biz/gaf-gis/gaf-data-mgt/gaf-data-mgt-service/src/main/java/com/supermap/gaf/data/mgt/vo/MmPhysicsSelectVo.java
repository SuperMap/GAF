/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.vo;

import com.supermap.gaf.data.mgt.entity.MmPhysics;
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
 * 物理 条件查询实体
 * @author wxl
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("物理 条件查询实体")
public class MmPhysicsSelectVo {
        @StringRange(entityClass = MmPhysics.class, message = "不在指定的字段名范围内")
        @QueryParam("searchFieldName")
        @ApiModelProperty("查询字段名")
        private String searchFieldName;
        @QueryParam("searchFieldValue")
        @ApiModelProperty("查询字段值")
        private String searchFieldValue;
        @StringRange(entityClass = MmPhysics.class, message = "不在指定的字段名范围内")
        @QueryParam("equalFieldName")
        @ApiModelProperty("等值查询字段名")
        private String equalFieldName;
        @QueryParam("equalFieldValue")
        @ApiModelProperty("等值查询字段值")
        private String equalFieldValue;
        @StringRange(entityClass = MmPhysics.class, message = "不在指定的字段名范围内")
        @QueryParam("orderFieldName")
        @ApiModelProperty("排序字段名")
        private String orderFieldName;
        @StringRange(value = {"asc", "desc"}, message = "不在指定的范围[asc,desc]内")
        @QueryParam("orderMethod")
        @ApiModelProperty("排序方法")
        private String orderMethod;
        @QueryParam("physicsId")
        @ApiModelProperty("主键")
        private String physicsId;
        @QueryParam("physicsName")
        @ApiModelProperty("物理表名")
        private String physicsName;
        @QueryParam("tableId")
        @ApiModelProperty("逻辑表id")
        private String tableId;
        @QueryParam("server")
        @ApiModelProperty("数据库地址")
        private String server;
        @QueryParam("dbName")
        @ApiModelProperty("数据库名")
        private String dbName;
        @QueryParam("username")
        @ApiModelProperty("用户名")
        private String username;
        @QueryParam("password")
        @ApiModelProperty("密码")
        private String password;
        @QueryParam("type")
        @ApiModelProperty("数据源类型")
        private String type;
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

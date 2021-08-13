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
 * 模型 条件查询实体
 * @author wxl
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("模型 条件查询实体")
public class MmModelSelectVo {
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

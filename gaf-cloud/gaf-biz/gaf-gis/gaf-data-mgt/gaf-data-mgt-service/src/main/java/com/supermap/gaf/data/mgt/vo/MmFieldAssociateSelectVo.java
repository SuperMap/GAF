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
 * 字段关联 条件查询实体
 * @author wxl
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("字段关联 条件查询实体")
public class MmFieldAssociateSelectVo {
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
        @QueryParam("fieldAssociateId")
        @ApiModelProperty("主键")
        private String fieldAssociateId;
        @QueryParam("modelId")
        @ApiModelProperty("模型id")
        private String modelId;
        @QueryParam("field1TableId")
        @ApiModelProperty("字段1逻辑表id")
        private String field1TableId;
        @QueryParam("field2TableId")
        @ApiModelProperty("字段2逻辑表id")
        private String field2TableId;
        @QueryParam("field1Id")
        @ApiModelProperty("字段1字段表id")
        private String field1Id;
        @QueryParam("field2Id")
        @ApiModelProperty("字段2字段表id")
        private String field2Id;
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

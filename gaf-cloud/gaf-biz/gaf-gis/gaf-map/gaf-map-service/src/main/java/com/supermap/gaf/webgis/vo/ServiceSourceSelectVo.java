package com.supermap.gaf.webgis.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;

/**
 * 服务来源关联表 条件查询实体
 * @author zrc
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("服务来源关联表条件查询实体")
public class ServiceSourceSelectVo {
    @QueryParam("searchFieldName")
    @ApiModelProperty("模糊查询字段名")
    private String searchFieldName;
    @QueryParam("searchFieldValue")
    @ApiModelProperty("模糊查询字段值")
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
    @QueryParam("serviceSourceId")
    @ApiModelProperty("服务来源关联记录id")
    private String serviceSourceId;
    @QueryParam("sourceId")
    @ApiModelProperty("来源id")
    private String sourceId;
    @QueryParam("serviceId")
    @ApiModelProperty("服务id")
    private String serviceId;
    @QueryParam("sourceType")
    @ApiModelProperty("来源类型。1:工作空间；2:瓦片")
    private Integer sourceType;
}
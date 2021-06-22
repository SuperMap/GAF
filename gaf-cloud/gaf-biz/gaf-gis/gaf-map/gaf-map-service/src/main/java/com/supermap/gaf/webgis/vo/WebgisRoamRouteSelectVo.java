package com.supermap.gaf.webgis.vo;

import com.supermap.gaf.validator.StringRange;
import com.supermap.gaf.webgis.entity.WebgisRoamRoute;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.QueryParam;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 漫游路线 条件查询实体
 *
 * @author wangxiaolong
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("漫游路线 条件查询实体")
public class WebgisRoamRouteSelectVo {
    @QueryParam("searchFieldName")
    @ApiModelProperty("查询字段名")
    @StringRange(entityClass = WebgisRoamRoute.class, message = "不在指定的字段名范围内")
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
    @StringRange(entityClass = WebgisRoamRoute.class, message = "不在指定的字段名范围内")
    private String orderFieldName;
    @QueryParam("orderMethod")
    @ApiModelProperty("排序方法")
    @StringRange(value = {"asc", "desc"}, message = "不在指定的范围[asc,desc]内")
    private String orderMethod;
    @QueryParam("gisRoamRouteId")
    @ApiModelProperty("漫游路线id")
    private String gisRoamRouteId;
    @QueryParam("gisAppId")
    @ApiModelProperty("地图应用")
    private String gisAppId;
    @QueryParam("userId")
    @ApiModelProperty("用户")
    private String userId;
    @QueryParam("wayName")
    @ApiModelProperty("名称")
    private String name;
    @QueryParam("speed")
    @ApiModelProperty("速度")
    private BigDecimal speed;
    @QueryParam("fpfPath")
    @ApiModelProperty("模板文件路径")
    private String fpfPath;
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

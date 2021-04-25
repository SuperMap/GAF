        package com.supermap.gaf.webgis.vo;

        import io.swagger.annotations.ApiModel;
        import io.swagger.annotations.ApiModelProperty;
        import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import javax.ws.rs.QueryParam;
        import java.util.Date;

/**
 * 漫游站点 条件查询实体
 * @author wangxiaolong
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("漫游站点 条件查询实体")
public class WebgisRoamStopSelectVo {
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
        @QueryParam("gisRoamStopId")
        @ApiModelProperty("漫游点id")
        private String gisRoamStopId;
        @QueryParam("name")
        @ApiModelProperty("站点名称")
        private String name;
        @QueryParam("gisRoamRouteId")
        @ApiModelProperty("漫游路线")
        private String gisRoamRouteId;
        @QueryParam("speed")
        @ApiModelProperty("站点速度")
        private Integer speed;
        @QueryParam("usemyspeed")
        @ApiModelProperty("是否使用站点速度")
        private Boolean usemyspeed;
        @QueryParam("longitude")
        @ApiModelProperty("相机经度")
        private Integer longitude;
        @QueryParam("latitude")
        @ApiModelProperty("相机纬度")
        private Integer latitude;
        @QueryParam("altitude")
        @ApiModelProperty("相机高度")
        private Integer altitude;
        @QueryParam("altitudemode")
        @ApiModelProperty("高度模式")
        private Integer altitudemode;
        @QueryParam("heading")
        @ApiModelProperty("相机方位角")
        private Integer heading;
        @QueryParam("tilt")
        @ApiModelProperty("相机俯仰角")
        private Integer tilt;
        @QueryParam("sightCenterX")
        @ApiModelProperty("视线中心点位置x")
        private Integer sightCenterX;
        @QueryParam("sightCenterY")
        @ApiModelProperty("视线中心点位置y")
        private Integer sightCenterY;
        @QueryParam("sightCenterZ")
        @ApiModelProperty("视线中心点位置z")
        private Integer sightCenterZ;
        @QueryParam("sortSn")
        @ApiModelProperty("排序序号")
        private Integer sortSn;
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

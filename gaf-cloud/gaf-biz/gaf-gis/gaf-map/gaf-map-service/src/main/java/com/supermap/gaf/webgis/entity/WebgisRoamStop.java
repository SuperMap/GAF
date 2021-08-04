/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 漫游站点
 *
 * @author wangxiaolong
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("漫游站点")
public class WebgisRoamStop implements Serializable {
    @NotNull
    @ApiModelProperty("漫游点id")
    private String gisRoamStopId;
    @NotNull
    @ApiModelProperty("站点名称")
    private String name;
    @NotNull
    @ApiModelProperty("漫游路线")
    private String gisRoamRouteId;
    @ApiModelProperty("站点速度")
    private Double speed;
    /**
     * 默认值1：false
     */
    @ApiModelProperty("是否使用站点速度")
    private Boolean usemyspeed;
    @ApiModelProperty("相机经度")
    private Double longitude;
    @ApiModelProperty("相机纬度")
    private Double latitude;
    @ApiModelProperty("相机高度")
    private Double altitude;
    @ApiModelProperty("高度模式")
    private String altitudemode;
    @ApiModelProperty("相机方位角")
    private Double heading;
    @ApiModelProperty("相机俯仰角")
    private Double tilt;
    @ApiModelProperty("视线中心点位置x")
    private Double sightCenterX;
    @ApiModelProperty("视线中心点位置y")
    private Double sightCenterY;
    @ApiModelProperty("视线中心点位置z")
    private Double sightCenterZ;
    @ApiModelProperty("排序序号")
    private Integer sortSn;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("状态")
    private Boolean status;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;
}

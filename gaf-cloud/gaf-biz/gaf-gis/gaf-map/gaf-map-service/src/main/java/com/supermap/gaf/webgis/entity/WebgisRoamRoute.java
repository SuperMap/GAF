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
 * 漫游路线
 * @author wangxiaolong 
 * @date 2021-2-24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("漫游路线")
public class WebgisRoamRoute implements Serializable{
    @NotNull
    @ApiModelProperty("漫游路线id")
    private String gisRoamRouteId;
    @ApiModelProperty("地图应用")
    private String gisAppId;
    @ApiModelProperty("用户")
    private String userId;
    @NotNull
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("速度")
    private Double speed;
    @ApiModelProperty("模板文件路径")
    private String fpfPath;
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

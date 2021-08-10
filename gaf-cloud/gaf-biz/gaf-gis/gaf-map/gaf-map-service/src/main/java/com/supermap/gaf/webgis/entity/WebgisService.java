/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.entity;

import com.supermap.gaf.annotation.ConfigName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import javax.validation.constraints.*;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * GIS服务
 *
 * @author wangxiaolong
 * @date 2020-12-05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("GIS服务")
public class WebgisService implements Serializable {
    @NotNull
    @ApiModelProperty("GIS服务id")
    @ConfigName("resourceId")
    private String gisServiceId;
    @NotNull
    @ApiModelProperty("名称")
    @ConfigName("resourceName")
    private String name;
    @ApiModelProperty("英文名称")
    private String nameEn;
    @ApiModelProperty("服务类别")
    @ConfigName({"resourceTag", "sourceType"})
    private String typeCode;
    @ApiModelProperty("API资源")
    private String resourceApiId;
    @ConfigName("resourceLocation")
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("时态")
    private Date timeAttribute;
    @ApiModelProperty("扩展属性。自定义属性,json,数据只加一层而不加多层，接口读出转为json时平铺放到服务属性中去")
    private String moreProperties;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("状态")
    @JSONField(name = "isStatus")
    private Boolean status;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;
    @ApiModelProperty("")
    @ConfigName("serviceType")
    private String tiandituServiceType;
    @ApiModelProperty("行政区划。行政区划编码，从数据字典选")
    private String regionCode;
    @ConfigName(value = "options", toJson = true)
    @ApiModelProperty("显示属性。json:{[...]}二维地图服务，设置最小以及最大显示级别,[{minimumTerrainLevel:5,maximumTerrainLevel:7}];三维地图服务，设置最小以及最大显示相机高度,[{name: 现状建筑@三维转换成果 ，visibleDistanceMin:0,visibleDistanceMax:2000}...]")
    private String displayAttrs;
}

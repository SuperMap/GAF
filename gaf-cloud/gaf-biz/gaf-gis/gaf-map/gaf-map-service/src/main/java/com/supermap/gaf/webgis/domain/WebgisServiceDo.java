/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.supermap.gaf.annotation.ConfigName;
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
 * GIS服务
 * @author wangxiaolong
 * @date 2020-12-05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("GIS服务 带关联表的id")
public class WebgisServiceDo implements Serializable {
    @NotNull
    @ApiModelProperty("GIS服务关联id")
    private String gisServiceAssocId;
    @NotNull
    @ApiModelProperty("GIS服务id")
    @ConfigName("resourceId")
    private String gisServiceId;
    @NotNull
    @ApiModelProperty("名称")
    @ConfigName("title")
    private String name;
    @ApiModelProperty("英文名称")
    private String nameEn;
    @ApiModelProperty("服务类别")
    private String typeCode;
    @ApiModelProperty("API资源")
    private String resourceApiId;
    @ApiModelProperty("地址")
    @ConfigName("url")
    private String address;
    @ApiModelProperty("时态")
    private Date timeAttribute;
    @ApiModelProperty("更多属性")
    private String moreProperties;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("状态")
    @JSONField(name="isStatus")
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
    private String tiandituServiceType;
    private String regionCode;
    @ApiModelProperty("显示属性。json:{[...]}二维地图服务，设置最小以及最大显示级别,[{minimumTerrainLevel:5,maximumTerrainLevel:7}];三维地图服务，设置最小以及最大显示相机高度,[{name: 现状建筑@三维转换成果 ，visibleDistanceMin:0,visibleDistanceMax:2000}...]")
    private String displayAttrs;
}

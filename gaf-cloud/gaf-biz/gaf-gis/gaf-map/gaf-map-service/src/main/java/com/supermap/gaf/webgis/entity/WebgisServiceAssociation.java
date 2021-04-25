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
import java.util.*;
import javax.validation.constraints.*;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * GIS服务关联
 * @author wangxiaolong 
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("GIS服务关联")
public class WebgisServiceAssociation implements Serializable{
    @NotNull
    @ApiModelProperty("GIS服务关联id")
    private String gisServiceAssocId;
    @NotNull
    @ApiModelProperty("GIS服务")
    private String gisServiceId;
    @NotNull
    @ApiModelProperty("GIS数据服务id")
    private String gisDataServiceId;
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
}

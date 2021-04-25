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
import javax.ws.rs.QueryParam;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 地图按钮
 * @author zhurongcheng 
 * @date 2020-12-05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("地图按钮")
public class WebgisButton implements Serializable{
    @NotNull
    @ApiModelProperty("按钮id")
    @QueryParam("buttonId")
    private String buttonId;
    @NotNull
    @ApiModelProperty("名称")
    @QueryParam("name")
    private String name;
    @ApiModelProperty("类别")
    private String type;

    @NotNull
    @ApiModelProperty("方法")
    private String method;
    @ApiModelProperty("图标")
    private String icon;
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

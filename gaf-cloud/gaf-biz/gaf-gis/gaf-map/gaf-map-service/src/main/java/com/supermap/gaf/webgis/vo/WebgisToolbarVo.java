/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.supermap.gaf.webgis.entity.WebgisToolbarButton;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工具条
 * @author zhurongcheng 
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("工具条按鈕配置詳情")
public class WebgisToolbarVo implements Serializable{
    @ApiModelProperty("工具条id")
    private String toolbarId;
    @NotNull
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("位置")
    private String toolbarLocation;
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
    @ApiModelProperty("类别")
    private String type;
    @Valid
    @NotNull
    @ApiModelProperty("按钮配置信息列表")
    private List<WebgisToolbarButton> webgisToolbarButtons;
}

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
 * 工具条按钮 带按钮具体信息
 *
 * @author zhurongcheng
 * @date yyyy-mm-dd
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("工具条按钮 带按钮配置信息")
public class WebgisToolbarButtonDo implements Serializable {
    @NotNull
    @ApiModelProperty("应用按钮id")
    private String gisAppButtonId;
    @NotNull
    @ApiModelProperty("工具条按钮id")
    private String toolbarButtonId;
    @NotNull
    @ApiModelProperty("工具条")
    private String toolbarId;
    @NotNull
    @ApiModelProperty("按钮")
    private String buttonId;
    @NotNull
    @ApiModelProperty("名称")
    @ConfigName("title")
    private String name;
    @ApiModelProperty("类别")
    private String type;
    @NotNull
    @ApiModelProperty("方法")
    @ConfigName("name")
    private String method;
    @ApiModelProperty("图标")
    @ConfigName
    private String icon;
    @ConfigName(toJson = true)
    @ApiModelProperty("参数")
    private String params;
    @ApiModelProperty("其它方法")
    @ConfigName(toJson = true)
    private String actions;
    @ApiModelProperty("二次单击关闭")
    @ConfigName
    @JSONField(name = "isToggle")
    private Boolean toggle;
    @ApiModelProperty("关闭其他面板")
    @JSONField(name = "isCloseOtherPanel")
    @ConfigName("closePanel")
    private Boolean closeOtherPanel;
    @ApiModelProperty("更多属性")
    @ConfigName(value = "moreProperties", expand = true)
    private String moreProperties;
    @ApiModelProperty("排序序号")
    private Integer sortSn;
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
    @ConfigName("children")
    @ApiModelProperty("子工具条id。关联webgis_toolbar的一个工具条id.不为空则为一个子工具条，不能是该按钮所属的工具条")
    private String subToolbarId;
}

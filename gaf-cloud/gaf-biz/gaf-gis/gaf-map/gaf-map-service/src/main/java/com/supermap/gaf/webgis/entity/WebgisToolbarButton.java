/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.entity;
import com.supermap.gaf.annotation.LogicDeleteField;
import com.supermap.gaf.annotation.ParentIdField;
import com.supermap.gaf.annotation.SortSnField;
import com.supermap.gaf.annotation.UpdatedTimeField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;
import javax.persistence.Id;
import javax.validation.constraints.*;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 工具条按钮
 * @author zhurongcheng 
 * @date 2020-12-05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("工具条按钮")
public class WebgisToolbarButton implements Serializable{
    @ApiModelProperty("工具条按钮id")
    @Id
    private String toolbarButtonId;
    @ApiModelProperty("工具条")
    @ParentIdField
    private String toolbarId;
    @NotNull
    @ApiModelProperty("按钮")
    private String buttonId;
    @ApiModelProperty("图标")
    private String icon;
    @ApiModelProperty("参数")
    private String params;
    @ApiModelProperty("其它方法")
    private String actions;
    @ApiModelProperty("二次单击关闭")
    @JSONField(name="isToggle")
    private Boolean toggle;
    @ApiModelProperty("关闭其他面板")
    @JSONField(name="isCloseOtherPanel")
    private Boolean closeOtherPanel;
    @ApiModelProperty("更多属性")
    private String moreProperties;
    @SortSnField
    @ApiModelProperty("排序序号")
    private Integer sortSn;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("状态")
    @JSONField(name="isStatus")
    @LogicDeleteField
    private Boolean status;
    @ApiModelProperty("子工具条。关联webgis_toolbar的一个工具条id.不为空则为一个子工具条，不能是该按钮所属的工具条")
    private String subToolbarId;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    @UpdatedTimeField
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;

}

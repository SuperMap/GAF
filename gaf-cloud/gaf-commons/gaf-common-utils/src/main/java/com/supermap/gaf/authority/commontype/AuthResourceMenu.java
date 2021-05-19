/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.commontype;

import com.alibaba.fastjson.annotation.JSONField;
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

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
* 菜单
 * @date:2021/3/25
* @author wxl
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("菜单")
public class AuthResourceMenu implements Serializable {
    @NotNull
    @ApiModelProperty("菜单id")
    @Id
    private String resourceMenuId;
    @NotNull
    @ApiModelProperty("菜单分组id")
    @ParentIdField
    private String menuCatalogId;
    @NotNull
    @ApiModelProperty(value = "显示名称", example = "权限控制")
    private String name;
    @NotNull
    @ApiModelProperty("关联模块。模块id")
    private String resourceModuleId;
    @ApiModelProperty(value = "参数。入口参数和值,para1=value1&para2=value2。菜单实际路径为模块路径加参数(url?params)", example = "para1=value1&para2=value2")
    private String params;
    @ApiModelProperty("图标")
    private String icon;
    @ApiModelProperty(value = "是否可见。true:可见，false:不可见", example = "true", allowableValues = "true,false")
    @JSONField(name="isVisible")
    private Boolean visible;
    @ApiModelProperty("状态。逻辑删除字段")
    @JSONField(name="isStatus")
    @LogicDeleteField
    private Boolean status = true;
    @ApiModelProperty(value = "排序序号", example = "1", allowableValues = "range[1,infinity]")
    @SortSnField
    private Integer sortSn;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @UpdatedTimeField
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;

    // transient
    private AuthResourceModule authResourceModule;
}

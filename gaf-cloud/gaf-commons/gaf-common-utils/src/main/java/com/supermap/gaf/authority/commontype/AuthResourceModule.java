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
import java.util.Date;

/**
 * 模块
 *
 * @author zhm
 * @date:2021/3/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("模块")
public class AuthResourceModule {
    @NotNull
    @ApiModelProperty("模块id")
    @Id
    private String resourceModuleId;
    @NotNull
    @ApiModelProperty("所属组件id")
    private String sysComponentId;
    @NotNull
    @ApiModelProperty("模块分组id")
    @ParentIdField
    private String moduleCatalogId;
    @NotNull
    @ApiModelProperty(value = "名称", example = "模块管理")
    private String name;
    @ApiModelProperty(value = "英文名称", example = "module-manage")
    private String nameEn;
    @ApiModelProperty(value = "类型.1:平台内页面，2：外部页面", example = "1", allowableValues = "1,2")
    private String type;
    @ApiModelProperty(value = "地址。暂时无用", example = "/authority/view/index.html#/ComponentModule")
    private String moduleUrl;
    @ApiModelProperty(value = "打开方式。0:当前窗口(或当前窗口新开tab),1:新窗口打开", example = "0", allowableValues = "0,1")
    private String target;
    @ApiModelProperty("图标")
    private String iconUrl;
    @ApiModelProperty("状态。逻辑删除字段")
    @JSONField(name = "isStatus")
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
    @ApiModelProperty("修改时间")
    @UpdatedTimeField
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;
    @ApiModelProperty(value = "路由路径。表示前端路由路径", example = "/authority/ComponentModule")
    private String path;
}

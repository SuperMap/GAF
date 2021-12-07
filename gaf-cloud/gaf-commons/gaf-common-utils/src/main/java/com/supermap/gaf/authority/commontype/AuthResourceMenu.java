/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.commontype;

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
 *
 * @author wxl
 * @date:2021/3/25
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

    @ApiModelProperty("父菜单id")
    @ParentIdField
    private String parentId;


    @NotNull
    @ApiModelProperty(value = "显示名称", example = "权限控制")
    private String name;

    // todo: 现在应该没啥用了
    //@ApiModelProperty(value = "类型.1:平台内页面，2：外部页面", example = "1", allowableValues = "1,2")
    //private String type;

    @NotNull
    // 前端目前逻辑是，当前窗口打开 使用 下面的前端路由路径path , 新窗口打开使用地址url
    @ApiModelProperty(value = "打开方式。0:当前窗口(或当前窗口新开tab),1:新窗口打开", example = "0", allowableValues = "0,1")
    private String target;

    @ApiModelProperty(value = "路由路径。表示前端路由路径", example = "/authority/ComponentModule")
    private String path;

    @ApiModelProperty(value = "地址", example = "http://www.xxxx.com/index.html")
    private String url;

    @ApiModelProperty("图标")
    private String icon;

    //// todo: wxl 好像没用了
    //@ApiModelProperty(value = "是否可见。true:可见，false:不可见", example = "true", allowableValues = "true,false")
    //@JSONField(name = "isVisible")
    //private Boolean visible;

    @ApiModelProperty(value = "排序序号", example = "1", allowableValues = "range[1,infinity]")
    @SortSnField
    private Integer sortSn;


    @ApiModelProperty("状态。逻辑删除字段")
    @LogicDeleteField
    private Boolean status = true;
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

}

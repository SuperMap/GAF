/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.api.scanner.entity;

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
 * api资源
 *
 * @author zhm
 * @date:2021/3/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("api资源")
public class AuthResourceApi implements Serializable {
    @NotNull
    @ApiModelProperty("接口资源id")
    @Id
    private String resourceApiId;
    @NotNull
    @ApiModelProperty("API组id")
    @ParentIdField
    private String apiCatalogId;
    @NotNull
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("路由路径")
    private String routeUrl;
    @ApiModelProperty("方法")
    private String method;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("状态")
    @JSONField(name = "isStatus")
    @LogicDeleteField
    private Boolean status;
    @ApiModelProperty("排序序号")
    @SortSnField
    private Integer sortSn;
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

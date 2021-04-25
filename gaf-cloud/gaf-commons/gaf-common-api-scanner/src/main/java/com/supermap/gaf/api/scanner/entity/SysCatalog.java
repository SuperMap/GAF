/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.api.scanner.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.supermap.gaf.annotation.*;
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
* 目录
 * @date:2021/3/25
* @author wangxiaolong
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("目录")
public class SysCatalog implements Serializable {
    @NotNull
    @ApiModelProperty("目录id")
    @Id
    @ConfigName("resourceId")
    private String catalogId;
    @NotNull
    @ApiModelProperty("上级目录id")
    @ParentIdField
    @ConfigName("pid")
    private String parentId;
    @ApiModelProperty("排序序号")
    @SortSnField
    private Integer sortSn;
    @NotNull
    @ApiModelProperty("目录名称")
    @ConfigName("resourceName")
    private String name;
    @NotNull
    @ApiModelProperty("类别")
    private String type;
    @ApiModelProperty("编码")
    private String code;
    @NotNull
    @ApiModelProperty("所属组件")
    private String sysComponentId;
    @ApiModelProperty("图标地址")
    private String iconUrl;
    @ApiModelProperty("描述")
    private String description;
    @NotNull
    @ApiModelProperty("状态")
    @JSONField(name="isStatus")
    @LogicDeleteField
    private Boolean status;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    @UpdatedTimeField
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("业务类别。业务类型字典码，资源目录等业务数据目录使用")
    private String bizTypeCode;
}

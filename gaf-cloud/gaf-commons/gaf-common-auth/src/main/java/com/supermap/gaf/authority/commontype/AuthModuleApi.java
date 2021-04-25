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
* 模块接口关联实体类
 * @date:2021/3/25
* @author zhm
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "模块接口", description = "模块接口关联实体类")
public class AuthModuleApi {
    @NotNull
    @Id
    @ApiModelProperty("模块接口id")
    private String moduleApiId;
    @NotNull
    @ApiModelProperty("模块id")
    @ParentIdField
    private String resourceModuleId;
    @NotNull
    @ApiModelProperty("接口id")
    private String resourceApiId;
    @ApiModelProperty("状态。逻辑删除字段")
    @LogicDeleteField
    @JSONField(name="isStatus")
    private Boolean status;
    @ApiModelProperty("排序序号")
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
}

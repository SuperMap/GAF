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
 * 岗位
 *
 * @author zhm
 * @date:2021/3/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("岗位")
public class AuthPost implements Serializable {
    @ApiModelProperty("岗位id")
    @Id
    private String postId;
    @ApiModelProperty("租户id")
    private String tenantId;
    @NotNull
    @ApiModelProperty("部门id")
    @ParentIdField
    private String departmentId;
    @ApiModelProperty(value = "排序序号", example = "1", allowableValues = "range[1,infinity]")
    @SortSnField
    private Integer sortSn = 1;
    @NotNull
    @ApiModelProperty(value = "名称", example = "开发")
    private String postName;
    @ApiModelProperty(value = "英文名称", example = "deplopmentPost")
    private String nameEn;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("状态。逻辑删除字段")
    @JSONField(name = "isStatus")
    @LogicDeleteField
    private Boolean status = true;
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

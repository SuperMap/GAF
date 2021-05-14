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
 * 用户挂职
 *
 * @date:2021/3/25
 * @author yangdong
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户挂职", description = "用户岗位关联实体类")
public class AuthUserParttime implements Serializable {
    @ApiModelProperty("用户挂职id")
    @Id
    private String userParttimeId;
    @NotNull
    @ApiModelProperty("用户id")
    @ParentIdField
    private String userId;
    @NotNull
    @ApiModelProperty("挂职部门id")
    private String departmentId;
    @NotNull
    @ApiModelProperty("岗位id")
    private String postId;
    @ApiModelProperty("岗位类别。暂时无用")
    private String postType;
    @ApiModelProperty("授权截止时间")
    private Date expirationTime;
    @ApiModelProperty("状态。逻辑删除字段")
    @JSONField(name = "isStatus")
    @LogicDeleteField
    private Boolean status = true;
    @ApiModelProperty("排序序号。暂时无用")
    @SortSnField
    private Integer sortSn = 1;
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
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.commontype;

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
 * 租户
 *
 * @author zhm
 * @date:2021/3/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("租户")
public class AuthTenant implements Serializable {
    /**
     * 平台默认租户id
     */
    public static final String PLATFORM_DEDAULT_TENANT_ID = "tenant_000000";
    @NotNull
    @ApiModelProperty("租户id")
    private String tenantId;
    @NotNull
    @ApiModelProperty(value = "租户名称", example = "xxx公司")
    private String tenantName;
    @ApiModelProperty("租户描述")
    private String description;
    @ApiModelProperty(value = "租户类别.1:开发并运营类,2:运营类", example = "1", allowableValues = "1,2")
    private String type;
    @ApiModelProperty("英文名称")
    private String nameEn;
    @NotNull
    @ApiModelProperty(value = "英文简称。仅允许英文字母和数字", example = "xxx123")
    private String briefNameEn;
    @ApiModelProperty("编码。暂时无用")
    private String code;
    @ApiModelProperty("状态。逻辑删除字段")
    private Boolean status;
    @ApiModelProperty("初始管理员id")
    private String adminId;
    @ApiModelProperty("初始管理员名称")
    private String adminName;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;

    // transient
    // 工作流租户
    private String workflowTenantId;
    private String workflowTenantName;
}

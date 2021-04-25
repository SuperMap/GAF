/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.commontype;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 第三方用户映射
 *
 * @date:2021/3/25
 * @author yangdong
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("第三方用户映射")
public class AuthP3UserMapping {
    @NotNull
    @ApiModelProperty("用户映射id")
    private String userMappingId;
    @NotNull
    @ApiModelProperty("用户")
    private String userId;
    @NotNull
    @ApiModelProperty("第三方用户")
    private String p3UserId;
    @NotNull
    @ApiModelProperty("第三方组件")
    private String p3ComponentId;
    @ApiModelProperty("状态。逻辑删除字段")
    @JSONField(name = "isStatus")
    private Boolean status = true;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;
    @ApiModelProperty("第三方租户")
    private String p3TenantId;
    @ApiModelProperty("第三方部门")
    private String p3DepartmentId;
}

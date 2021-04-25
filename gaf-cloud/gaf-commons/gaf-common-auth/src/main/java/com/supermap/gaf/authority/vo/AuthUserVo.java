/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @date:2021/3/25
 * @author wxl
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户vo")
public class AuthUserVo implements Serializable {
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("部门id")
    private String departmentId;
    @ApiModelProperty("排序序号")
    private Integer sortSn;
    @ApiModelProperty("登录用户名")
    private String name;
    @ApiModelProperty("真实姓名")
    private String realName;
    @ApiModelProperty("岗位id")
    private String postId;
    @ApiModelProperty("授权截止时间")
    private Date expirationTime;
    @ApiModelProperty("是否第三方")
    private Boolean isThirdParty;
    @ApiModelProperty("状态")
    private Boolean status;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("上次登录时间")
    private Date lastLoginTime;
    @ApiModelProperty("创建时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private String updatedBy;
    @ApiModelProperty("部门名称")
    private String departmentName;
    @ApiModelProperty("岗位名称")
    private String postName;
}

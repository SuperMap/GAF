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

import java.io.Serializable;
import java.util.List;

/**
 * 用户详情
 * @author dqc
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户详情", description = "用户详情")
public class AuthUserInfoDetails implements Serializable {
    private static final long serialVersionUID = -3861178637681773147L;

    @ApiModelProperty("用户信息")
    private AuthUser authUser;

    @ApiModelProperty("用户api信息")
    private List<AuthResourceApi> authResourceApiList;

    @ApiModelProperty("用户模块信息")
    private List<AuthResourceModule> authResourceModuleList;

    @ApiModelProperty("用户角色信息")
    private List<AuthRole> authRoleList;


}

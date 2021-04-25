/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.vo;


import com.supermap.gaf.authority.commontype.AuthTenant;
import com.supermap.gaf.authority.commontype.AuthUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 租户初始化信息
 * @date:2021/3/25
 * @author zhm
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("租户初始化信息")
public class TenantInitVo implements Serializable {
    @NotNull
    @ApiModelProperty("租户")
    private AuthTenant authTenant;

    @NotNull
    @ApiModelProperty("租户管理员")
    private AuthUser authUser;

}

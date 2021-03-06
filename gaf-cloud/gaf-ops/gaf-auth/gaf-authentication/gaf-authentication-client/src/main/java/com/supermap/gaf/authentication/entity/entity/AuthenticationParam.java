/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.entity.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/23 11:01 AM
 */
@Data
@NoArgsConstructor
@ApiModel("认证参数对象")
public class AuthenticationParam implements Serializable {
    private static final long serialVersionUID = -3977280069205979450L;

    @ApiModelProperty("cookie中包含的自定义session")
    private String customSessionId;
    @ApiModelProperty("http请求头中包含的token")
    private String authorization;
}

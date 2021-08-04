/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.commontypes.rest;

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
public class AuthenticationResult implements Serializable {
    private static final long serialVersionUID = 7092070907507524738L;

    private String username;
    private String jwtToken;
}

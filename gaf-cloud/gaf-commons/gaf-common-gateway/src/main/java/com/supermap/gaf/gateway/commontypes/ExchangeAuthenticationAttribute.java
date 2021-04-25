/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.gateway.commontypes;

import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.gateway.commontypes.properties.GatewaySecurityProperties;
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
public class ExchangeAuthenticationAttribute implements Serializable {

    private static final long serialVersionUID = -5720391819183827716L;

    private String uri;
    private Boolean isPublicUrl;
    private Boolean isIndexUrl;
    private AuthenticationResult authenticationResult;
    private GatewaySecurityProperties gatewaySecurityProperties;
}

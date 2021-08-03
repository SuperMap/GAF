/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.shiro.commontypes;

import org.apache.shiro.authc.AuthenticationToken;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class JWTToken implements AuthenticationToken {

    private static final long serialVersionUID = 1339735234631809734L;

    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return Boolean.TRUE;
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.shiro.commontypes;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;


/**
 * @author dqc
 */
@Data
public class CustomToken implements AuthenticationToken {

    private static final long serialVersionUID = -1L;

    private HttpServletRequest request;

    public CustomToken(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Object getPrincipal() {
        return this.request.getUserPrincipal();
    }

    @Override
    public Object getCredentials() {
        return Boolean.TRUE;
    }
}

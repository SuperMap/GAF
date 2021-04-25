/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.entity.entity;


import java.io.Serializable;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/23 11:01 AM
 */
public class AuthenticationResult implements Serializable {
    private static final long serialVersionUID = 7092070907507524738L;

    private String username;
    private String jwtToken;

    public AuthenticationResult() {
    }

    public AuthenticationResult(String username, String jwtToken) {
        this.username = username;
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}

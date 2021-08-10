/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authentication.config.authc.jwtorsession;

import com.supermap.gaf.authentication.entity.entity.AuthenticationParam;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/18 3:26 PM
 */
public class JwtOrSessionAuthentication extends AbstractAuthenticationToken {

    private String principal;
    private String jwtToken;

    private AuthenticationParam authenticationParam;


    public JwtOrSessionAuthentication(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public JwtOrSessionAuthentication(Collection<? extends GrantedAuthority> authorities, String principal, String jwtToken) {
        super(authorities);
        this.principal = principal;
        this.jwtToken = jwtToken;
    }

    public JwtOrSessionAuthentication(Collection<? extends GrantedAuthority> authorities, AuthenticationParam authenticationParam) {
        super(authorities);
        this.authenticationParam = authenticationParam;
    }

    @Override
    public Object getCredentials() {
        return this.jwtToken;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public AuthenticationParam getAuthenticationParam() {
        return authenticationParam;
    }

    public void setAuthenticationParam(AuthenticationParam authenticationParam) {
        this.authenticationParam = authenticationParam;
    }
}

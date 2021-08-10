/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authentication.config.authc.jwtorsession;

import com.supermap.gaf.authentication.config.authc.CustomUserDetailsServiceImpl;
import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.authentication.service.ValidateAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/16 7:09 PM
 */
public class JwtOrSessionAuthenticationProvider implements AuthenticationProvider {
    private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
    @Autowired
    private ValidateAuthenticationService validateAuthenticationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtOrSessionAuthentication jwtOrSessionAuthentication = (JwtOrSessionAuthentication) authentication;

        AuthenticationResult authenticationResult = validateAuthenticationService.authentication(jwtOrSessionAuthentication.getAuthenticationParam());

        if (StringUtils.isEmpty(authenticationResult.getUsername())) {
            throw new BadCredentialsException("");
        }
        UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(authenticationResult.getUsername());
        JwtOrSessionAuthentication result = new JwtOrSessionAuthentication(userDetails.getAuthorities(), authenticationResult.getUsername(), authenticationResult.getJwtToken());
        result.setAuthenticated(true);
        return result;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(JwtOrSessionAuthentication.class);
    }


    public void setCustomUserDetailsServiceImpl(CustomUserDetailsServiceImpl customUserDetailsServiceImpl) {
        this.customUserDetailsServiceImpl = customUserDetailsServiceImpl;
    }


}


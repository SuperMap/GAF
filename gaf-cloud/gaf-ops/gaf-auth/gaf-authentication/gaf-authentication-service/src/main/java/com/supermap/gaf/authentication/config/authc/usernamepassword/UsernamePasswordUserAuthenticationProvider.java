/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.config.authc.usernamepassword;

import com.supermap.gaf.authentication.config.authc.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/16 7:09 PM
 */
public class UsernamePasswordUserAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("username can not be empty");
        }
        if (StringUtils.isEmpty(password)) {
            throw new BadCredentialsException("password can not be empty");
        }
        UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(username);
        if (!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("password is incorrect");
        }
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        return new UsernamePasswordAuthenticationToken(userDetails,password,authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}


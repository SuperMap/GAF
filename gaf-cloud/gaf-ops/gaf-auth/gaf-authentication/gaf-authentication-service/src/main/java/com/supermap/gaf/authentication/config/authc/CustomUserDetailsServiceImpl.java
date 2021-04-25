/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.config.authc;

import com.supermap.gaf.authority.commontype.AuthRole;
import com.supermap.gaf.authority.commontype.AuthTenant;
import com.supermap.gaf.authority.commontype.AuthUser;
import com.supermap.gaf.authority.service.AuthAuthorizationQueryService;
import com.supermap.gaf.authority.service.AuthTenantQueryService;
import com.supermap.gaf.authority.service.AuthUserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/16 7:09 PM
 */
public class CustomUserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private AuthUserQueryService authUserQueryService;
    @Autowired
    private AuthAuthorizationQueryService authAuthorizationQueryService;
    @Autowired
    private AuthTenantQueryService authTenantQueryService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = authUserQueryService.getByUserName(username);
        if (null == user){
            throw new UsernameNotFoundException(username);
        }
        AuthTenant authTenant = authTenantQueryService.getById(user.getTenantId());
        if (null == authTenant){
            throw new UsernameNotFoundException(username);
        }
        List<AuthRole>  authRoles = authAuthorizationQueryService.listAuthorizationRole(user.getUserId());
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (AuthRole role : authRoles){
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);
    }
}


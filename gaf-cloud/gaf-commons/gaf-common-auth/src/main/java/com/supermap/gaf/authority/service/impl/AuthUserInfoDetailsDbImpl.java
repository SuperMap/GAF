package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.*;
import com.supermap.gaf.authority.service.AuthAuthorizationQueryService;
import com.supermap.gaf.authority.service.AuthUserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : duke
 * @since 2021/5/19 9:22 AM
 */
@Service
public class AuthUserInfoDetailsDbImpl implements IauthUserInfoDetails {
    @Autowired
    private AuthUserQueryService userQueryService;
    @Autowired
    private AuthAuthorizationQueryService authAuthorizationQueryService;

    @Override
    public AuthUserInfoDetails getAuthUserInfoDetails(String username) {
        AuthUser authUser = userQueryService.getByUserName(username);
        authUser.setPassword(null);
        String userId = authUser.getUserId();
        List<AuthResourceApi> authResourceApis = authAuthorizationQueryService.listAuthorizationApi(userId);
        List<AuthResourceModule> authResourceModules = authAuthorizationQueryService.listAuthorizationModule(userId);
        List<AuthRole> authRoles = authAuthorizationQueryService.listAuthorizationRole(userId);
        return new AuthUserInfoDetails(authUser,authResourceApis,authResourceModules,authRoles);
    }

}

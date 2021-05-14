/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.shiro.realms;

import com.supermap.gaf.authority.client.AuthUserInfoDetailsClient;
import com.supermap.gaf.authority.commontype.*;
import com.supermap.gaf.authority.service.AuthAuthorizationQueryService;
import com.supermap.gaf.authority.service.AuthUserQueryService;
import com.supermap.gaf.shiro.JJWTUtils;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.JWTToken;
import io.buji.pac4j.subject.Pac4jPrincipal;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.oauth.profile.OAuth20Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author:yj
 * @date:2021/3/25
*/
public class JWTTokenRealm extends AuthorizingRealm {
    @Autowired
    @Lazy
    private AuthUserInfoDetailsClient userInfoDetailsClient;

    @Override
    public Class<?> getAuthenticationTokenClass() {
        return JWTToken.class;// 此Realm只支持JwtToken
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JWTToken jwtToken = (JWTToken) token;
        try {
            String accessToken = (String) jwtToken.getPrincipal();

            OAuth20Profile profile = getOAuth20Profile(accessToken);
            String username = JJWTUtils.getUserNameFromJwsUntrusted(accessToken,"user_name");

            // 获取权限、租户、角色信息(新)
            AuthUserInfoDetails userInfoDetails = userInfoDetailsClient.getAuthUserInfoDetails(username).getData();

            AuthUser authUser = userInfoDetails.getAuthUser();
            String userId = authUser.getUserId();
            profile.setId(userId);

            List<AuthResourceApi> authResourceApis = userInfoDetails.getAuthResourceApiList();
            List<AuthResourceModule> authResourceModules = userInfoDetails.getAuthResourceModuleList();
            List<AuthRole> authRoles = userInfoDetails.getAuthRoleList();
            SecurityUtilsExt.recordKeycloakUser(profile,authUser,authResourceApis,authResourceModules,authRoles);
            final Pac4jPrincipal principal = new Pac4jPrincipal(Arrays.asList(new CommonProfile[] { profile }));
            return new SimpleAuthenticationInfo(principal, Boolean.TRUE, getName());
        } catch (Exception e) {
            throw new AuthenticationException("JWT 解析异常:");
        }

    }

    /**
     * 授权,JWT已包含访问主张只需要解析其中的主张定义就行了
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        final Set<String> roles = new HashSet<>();
        final Set<String> permissions = new HashSet<>();
        final Pac4jPrincipal principal = principals.oneByType(Pac4jPrincipal.class);
        if (principal != null) {
            final List<CommonProfile> profiles = principal.getProfiles();
            for (CommonProfile profile : profiles) {
                if (profile != null) {
                    roles.addAll(profile.getRoles());
                    permissions.addAll(profile.getPermissions());
                }
            }
        }
        final SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(roles);
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }


    private OAuth20Profile getOAuth20Profile(String accessToken){
        OAuth20Profile profile = new OAuth20Profile() {
            @Override
            public void setAccessToken(String accessToken) {
                super.setAccessToken(accessToken);
            }
        };
        profile.setAccessToken(accessToken);
        return profile;
    }

}

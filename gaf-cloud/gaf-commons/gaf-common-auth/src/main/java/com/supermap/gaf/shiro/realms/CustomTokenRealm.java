/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.shiro.realms;

import com.supermap.gaf.authority.commontype.*;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.CustomToken;
import io.buji.pac4j.subject.Pac4jPrincipal;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.pac4j.core.profile.CommonProfile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author:yj
 * @date:2021/3/25
 */
@Slf4j
@Data
public class CustomTokenRealm extends AuthorizingRealm {
    private IauthUserInfoDetails iauthUserInfoDetails;
    private IauthUsername iauthUsername;

    public CustomTokenRealm(IauthUsername iauthUsername, IauthUserInfoDetails iauthUserInfoDetails) {
        this.iauthUserInfoDetails = iauthUserInfoDetails;
        this.iauthUsername = iauthUsername;
    }

    @Override
    public Class<?> getAuthenticationTokenClass() {
        return CustomToken.class;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        CustomToken customToken = (CustomToken) token;
        try {
            String username = iauthUsername.getAuthUsername(customToken);

            // 获取权限、租户、角色信息(新)
            AuthUserInfoDetails userInfoDetails = iauthUserInfoDetails.getAuthUserInfoDetails(username);

            CommonProfile profile = new CommonProfile();
            AuthUser authUser = userInfoDetails.getAuthUser();
            String userId = authUser.getUserId();
            profile.setId(userId);

            List<AuthResourceApi> authResourceApis = userInfoDetails.getAuthResourceApiList();
            List<AuthResourceMenu> authResourceMenus = userInfoDetails.getAuthResourceMenusList();
            List<AuthRole> authRoles = userInfoDetails.getAuthRoleList();
            SecurityUtilsExt.recordKeycloakUser(profile, authUser, authResourceApis,authResourceMenus, authRoles);
            final Pac4jPrincipal principal = new Pac4jPrincipal(Arrays.asList(new CommonProfile[]{profile}));
            return new SimpleAuthenticationInfo(principal, Boolean.TRUE, getName());
        } catch (Exception e) {
            log.error("【URI:" + ((CustomToken) token).getRequest().getRequestURI() + "】获取用户信息失败");
            throw new AuthenticationException("获取用户信息失败:");
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


}

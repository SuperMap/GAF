/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authentication.service.impl;

import com.supermap.gaf.authentication.entity.entity.AuthenticationParam;
import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.authentication.entity.entity.AuthorizationParam;
import com.supermap.gaf.authentication.service.CustomLoginService;
import com.supermap.gaf.authentication.service.ValidateAuthenticationService;
import com.supermap.gaf.authority.commontype.AuthResourceApi;
import com.supermap.gaf.authority.commontype.AuthRole;
import com.supermap.gaf.authority.commontype.AuthUserInfoDetails;
import com.supermap.gaf.authority.service.impl.AuthUserInfoDetailsDbImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.supermap.gaf.authentication.entity.constant.LoginConstant.REDIS_LOGIN_SESSION_PREFIX;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/23 10:48 AM
 */
@Slf4j
@Service
public class ValidateAuthenticationServiceImpl implements ValidateAuthenticationService {
    @Autowired
    private CustomLoginService customLoginService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private AuthUserInfoDetailsDbImpl authUserInfoDetailsDb;

    @Override
    public AuthenticationResult authentication(AuthenticationParam authenticationParam) {
        String customSessionId = authenticationParam.getCustomSessionId();
        String jwtToken = authenticationParam.getAuthorization();

        AuthenticationResult result = new AuthenticationResult();
        if (!StringUtils.isEmpty(jwtToken)) {
            try {
                Map<String, ?> map = customLoginService.checkJwtToken(jwtToken);
                if (null != map) {
                    result.setUsername((String) map.get("user_name"));
                    result.setJwtToken(jwtToken);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (StringUtils.isEmpty(result.getUsername()) && !StringUtils.isEmpty(customSessionId)) {
            try {
                Map<String, String> oAuth2AccessToken = (Map<String, String>) redisTemplate.opsForHash().get(REDIS_LOGIN_SESSION_PREFIX + customSessionId, "token");
                if (oAuth2AccessToken != null) {
                    jwtToken = oAuth2AccessToken.get("access_token");
                    Map<String, ?> map = null;
                    try {
                        map = customLoginService.checkJwtToken(jwtToken);
                        if (null != map) {
                            result.setUsername((String) map.get("user_name"));
                            result.setJwtToken(jwtToken);
                        }
                    } catch (Exception e) {
                        //检查到过期可以使用refreshtoken 刷新token
                        String refreshToken = oAuth2AccessToken.get("refresh_token");
                        OAuth2AccessToken refreshOauth2AccessToken = customLoginService.refreshOauth2AccessToken(refreshToken);
                        //更新redis存储的认证信息
                        customLoginService.refreshStoreLoginSession(customSessionId, refreshOauth2AccessToken);
                        String username = (String) redisTemplate.opsForHash().get(REDIS_LOGIN_SESSION_PREFIX + customSessionId, "username");
                        result.setUsername(username);
                        result.setJwtToken(refreshOauth2AccessToken.getValue());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public Boolean authorization(AuthorizationParam authorizationParam) {
        AuthUserInfoDetails details = authUserInfoDetailsDb.getAuthUserInfoDetails(authorizationParam.getUsername());
        List<AuthResourceApi> authResourceApiList = details.getAuthResourceApiList();
        if (isAdminAccess(details.getAuthRoleList())) {
            return true;
        }

        return accessValid(authorizationParam, authResourceApiList);
    }

    /**
     * 匹配uri和权限列表
     *
     * @return
     */
    private boolean accessValid(AuthorizationParam authorizationParam, List<AuthResourceApi> authResourceApis) {
        AntPathMatcher matcher = new AntPathMatcher();
        for (AuthResourceApi authResourceApi : authResourceApis) {
            String path = authResourceApi.getRouteUrl();
            if (matcher.match(path, authorizationParam.getUri()) && authorizationParam.getMethod().equalsIgnoreCase(authResourceApi.getMethod())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是管理员角色权限
     *
     * @return
     */
    private boolean isAdminAccess(List<AuthRole> authRoles) {
        for (AuthRole role : authRoles) {
            if ("platform_admin".equals(role.getNameEn())) {
                return true;
            }
        }
        return false;
    }


}

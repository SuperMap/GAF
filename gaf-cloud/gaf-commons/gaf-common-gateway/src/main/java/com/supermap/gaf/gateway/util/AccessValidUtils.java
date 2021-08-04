/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.util;

import com.supermap.gaf.authority.commontype.AuthResourceApi;
import com.supermap.gaf.authority.commontype.AuthRole;
import com.supermap.gaf.authority.enums.ResourceApiMethodEnum;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.util.AntPathMatcher;

import java.util.List;


/**
 * @author : duke
 * @date:2021/3/25 权限判断工具类
 * @since 2020/4/20 9:45 AM
 */
public class AccessValidUtils {

    private static Logger log = LogUtil.getLocLogger(com.supermap.gaf.gateway.util.AccessValidUtils.class);

    private static final String AUTH_LOGIN_URL = "oauth2/authorization";
    private static final String FINAL_LOGIN_URL = "login/oauth2/code";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String BEARER = "Bearer ";


    /**
     * 匹配uri和权限列表
     *
     * @param uri
     * @return
     */
    public boolean accessValid(String uri, String method, List<AuthResourceApi> authResourceApis) {
        AntPathMatcher matcher = new AntPathMatcher();
        for (AuthResourceApi authResourceApi : authResourceApis) {
            String path = authResourceApi.getRouteUrl();
            if (matcher.match(path, uri) && method.equalsIgnoreCase(ResourceApiMethodEnum.getByValue(authResourceApi.getMethod()).name())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 是否是登录请求
     *
     * @param uri
     * @return
     */
    public boolean isOauthLoginUrl(String uri) {
        return uri.contains(AUTH_LOGIN_URL) || uri.contains(FINAL_LOGIN_URL);
    }


    /**
     * 判断url是否是publicUrls
     *
     * @return
     */
    public static boolean isPublicUrls(String uri, List<String> publicUrls) {
        AntPathMatcher matcher = new AntPathMatcher();
        for (String publicUrl : publicUrls) {
            if (matcher.match(publicUrl, uri)) {
                return true;
            }
        }
        return false;
    }

//    /**
//     * 从exchange里面获取安全框架上下文
//     * @return
//     */
//    public SecurityContext getSecurityContext(WebSession session){
//        try {
//            return (SecurityContext) session.getAttributes().get(SPRING_SECURITY_CONTEXT_KEY);
//        }catch (Exception e){
//            log.info("无法从获取security上下文");
//        }
//        return null;
//    }

    /**
     * 判断是否是管理员角色权限
     *
     * @return
     */
    public boolean isAdminAccess(List<AuthRole> authRoles) {
        for (AuthRole role : authRoles) {
            if ("ADMIN".equals(role.getNameEn())) {
                return true;
            }
        }
        return false;
    }
}

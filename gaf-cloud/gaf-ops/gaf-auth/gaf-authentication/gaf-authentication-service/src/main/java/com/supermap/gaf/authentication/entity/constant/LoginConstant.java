/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authentication.entity.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/20 2:32 PM
 */
@Component
public class LoginConstant {

    public static String LOGIN_SUCCESS_REDIRECT = "/portal/view/index.html";

    public static String LOGIN_URL = "/authentication/view/index.html#/login";


    public static final String CUSTOM_LOGIN_SESSION_NAME = "CUSTOM_SESSION_ID";

    public static final String CUSTOM_CLIENT = "custom_client";
    public static final String CLIENT_ID = "client_id";
    public static final String USER_NAME = "user_name";
    public static final String RESPONSE_TYPE = "response_type";

    public static final String REDIS_LOGIN_SESSION_PREFIX = "login_session_to_user_info:";

    public static final String AUTHORIZATION = "Authorization";

    public static final String BEARER = "Bearer";

    public static final String LOGIN_WITH_JWT_SEESION = "/authentication/login/jwt_session";
    public static final String OAUTH2_AUTHORIZE = "/oauth/authorize";

    public static final String LOGIN_WITH_USERNAME_PASSWORD = "/authentication/login/username_password";

    public static final String LOGIN_CENTER_URL = "/authentication/login/center";

    public static final String LOGIN_CALLBACK_WITH_OIDC = "/authentication/login/callback/oidc";

    public static final String LOGIN_CALLBACK_WITH_OIDC_VALIDATED = "/authentication/login/callback/oidc/validated";


    public static final int TOKEN_EXPIRE = 7200;

    public static final int TOKEN_REFRESH_EXPIRE = 72000;


    @Value("${login.loginSuccessRedirect:}")
    public void setLoginSuccessRedirect(String loginSuccessRedirect) {
        if (!StringUtils.isEmpty(loginSuccessRedirect)) {
            LoginConstant.LOGIN_SUCCESS_REDIRECT = loginSuccessRedirect;
        }
    }

    @Value("${login.loginUrl:}")
    public void setLoginUrl(String loginUrl) {
        if (!StringUtils.isEmpty(loginUrl)) {
            LoginConstant.LOGIN_URL = loginUrl;
        }
    }
}

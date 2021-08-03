/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authentication.entity.entity.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/16 10:57 AM
 */
@ConfigurationProperties(prefix = "login")
@Component
public class LoginProperties {
    public static final int THIRDPARTY_PROPERTY_LEVEL = 2;
    /**
     * eg: oidc:keycloak
     */
    private String enabledThirdParty;

    private String loginSuccessRedirect;
    private String loginUrl;

    @PostConstruct
    public void validate() {
        if (StringUtils.isEmpty(enabledThirdParty)) {
            return;
        }
        String[] enableThirdParts = enabledThirdParty.split(":");
        if (enableThirdParts.length != THIRDPARTY_PROPERTY_LEVEL) {
            throw new IllegalStateException("enable-third-party must be like 'oidc:keyclaok',param after split having length is " + THIRDPARTY_PROPERTY_LEVEL);
        }
        if (StringUtils.isEmpty(enableThirdParts[0]) || StringUtils.isEmpty(enableThirdParts[1])) {
            throw new IllegalStateException("enable-third-party param must not be empty");
        }
    }

    public String getEnabledThirdParty() {
        return enabledThirdParty;
    }

    public void setEnabledThirdParty(String enabledThirdParty) {
        this.enabledThirdParty = enabledThirdParty;
    }

    public String getLoginSuccessRedirect() {
        return loginSuccessRedirect;
    }

    public void setLoginSuccessRedirect(String loginSuccessRedirect) {
        this.loginSuccessRedirect = loginSuccessRedirect;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.gateway.commontypes.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 网关安全设置
 * </p>
 *
 * @author ${Author}
 * @version ${Version}
 * @date:2021/3/25
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "gateway.security")
public class GatewaySecurityProperties {

    public static String DEFAULT_LOGIN_URL = "/login";
    public static String DEFAULT_LOGOUT_URL = "/logout";
    public static String DEFAULT_INDEX_URL = "/index.html";
    public static String DEFAULT_PUBLIC_URLS = "/**/view/**,/**/static/**,/**/_static/**";

    private String centerLoginUrl = DEFAULT_LOGIN_URL;

    private String gatewayLoginUrl = DEFAULT_LOGIN_URL;

    private String centerLogoutUrl = DEFAULT_LOGOUT_URL;

    private String gatewayLogoutUrl = DEFAULT_LOGOUT_URL;

    private String indexUrl = DEFAULT_INDEX_URL;

    private boolean apiAuthzEnable = false;

    private List<String> publicUrls = Arrays.asList(DEFAULT_PUBLIC_URLS);


    public String getCenterLoginUrl() {
        return centerLoginUrl;
    }

    public void setCenterLoginUrl(String centerLoginUrl) {
        this.centerLoginUrl = centerLoginUrl;
    }

    public String getGatewayLoginUrl() {
        return gatewayLoginUrl;
    }

    public void setGatewayLoginUrl(String gatewayLoginUrl) {
        this.gatewayLoginUrl = gatewayLoginUrl;
    }

    public String getCenterLogoutUrl() {
        return centerLogoutUrl;
    }

    public void setCenterLogoutUrl(String centerLogoutUrl) {
        this.centerLogoutUrl = centerLogoutUrl;
    }

    public String getGatewayLogoutUrl() {
        return gatewayLogoutUrl;
    }

    public void setGatewayLogoutUrl(String gatewayLogoutUrl) {
        this.gatewayLogoutUrl = gatewayLogoutUrl;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public List<String> getPublicUrls() {
        return publicUrls;
    }

    public void setPublicUrls(List<String> publicUrls) {
        this.publicUrls = publicUrls;
    }

    public boolean isApiAuthzEnable() {
        return apiAuthzEnable;
    }

    public void setApiAuthzEnable(boolean apiAuthzEnable) {
        this.apiAuthzEnable = apiAuthzEnable;
    }
}

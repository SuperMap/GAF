/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.shiro.config;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 系统全局Shiro配置，支持dev(开发模式，默认账户admin|123456）\cas\keylcoak
 *
 * @author h_sha
 * @date:2021/3/25
 */
@ConfigurationProperties(prefix = "shiro")
@EnableConfigurationProperties({KeycloakConfig.class})
public class ShiroConfig {

    /**
     * 默认登录路径
     */
    private boolean enable;
    /**
     * 默认登录路径
     */
    private String loginUrl;

    /**
     * 登录成功跳转地址
     */
    private String successUrl;

    /**
     * 工程服务路径
     */
    private String serviceUrl;

    /**
     * 无需登录访问地址，支持ant表达式 /view**, /view/**等
     */
    private List<String> publicUrls;

    /**
     * 请求权限设, 单记录,分割 "/**, authc", "/login**, anon"
     */
    private List<String> urlFilters;

    /**
     * 默认配置的权限设置
     */
    private DevConfig dev;

    /**
     * cas配置
     */
    private CasConfig cas;

    /**
     * keycloak配置
     */
    @Autowired
    private KeycloakConfig keycloak;

    public DevConfig getDev() {
        return dev;
    }

    public void setDev(DevConfig dev) {
        this.dev = dev;
    }

    public CasConfig getCas() {
        return cas;
    }

    public void setCas(CasConfig cas) {
        this.cas = cas;
    }

    public KeycloakConfig getKeycloak() {
        return keycloak;
    }

    public void setKeycloak(KeycloakConfig keycloak) {
        this.keycloak = keycloak;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    /**
     * <p>
     * 设置服务根路径，可以设置服务跳转回的标准服务地址，也可以设置相对路径，后补充服务参数
     * </p>
     *
     * @return
     * @since 1.0.0
     */
    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public List<String> getUrlFilters() {
        return urlFilters;
    }

    public void setUrlFilters(List<String> urlFilters) {
        this.urlFilters = urlFilters;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public List<String> getPublicUrls() {
        return publicUrls;
    }

    public void setPublicUrls(List<String> publicUrls) {
        this.publicUrls = publicUrls;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}

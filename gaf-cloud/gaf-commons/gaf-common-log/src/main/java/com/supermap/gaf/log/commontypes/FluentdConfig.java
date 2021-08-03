/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.log.commontypes;

import com.supermap.gaf.log.context.FluentdAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


/**
 * @author:yj
 * @date:2021/3/25
 */
@Component
@ConfigurationProperties(prefix = "fluentd")
@ConditionalOnProperty(name = "fluentd.enable", havingValue = "true")
public class FluentdConfig {
    @Value("${spring.application.name}")
    private String service;
    private String host;
    private int port;
    private boolean enable;
    private String tagPrefix;

    @Bean
    public FluentdConfig setFluentdConfig(@Autowired FluentdConfig config) {
        FluentdAppender.cfg = config;
        return config;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getTagPrefix() {
        return tagPrefix;
    }

    public void setTagPrefix(String tagPrefix) {
        this.tagPrefix = tagPrefix;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}

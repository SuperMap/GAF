/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * @Description iserver基础服务信息
 * @Author gaobo
 * @author:yj
 * @date:2021/3/25
 * @Date 2019/12/7
 **/
@Configuration
@ConfigurationProperties(prefix = "hostservers")
@EnableConfigurationProperties({HostServerSetting.class})
@ApiModel("iserver基础服务信息")
public class HostServerConfig {
    @ApiModelProperty("基础服务id")
    private String defaultServerId;
    @ApiModelProperty("基础服务配置")
    private List<HostServerSetting> hostServerSetting;

    public List<HostServerSetting> getHostServerSetting() {
        return hostServerSetting;
    }

    public void setHostServerSetting(List<HostServerSetting> hostServerSetting) {
        this.hostServerSetting = hostServerSetting;
    }

    public String getDefaultServerId() {
        return defaultServerId;
    }

    public void setDefaultServerId(String defaultServerId) {
        this.defaultServerId = defaultServerId;
    }

}


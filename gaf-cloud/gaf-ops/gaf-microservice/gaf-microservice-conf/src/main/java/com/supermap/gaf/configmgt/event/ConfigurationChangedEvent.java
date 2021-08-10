/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.configmgt.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
 * @version 1.0 2019-9-2
 * @date:2021/3/25
 * @description 配置更改事件
 */
public class ConfigurationChangedEvent extends ApplicationEvent {

    private static Object source = new Object();

    private String application;


    private ConfigurationChangedEvent() {
        super(source);
    }

    public ConfigurationChangedEvent(String application) {
        super(source);
        this.application = application;
    }

    public String getApplication() {
        return this.application;
    }
}

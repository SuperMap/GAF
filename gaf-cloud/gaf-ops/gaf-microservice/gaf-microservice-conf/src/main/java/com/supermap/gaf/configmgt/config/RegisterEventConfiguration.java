/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.configmgt.config;

import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
 * @version 1.0 2019-9-9
 * @date:2021/3/25
 * @description 用户扩展事件注册
 */
@Configuration
@RemoteApplicationEventScan(basePackages = "com.supermap.gaf.bus.event")
public class RegisterEventConfiguration {
}

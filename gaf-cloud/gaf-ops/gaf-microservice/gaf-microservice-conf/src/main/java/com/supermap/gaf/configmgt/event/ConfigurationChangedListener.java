/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.configmgt.event;

import com.supermap.gaf.configmgt.services.ConfigServerMgtService;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


/**
 * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
 * @version 1.0 2019-9-2
 * @date:2021/3/25
 * @description
 */
@Component
public class ConfigurationChangedListener implements ApplicationListener<ConfigurationChangedEvent>{


    @Autowired
    private ConfigServerMgtService configServerMgtService;

    private Logger logger = LogUtil.getLocLogger(ConfigurationChangedListener.class);
    
    @Override
    public void onApplicationEvent(ConfigurationChangedEvent event) {
        configServerMgtService.refreshConfiguration(event.getApplication());
        logger.info("------更新配置中-----");
    }
}

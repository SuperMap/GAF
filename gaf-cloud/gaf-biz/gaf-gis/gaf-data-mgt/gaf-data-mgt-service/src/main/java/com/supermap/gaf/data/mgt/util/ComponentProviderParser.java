/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.util;

import com.supermap.gaf.data.mgt.entity.RestInterfaceType;
import com.supermap.server.config.ComponentSetting;
import com.supermap.server.config.ProviderSetting;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @program: gaf-datacenter-modules
 * @description: 服务组件&提供者解析器
 * @author: lidong
 * @date:2021/3/25
 * @create: 2019/05/21
 */
@Service("componentProviderParser")
public class ComponentProviderParser {


    /**
     * 生成服务提供者
     *
     * @param providerName
     * @param setting
     * @param providerSettingClass
     * @return
     */
    public ProviderSetting createProviderSetting(Object setting, String providerName,String providerSettingClass) {
        ProviderSetting providerSetting = new ProviderSetting();
        providerSetting.config = setting;
        providerSetting.name = providerName;
        providerSetting.type = providerSettingClass;
        providerSetting.alias = providerName;
        return providerSetting;
    }

    /**
     * 生成服务组件信息
     *
     * @param componentName  组件名
     * @param providers      服务提供者名,多个用逗号(,)分割
     * @param interfaceType 接口名:rest,restjsr
     * @param type           服务提供者类型
     * @param configParams
     * @return
     */
    public ComponentSetting createComponentSetting(String componentName, String providers, RestInterfaceType interfaceType, String type,
                                                   Map <String, Object> configParams) {
        ComponentSetting setting = new ComponentSetting();
        setting.enabled = true;
        setting.interfaceNames = interfaceType.toString().toLowerCase();
        setting.providers = providers;
        setting.type = type;
        setting.name = componentName;
        return setting;
    }
}

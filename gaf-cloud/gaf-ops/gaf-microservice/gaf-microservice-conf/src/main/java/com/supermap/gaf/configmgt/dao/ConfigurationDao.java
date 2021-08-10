/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.configmgt.dao;


import com.supermap.gaf.configmgt.commontypes.ConfigProperties;
import com.supermap.gaf.configmgt.commontypes.ConfigPropertiesGroup;
import com.supermap.gaf.microservice.ConfigQueryParameter;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
 * @version 1.0 2019-8-9
 * @date:2021/3/25
 * @description
 */
@Repository
public interface ConfigurationDao {
    /**
     * 获取配置信息
     *
     * @param queryParameter
     * @return
     */
    List<ConfigProperties> getConfigurations(ConfigQueryParameter queryParameter);

    /**
     * 保存配置信息
     *
     * @param configProperties
     * @return
     */
    boolean saveConfiguration(ConfigProperties configProperties);

    /**
     * 删除配置信息
     *
     * @param configProperties
     * @return
     */
    boolean deleteConfiguration(ConfigProperties configProperties);

    /**
     * 编辑配置信息
     *
     * @param configProperties
     * @return
     */
    boolean updateConfiguration(ConfigProperties configProperties);

    /**
     * 获取微服务名列表
     *
     * @return
     */
    List<String> getApplicationNames();

    /**
     * 查询应用配置文件信息
     *
     * @param queryParameter
     * @return
     */
    List<ConfigPropertiesGroup> groupConfigurations(ConfigQueryParameter queryParameter);

    /**
     * 根据id集合删除配置
     *
     * @param ids 配置id集合 不能为空集合
     */
    void deleleByIds(List<String> ids);

    /**
     * 根据等值查询条件删除
     *
     * @param condition 等值查询条件,包括application(服务名称),label(分支), profile(环境), tenantId(租户id)
     */
    void delete(ConfigPropertiesGroup condition);
}

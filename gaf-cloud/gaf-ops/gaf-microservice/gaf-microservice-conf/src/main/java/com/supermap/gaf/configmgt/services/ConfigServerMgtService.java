/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.configmgt.services;

import com.supermap.gaf.commontypes.Page;
import com.supermap.gaf.configmgt.commontypes.ConfigPropertiesGroup;
import com.supermap.gaf.configmgt.commontypes.GroupWithProperties;
import com.supermap.gaf.configmgt.vo.ConfigPropertiesVo;
import com.supermap.gaf.microservice.ConfigQueryParameter;
import com.supermap.gaf.microservice.ServerConfigInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
 * @version 1.0 2019-8-9
 * @date:2021/3/25
 * @description
 */
public interface ConfigServerMgtService {


    /**
     * 获取所有的应用 配置信息
     *
     * @param queryParameter
     * @return 应用配置信息
     */
    Page<ConfigPropertiesGroup> pageConfigPropertiesGroups(ConfigQueryParameter queryParameter);

    /**
     * 保存用户输入的配置信息
     *
     * @param groupWithProperties 用户输入的配置
     * @return 保存状态
     */
    boolean saveConfig(GroupWithProperties groupWithProperties);


    /**
     * 获取所有服务名，已去重
     *
     * @return
     */
    List<String> getApplicationNames();

    /**
     * 检查服务名是否重复
     *
     * @param applicationName
     * @return
     */
    boolean checkApplicationName(String applicationName);

    /**
     * 批量更新配置信息
     *
     * @param groupWithProperties
     * @return
     */
    boolean batchUpdateConfig(GroupWithProperties groupWithProperties);

    /**
     * 刷新配置
     *
     * @param application
     * @return
     */
    boolean refreshConfiguration(String application);

    /**
     * 根据租户id和应用程序名称和分支及环境查新配置信息
     *
     * @param queryParameter 查询参数 有应用名称，环境，分支，及租户id
     * @return 返回应用配置信息不分页
     */
    List<ConfigPropertiesVo> listConfigPropertiesVo(ConfigQueryParameter queryParameter);

    /**
     * 根据id删除属性
     *
     * @param id 属性id
     * @return
     */
    boolean removeById(String id);

    /**
     * 根据id集合删除属性
     *
     * @param ids id集合
     * @return
     */
    boolean removeByIds(List<String> ids);

    /**
     * 根据条件批量删除配置 每个查询条件是application(服务名称，模糊匹配),label(分支), profile(环境), tenantId(租户id)
     *
     * @param conditions 查询条件集合
     * @return
     */
    boolean remove(List<ConfigPropertiesGroup> conditions);
}

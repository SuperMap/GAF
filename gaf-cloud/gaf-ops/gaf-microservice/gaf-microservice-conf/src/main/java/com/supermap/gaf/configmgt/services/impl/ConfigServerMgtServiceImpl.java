/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.configmgt.services.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.commontypes.Page;
import com.supermap.gaf.configmgt.commontypes.ConfigProperties;
import com.supermap.gaf.configmgt.commontypes.ConfigPropertiesGroup;
import com.supermap.gaf.configmgt.commontypes.GroupWithProperties;
import com.supermap.gaf.configmgt.util.MybatisBatchUtil;
import com.supermap.gaf.configmgt.vo.ConfigPropertiesVo;
import com.supermap.gaf.microservice.ConfigQueryParameter;
import com.supermap.gaf.configmgt.dao.ConfigurationDao;
import com.supermap.gaf.configmgt.event.ConfigurationChangedEvent;
import com.supermap.gaf.configmgt.services.ConfigServerMgtService;
import com.supermap.gaf.rest.utils.OKHttpUtil;
import com.supermap.gaf.utils.LogUtil;
import com.supermap.gaf.utils.PrimaryKeyUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
 * @version 1.0 2019-8-9
 * @date:2021/3/25
 * @description
 */
@Service
public class ConfigServerMgtServiceImpl implements ConfigServerMgtService {

    private static Logger logger = LogUtil.getLocLogger(ConfigServerMgtServiceImpl.class);

    @Autowired
    private ConfigurationDao configurationDao;
    @Autowired
    private ApplicationContext applicationContext;

    @Value("${local-address:empty}")
    private String localAddress;


    @Override
    public Page<ConfigPropertiesGroup> pageConfigPropertiesGroups(ConfigQueryParameter queryParameter) {
        PageInfo<ConfigPropertiesGroup> pageInfo = PageHelper.startPage(queryParameter.getPageIndex(), queryParameter.getPageSize()).doSelectPageInfo(() -> {
            configurationDao.groupConfigurations(queryParameter);
        });
        Page<ConfigPropertiesGroup> page = new Page<>();
        page.setTotal((int) pageInfo.getTotal());
        page.setPageIndex(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.calculateTotalPage();
        page.setContent(pageInfo.getList());
        return page;
    }

    @Override
    public List<ConfigPropertiesVo> listConfigPropertiesVo(ConfigQueryParameter queryParameter) {
        List<ConfigProperties> configurations = configurationDao.getConfigurations(queryParameter);
        List<ConfigPropertiesVo> result = configurations.stream().map(configProperties -> {
            ConfigPropertiesVo configPropertiesVo = new ConfigPropertiesVo();
            BeanUtils.copyProperties(configProperties, configPropertiesVo);
            return configPropertiesVo;
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public boolean removeById(String id) {
        ConfigProperties configProperties = new ConfigProperties();
        configProperties.setId(id);
        this.configurationDao.deleteConfiguration(configProperties);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeByIds(List<String> ids) {
        configurationDao.deleleByIds(ids);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(List<ConfigPropertiesGroup> conditions) {
        return MybatisBatchUtil.insertOrUpdateBatch(ConfigurationDao.class, conditions, ConfigurationDao::delete);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveConfig(GroupWithProperties groupWithProperties) {
        List<ConfigProperties> configServerInfos = parseGroupWithProperties(groupWithProperties);
        if (CollectionUtils.isNotEmpty(configServerInfos)) {
            return MybatisBatchUtil.insertOrUpdateBatch(ConfigurationDao.class, configServerInfos, ConfigurationDao::saveConfiguration);
        }
        return true;
    }

    private List<ConfigProperties> parseGroupWithProperties(GroupWithProperties parameter) {
        List<ConfigProperties> configServerInfoList = new ArrayList<>();
        if (parameter != null) {
            String application = parameter.getApplication();
            String profilesActive = parameter.getProfile();
            String label = parameter.getLabel();
            String tenantId = parameter.getTenantId();
            List<ConfigPropertiesVo> properties = parameter.getConfigPropertiesVos();
            for (ConfigPropertiesVo vo : properties) {
                if (vo != null) {
                    ConfigProperties configProperties = new ConfigProperties();
                    String id = StringUtils.isNoneBlank(vo.getId()) ? vo.getId() : PrimaryKeyUtil.getInstance().GetPrimaryKeyValue();
                    configProperties.setId(id);
                    configProperties.setApplication(application);
                    configProperties.setLabel(label);
                    configProperties.setTenantId(tenantId);
                    configProperties.setProfile(profilesActive);
                    configProperties.setPropertyKey(vo.getPropertyKey());
                    configProperties.setPropertyValue(vo.getPropertyValue());
                    configProperties.setDescription(vo.getDescription());
                    configServerInfoList.add(configProperties);
                }
            }
        }
        return configServerInfoList;
    }


    /**
     * 获取所有服务名，已去重
     *
     * @return
     */
    @Override
    public List<String> getApplicationNames() {
        return configurationDao.getApplicationNames();
    }

    /**
     * 检查服务名是否重复
     *
     * @return
     */
    @Override
    public boolean checkApplicationName(String applicationName) {
        boolean isExisted = false;
        for (String name : this.getApplicationNames()) {
            if (name.equals(applicationName)) {
                isExisted = true;
                break;
            }
        }
        return isExisted;
    }

    /**
     * 批量更新配置信息
     *
     * @param groupWithProperties
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean batchUpdateConfig(GroupWithProperties groupWithProperties) {
        List<ConfigProperties> oldConfigPropertiesList = configurationDao.getConfigurations(new ConfigQueryParameter(groupWithProperties.getApplication(), groupWithProperties.getProfile(), groupWithProperties.getLabel(), groupWithProperties.getTenantId()));
        List<ConfigPropertiesVo> configPropertiesVos = groupWithProperties.getConfigPropertiesVos();
        List<ConfigProperties> toAdd = new LinkedList<>();
        List<ConfigProperties> toUpdate = new LinkedList<>();
        for (ConfigPropertiesVo vo : configPropertiesVos) {
            boolean isExisted = false;
            for (ConfigProperties old : oldConfigPropertiesList) {
                if (Objects.equals(old.getId(), vo.getId())) {
                    isExisted = true;
                    if (!Objects.equals(old.getPropertyKey(), vo.getPropertyKey())
                            || !Objects.equals(old.getPropertyValue(), vo.getPropertyValue())
                            || !Objects.equals(old.getDescription(), vo.getDescription())
                    ) {
                        ConfigProperties configProperties = new ConfigProperties();
                        BeanUtils.copyProperties(vo, configProperties);
                        toUpdate.add(configProperties);
                    }
                    break;
                }
            }
            if (!isExisted) {
                ConfigProperties configProperties = new ConfigProperties();
                BeanUtils.copyProperties(vo, configProperties);
                configProperties.setApplication(groupWithProperties.getApplication());
                configProperties.setProfile(groupWithProperties.getProfile());
                configProperties.setLabel(groupWithProperties.getLabel());
                configProperties.setTenantId(groupWithProperties.getTenantId());
                configProperties.setId(PrimaryKeyUtil.getInstance().GetPrimaryKeyValue());
                toAdd.add(configProperties);
            }
        }
        List<String> toRemove = new LinkedList<>();
        for (ConfigProperties old : oldConfigPropertiesList) {
            List<ConfigPropertiesVo> collect = configPropertiesVos.stream().filter(vo -> Objects.equals(vo.getId(), old.getId())).collect(Collectors.toList());
            if (collect.size() == 0) {
                toRemove.add(old.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(toAdd)) {
            MybatisBatchUtil.insertOrUpdateBatch(ConfigurationDao.class, toAdd, ConfigurationDao::saveConfiguration);
        }
        if (CollectionUtils.isNotEmpty(toRemove)) {
            configurationDao.deleleByIds(toRemove);
        }
        if (CollectionUtils.isNotEmpty(toUpdate)) {
            MybatisBatchUtil.insertOrUpdateBatch(ConfigurationDao.class, toUpdate, ConfigurationDao::updateConfiguration);
        }
        this.publishEvent(groupWithProperties.getApplication());
        return true;
    }

    @Override
    public boolean refreshConfiguration(String application) {
        try {
            String url = StringUtils.isNoneBlank(application) ? "http://" + localAddress + "/actuator/bus-refresh/" + application : "http://" + localAddress + "/actuator/bus-refresh";
            logger.info(url);
            OKHttpUtil.postUseOkhttp(url, StringUtils.EMPTY);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }


    /**
     * @author <a href="mailto:wenyuanwu@gtmap.cn">wenyuanwu</a>
     * @description 发布一个配置改变事件
     */
    private void publishEvent(String application) {
        applicationContext.publishEvent(new ConfigurationChangedEvent(application));
    }
}

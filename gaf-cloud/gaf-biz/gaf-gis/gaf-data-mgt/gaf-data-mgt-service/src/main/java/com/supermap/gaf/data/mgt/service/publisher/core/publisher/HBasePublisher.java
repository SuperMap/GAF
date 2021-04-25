/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.service.publisher.core.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.HBaseParameter;
import com.supermap.gaf.data.mgt.entity.PublishServiceParameter;
import com.supermap.gaf.data.mgt.entity.RestInterfaceType;
import com.supermap.gaf.data.mgt.service.publisher.config.UrlConfig;
import com.supermap.gaf.data.mgt.service.publisher.constant.PublishStatusCode;
import com.supermap.gaf.data.mgt.service.publisher.core.AbstractPublisher;
import com.supermap.gaf.data.mgt.service.publisher.core.registry.PublisherType;
import com.supermap.gaf.data.mgt.common.IServerManager;
import com.supermap.gaf.data.mgt.service.publisher.util.HttpClientUtil;
import com.supermap.gaf.data.mgt.util.ComponentProviderParser;
import com.supermap.gaf.utils.LogUtil;
import com.supermap.server.config.ComponentSetting;
import com.supermap.server.config.ProviderSetting;
import com.supermap.services.components.commontypes.HBaseKerberosSetting;
import com.supermap.services.components.impl.DataImpl;
import com.supermap.services.components.impl.MapImpl;
import com.supermap.services.providers.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.supermap.services.rest.management.ServiceType.RESTDATA;
import static com.supermap.services.rest.management.ServiceType.RESTMAP;


/**
* @author:yw
* @Date 2021-3-12
 * hbase服务发布
 * 通过发布服务提供者和服务组件实现
 * @date:2021/3/25
 * 【注意】hbase不存在工作空间概念，且每个数据及都要发布成一个服务
 */

@Service
public class HBasePublisher extends AbstractPublisher {

    private static Logger logger = LogUtil.getLocLogger(HBasePublisher.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private ComponentProviderParser componentProviderParser;

    @Autowired
    private IServerManager iServerManager;

    /**---------------------以下变量为一次操作中的缓存，会在init()中赋值，在dispose()中销毁**/
    private List<Setting> settings = new ArrayList<>();

    @Override
    public MessageResult<String> verify(PublishServiceParameter originParameter) {
        // 该部分包括：服务类型校验、 数据源可达性校验
        try {
            HBaseParameter hBaseParameter = originParameter.getHBaseParameter();
            if (null == hBaseParameter) {
                return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_PARAMETER_NULL.getDescribe()).build();
            }
            // 1、服务类型校验
            List<String> serviceTypes = hBaseParameter.getServiceTypes();
            if (CollectionUtils.isEmpty(serviceTypes)) {
                return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_SERVICE_TYPE_NOT_SUPPORTED.getDescribe()).build();
            }
            HBaseParameter.SUPPORTSERVICETYPE[] supportServiceTypes = HBaseParameter.SUPPORTSERVICETYPE.values();
            Map<String, String> supportServiceTypeMap = new HashMap<String, String>(16);
            for (HBaseParameter.SUPPORTSERVICETYPE supportServiceType : supportServiceTypes) {
                supportServiceTypeMap.put(supportServiceType.name(), supportServiceType.name());
            }
            for (String serviceType : serviceTypes) {
                if (!supportServiceTypeMap.containsKey(serviceType)) {
                    return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_SERVICE_TYPE_NOT_SUPPORTED.getDescribe()).build();
                }
            }
            // 2、数据源可达性校验
            // 【注意】此处只能校验参数是否为空校验，不能真实确认是否可用
            if (StringUtils.isEmpty(hBaseParameter.getZookeeper()) || StringUtils.isEmpty(hBaseParameter.getCatalog())) {
                return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_DATASOURCE_UNAVAILABLE.getDescribe()).build();
            }
            return MessageResult.<String>successe(String.class).build();
        } catch (Exception e) {
            logger.error(PublishStatusCode.PUBLISH_EXCEPTION_PARAMETER_VALID_ERROR.getDescribe(), e);
            return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_PARAMETER_VALID_ERROR.getDescribe()).build();
        }
    }

    @Override
    public MessageResult<String> init() {
        try {
            // 原始参数
            PublishServiceParameter originParameter = this.getParameter();
            HBaseParameter hBaseParameter = originParameter.getHBaseParameter();

            String catalog = hBaseParameter.getCatalog();

            List<String> serviceTypes = hBaseParameter.getServiceTypes();
            List<String> allowDatasetNames = hBaseParameter.getAllowDatasetNames();
            for (String serviceType : serviceTypes) {
                String providerClassName = "";
                String componentClassName = "";
                // 此处生成的名称eg：map-ydcatalog1，后续根据条件判断是否添加数据集名称
                String providerName = "";
                if (RESTDATA.name().equalsIgnoreCase(serviceType)) {
                    providerClassName = HBaseDataProvider.class.getName();
                    componentClassName = DataImpl.class.getName();
                    providerName = String.format("data-%s", catalog);
                } else if (RESTMAP.name().equalsIgnoreCase(serviceType)) {
                    providerClassName = HBaseMapProvider.class.getName();
                    componentClassName = MapImpl.class.getName();
                    providerName = String.format("map-%s", catalog);
                }

                /**
                 * 【注意】数据服务是每个catalog发布一个服务；地图服务是每个数据集发布一个服务，如果入参中数据集列表为空则也发布为一个服务
                 * 每个传入的数据集都发布成一个服务，未传入则直接发布一个即可
                 * **/
                // 地图服务，且数据集列表不为空，每个数据集发布为一个服务
                if (RESTMAP.name().equalsIgnoreCase(serviceType) && !CollectionUtils.isEmpty(allowDatasetNames)) {
                    for (String allowDatasetName : allowDatasetNames) {
                        ProviderSetting providerSetting = createProviderSetting(hBaseParameter, Arrays.asList(allowDatasetName), providerName, providerClassName, serviceType);
                        ComponentSetting componentSetting = createComponentSetting(providerSetting.name, componentClassName);
                        Setting setting = new Setting(providerSetting, componentSetting, serviceType, catalog);
                        settings.add(setting);
                    }
                } else {
                    // 数据服务，或者数据集列表为空，直接发布为一个服务
                    ProviderSetting providerSetting = createProviderSetting(hBaseParameter, allowDatasetNames, providerName, providerClassName, serviceType);
                    ComponentSetting componentSetting = createComponentSetting(providerSetting.name, componentClassName);
                    Setting setting = new Setting(providerSetting, componentSetting, serviceType, catalog);
                    settings.add(setting);
                }
            }
            return MessageResult.<String>successe(String.class).build();
        } catch (Exception e) {
            logger.error(PublishStatusCode.PUBLISH_EXCEPTION_PARAMETER_INIT_ERROR.getDescribe(), e);
            return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_PARAMETER_INIT_ERROR.getDescribe()).build();
        }
    }

    /**
     * 创建服务提供者
     * 【说明】参数‘datasetName’不为空则提供者名称为：serviceType-catalog-datasetName，否则名称为：serviceType-catalog
     */
    private ProviderSetting createProviderSetting(HBaseParameter hBaseParameter, List<String> datasetNames, String providerName, String providerClassName, String serviceType) {

        String zookeeper = hBaseParameter.getZookeeper();
        String catalog = hBaseParameter.getCatalog();

        // 待发布的数据集列表
        List<FilteredDatasourceInfo> filteredDatasourceInfos = new ArrayList<>();
        FilteredDatasourceInfo filteredDatasourceInfo = new FilteredDatasourceInfo();
        filteredDatasourceInfo.datasourceName = catalog;
        if (!CollectionUtils.isEmpty(datasetNames)) {
            filteredDatasourceInfo.includedDatasetNames = datasetNames;
        }
        filteredDatasourceInfos.add(filteredDatasourceInfo);

        Object setting = null;
        // 地图服务
        if (RESTMAP.name().equalsIgnoreCase(serviceType)) {
            HBaseMapProviderSetting hBaseMapProviderSetting = new HBaseMapProviderSetting();
            hBaseMapProviderSetting.setZookeepers(zookeeper);
            hBaseMapProviderSetting.setCatalog(catalog);
            // 样式文件加载
            if (!StringUtils.isEmpty(hBaseParameter.getStylePath())) {
                hBaseMapProviderSetting.setStylePath(hBaseParameter.getStylePath());
            }
            // hbase安全认证
            if (hBaseParameter.isAuthentication()) {
                HBaseKerberosSetting hBaseKerberosSetting = new HBaseKerberosSetting();
                hBaseKerberosSetting.coreXml = hBaseParameter.getCoreXml();
                hBaseKerberosSetting.hbaseXml = hBaseParameter.getHbaseXml();
                hBaseKerberosSetting.hdfsXml = hBaseParameter.getHdfsXml();
                hBaseKerberosSetting.krb5ConfPath = hBaseParameter.getKrb5ConfPath();
                hBaseMapProviderSetting.setHbaseKerberosSetting(hBaseKerberosSetting);
            }
            hBaseMapProviderSetting.setFilterDatasource(filteredDatasourceInfos);
            hBaseMapProviderSetting.setThreadsPerCore(0);
            hBaseMapProviderSetting.setOutputPath("./output");
            hBaseMapProviderSetting.setOutputSite("http://{ip}:{port}/iserver/output/");
            setting = hBaseMapProviderSetting;
            // 地图服务，服务提供者名称为：map-catalog-datasetName
            providerName = String.format("%s-%s", providerName, datasetNames.get(0));
        } else if (
            // 数据服务
             RESTDATA.name().equalsIgnoreCase(serviceType)) {
            HBaseDataProviderSetting hBaseDataProviderSetting = new HBaseDataProviderSetting();
            hBaseDataProviderSetting.zookeepers = zookeeper;
            hBaseDataProviderSetting.catalog = catalog;
            hBaseDataProviderSetting.datasourceInfos = filteredDatasourceInfos;
            setting = hBaseDataProviderSetting;
            // 数据服务，服务提供者名称为：data-catalog
        }
        // 经过数据校验阶段，此setting一定创建成功
        return componentProviderParser.createProviderSetting(setting, providerName, providerClassName);
    }

    /**
     * 创建服务组件信息
     * 【说明】参数‘name’即为组件名称，设定为与服务提供者名称一样
     */
    private ComponentSetting createComponentSetting(String name, String componentClassName) {
        return componentProviderParser.createComponentSetting(name, name, RestInterfaceType.REST, componentClassName, null);
    }

    @Override
    public MessageResult<String> doPublish(String iServerUrl, String token) {
        // 本次发布过程中已经发布的服务实例，如果发布过程中出现异常，则作为删除本次已发布的服务依据
        List<String> publishedInstances = new ArrayList<>();
        try {
            if (CollectionUtils.isEmpty(settings)) {
                return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION.getDescribe()).build();
            }
            // 获取当前iserver上的服务实例，判定当前服务是否需要发布
            Set<String> instanceSet = iServerManager.getCurrentInstanceName();
            // 得到需要发布的服务，注意不是服务类型，此处与工作空间发布不一样
            for (Setting setting : settings) {
                ProviderSetting providerSetting = setting.getProviderSetting();
                String instanceName = providerSetting.name;
                if (!instanceSet.contains(instanceName)) {
                    publishServer(iServerUrl, token, publishedInstances, setting, providerSetting, instanceName);
                }
            }
            return MessageResult.<String>successe(String.class).build();
        } catch (Exception e) {
            logger.error("发布异常，删除其他已发布的服务", e);
            // 删除本次发布过程中出现异常后
            if (!CollectionUtils.isEmpty(publishedInstances)) {
                for (String publishedInstance : publishedInstances) {
                    MessageResult<String> removeResult = iServerManager.removeInstance(publishedInstance);
                    if (!removeResult.isSuccessed()) {
                        logger.error(removeResult.getMessage());
                    }
                }
            }
            return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION.getDescribe()).build();
        }
    }

    /**
     * 发布iserver服务
     */
    private void publishServer(String iServerUrl, String token, List<String> publishedInstances, Setting setting, ProviderSetting providerSetting, String instanceName) throws Exception {
        // 1、发布服务提供者
        String providerParameter = MAPPER.writeValueAsString(providerSetting);
        String publishProviderUrl = UrlConfig.getProvidersManagerUrl(iServerUrl, token);
        Object providerRespStr = HttpClientUtil.execute(publishProviderUrl, providerParameter);
        if (null == providerRespStr) {
            throw new Exception("发布服务提供者异常");
        }
        // 2、发布服务组件
        ComponentSetting componentSetting = setting.getComponentSetting();
        String componentParameter = MAPPER.writeValueAsString(componentSetting);
        String componentProviderUrl = UrlConfig.getComponentManagerUrl(iServerUrl, token);
        Object commonentRespStr = HttpClientUtil.execute(componentProviderUrl, componentParameter);
        if (null == commonentRespStr) {
            throw new Exception("发布服务组件异常");
        }
        // 3、发布成功后缓存实例名称
        publishedInstances.add(instanceName);
    }

    @Override
    public void dispose() {
        // 注意，可能在置空的瞬间有新的参数进入，考虑加锁问题，解决方案，在调度器中执行发布流程整个过程加上同步锁，故此处不再考虑此问题
        settings.clear();
    }

    @Override
    public String supportPublisher() {
        return PublisherType.HBASE.getType();
    }

    class Setting {
        private ProviderSetting providerSetting;
        private ComponentSetting componentSetting;

        // 服务类型
        private String serviceType;
        // 数据库名称
        private String catalog;

        public Setting(ProviderSetting providerSetting, ComponentSetting componentSetting, String serviceType, String catalog) {
            this.providerSetting = providerSetting;
            this.componentSetting = componentSetting;
            this.serviceType = serviceType;
            this.catalog = catalog;
        }

        public ProviderSetting getProviderSetting() {
            return providerSetting;
        }

        public void setProviderSetting(ProviderSetting providerSetting) {
            this.providerSetting = providerSetting;
        }

        public ComponentSetting getComponentSetting() {
            return componentSetting;
        }

        public void setComponentSetting(ComponentSetting componentSetting) {
            this.componentSetting = componentSetting;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }

        public String getCatalog() {
            return catalog;
        }

        public void setCatalog(String catalog) {
            this.catalog = catalog;
        }
    }
}

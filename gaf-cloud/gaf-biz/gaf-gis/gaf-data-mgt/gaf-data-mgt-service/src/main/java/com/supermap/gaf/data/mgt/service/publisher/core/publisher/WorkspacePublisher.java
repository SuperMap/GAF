/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service.publisher.core.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermap.data.*;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.common.IServerManager;
import com.supermap.gaf.data.mgt.entity.*;
import com.supermap.gaf.data.mgt.service.MapTemplateService;
import com.supermap.gaf.data.mgt.service.publisher.config.UrlConfig;
import com.supermap.gaf.data.mgt.service.publisher.constant.PublishStatusCode;
import com.supermap.gaf.data.mgt.service.publisher.core.AbstractPublisher;
import com.supermap.gaf.data.mgt.service.publisher.core.registry.PublisherType;
import com.supermap.gaf.data.mgt.service.publisher.util.HttpClientUtil;
import com.supermap.gaf.data.mgt.service.publisher.util.WorkspaceParser;
import com.supermap.gaf.data.mgt.util.TemplateMapParser;
import com.supermap.gaf.utils.FileUtil;
import com.supermap.gaf.utils.LogUtil;
import com.supermap.services.providers.FilteredDatasourceInfo;
import com.supermap.services.providers.util.CommontypesConversion;
import com.supermap.services.rest.management.ServiceType;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;

import static com.supermap.services.rest.management.ServiceType.*;

/**
* @author:yw
* @Date 2021-3-12
 * @date:2021/3/25
 * 工作空间服务发布者
 */
@Service
public class WorkspacePublisher extends AbstractPublisher {

    private static Logger logger = LogUtil.getLocLogger(WorkspacePublisher.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String POINT ="POINT";
    private static final String LINE ="LINE";
    private static final String REGION ="REGION";
    /**
     * 地图比例尺常量
     */
    private static final double[] SCALES = {1 / 295829355.454155, 1 / 147914677.727296, 1 / 73957338.8636482, 1 / 36978669.4318241, 1 / 18489334.715912,
            1 / 9244667.35795517, 1 / 4622333.67897758, 1 / 2311166.83948879, 1 / 1155583.4197444, 1 / 577791.709872198, 1 / 288895.854936099,
            1 / 144447.92746805, 1 / 72223.9637340248, 1 / 36111.9818670124, 1 / 18055.9909335062, 1 / 9027.9954667531, 1 / 4513.99773337655,
            1 / 2256.99886668828, 1 / 1128.49943334414};

    @Autowired
    private WorkspaceParser workspaceParser;

    @Autowired
    private MapTemplateService mapTemplateService;

    @Autowired
    private TemplateMapParser templateMapParser;

    @Autowired
    private IServerManager iServerManager;

    /**
     * iserver发布工作空间参数
     */
    private com.supermap.services.rest.management.PublishServiceParameter publishParameter;


    //---------------------以下变量为一次操作中的缓存，会在init()中赋值，在dispose()中销毁
    /**
     * 数据库型工作空间缓存
     */
    private Workspace dbWorkspace = null;

    @Override
    public MessageResult<String> verify(PublishServiceParameter originParameter) {
        try {
            WorkspaceParameter workspaceParameter = originParameter.getWorkspaceParameter();
            if (null == workspaceParameter) {
                return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_PARAMETER_NULL.getDescribe()).build();
            }

            // 1、工作空间类型是否支持校验
            boolean supportWorkspaceType = Boolean.FALSE;
            String workspaceType = workspaceParameter.getWorkspaceType();
            for (WorkspaceParameter.WORKSPACETYPE workspacetype : WorkspaceParameter.WORKSPACETYPE.values()) {
                if (workspacetype.name().equalsIgnoreCase(workspaceType)) {
                    supportWorkspaceType = Boolean.TRUE;
                    break;
                }
            }
            if (!supportWorkspaceType) {
                return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_WORKSPACE_TYPE_NOT_SUPPORTED.getDescribe()).build();
            }

            // 2、服务发布类型校验，暂时只支持RESTMAP\RESTDATA\RESTREALSPACE\RESTSPATIALANALYST
            if (checkServiceTypes(workspaceParameter)) {
                return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_SERVICE_TYPE_NOT_SUPPORTED.getDescribe()).build();
            }

            // 3、数据源可达性校验
            //【注意】a、文件型数据源的源文件在和iserver同一服务器上，此处并不能做工作空间或者文件是否存在校验，只能校验参数中文件路径不能为空
            //           文件型工作空间合法校验会在调用iserver的发布接口中，iserver自己校验
            // b、数据库型工作空间在本方法流程中校验
            if (checkDatasourceAvailable(workspaceParameter, workspaceType)) {
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
        // 根据参数构建工作空间，放入地图模板、资源等，最后构建一个iserver需要的【ServicePublishParameter】交给 doPublish方法开始发布
        try {
            // 原始参数
            PublishServiceParameter originParameter = this.getParameter();
            WorkspaceParameter workspaceParameter = originParameter.getWorkspaceParameter();

            // 工作空间连接信息，用于组装iserver发布服务的参数
            String workspaceConnectionInfo = "";

            // 数据库工作空间额外属性设置
            if (WorkspaceParameter.WORKSPACETYPE.DB.name().equalsIgnoreCase(workspaceParameter.getWorkspaceType())) {
                DatasourceConnectionInfo datasourceConnectionInfo = workspaceParser.getDatasourceConnectionInfo(workspaceParameter.getDbServer());
                if (null == datasourceConnectionInfo) {
                    return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_WORKSPACE_UNAVAILABLE.getDescribe()).build();
                }
                dbWorkspace = workspaceParser.createWorkspace(datasourceConnectionInfo, null);
                if (null == dbWorkspace) {
                    return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_WORKSPACE_UNAVAILABLE.getDescribe()).build();
                }
                Datasource datasource = dbWorkspace.getDatasources().get(datasourceConnectionInfo.getAlias());
                if (null == datasource){
                    return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_DATASOURCE_UNAVAILABLE.getDescribe()).build();
                }
                boolean isOpened = datasource.isOpened();
                if (!isOpened){
                    dbWorkspace.getDatasources().open(datasourceConnectionInfo);
                }
                // 【注意】如果发布的服务类型中存在地图服务，需要加载地图模板和资源，被允许发布的数据集才能生成在工作空间下生成地图
                if (workspaceParameter.getServiceTypes().contains(RESTMAP.name())) {
                    // 1、数据库型工作空间添加地图模板，包括配置的默认模板，当传入的参数中模板找不到，则赋予对应空间类型（点、线、面）的默认模板
                    MessageResult<String> applyMapTemplateResult = applyMapTemplate(workspaceParameter);
                    if (!applyMapTemplateResult.isSuccessed()) {
                        return applyMapTemplateResult;
                    }

                    // 2、加载资源到工作空间
                    MessageResult<String> loadResourceResult = loadResource(workspaceParameter);
                    if (!loadResourceResult.isSuccessed()) {
                        return loadResourceResult;
                    }
                }
                WorkspaceConnectionInfo workspaceConnectionInfoWithDatasource = workspaceParser.getWorkspaceConnectionInfo(datasourceConnectionInfo, null);
                workspaceConnectionInfo = CommontypesConversion.getWorkspaceConnectionInfo(workspaceConnectionInfoWithDatasource).toStandardString();
                // 修改工作空间后，保存变更
                dbWorkspace.save();
            } else if (WorkspaceParameter.WORKSPACETYPE.FILE.name().equalsIgnoreCase(workspaceParameter.getWorkspaceType())) {
                // 【注意】此处文件型工作空间的连接信息只用了文件路径，如果有密码怎么办？？？
                FileServer fileServer = workspaceParameter.getFileServer();
                workspaceConnectionInfo = fileServer.getFilePath();
            }

            List<String> serviceTypes = workspaceParameter.getServiceTypes();
            ServiceType[] serviceTypesArray = new ServiceType[serviceTypes.size()];
            for (int i = 0; i < serviceTypes.size(); i++) {
                serviceTypesArray[i] = ServiceType.valueOf(serviceTypes.get(i));
            }

            // 构建iserver发布工作空间的参数 publishParameter
            buildPublishParameter(workspaceParameter, workspaceConnectionInfo, serviceTypesArray);
            return MessageResult.<String>successe(String.class).build();
        } catch (Exception e) {
            logger.error(PublishStatusCode.PUBLISH_EXCEPTION_PARAMETER_INIT_ERROR.getDescribe(), e);
            return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_PARAMETER_INIT_ERROR.getDescribe()).build();
        }
    }

    /**
     * 构建iserver发布工作空间的参数 publishParameter
     */
    private void buildPublishParameter(WorkspaceParameter workspaceParameter, String workspaceConnectionInfo, ServiceType[] serviceTypesArray) {
        publishParameter = new com.supermap.services.rest.management.PublishServiceParameter();
        publishParameter.workspaceConnectionInfo = workspaceConnectionInfo;
        publishParameter.isMultiInstance = Boolean.FALSE;
        publishParameter.servicesTypes = serviceTypesArray;
        List<String> allowDatasetNames = workspaceParameter.getAllowDatasetNames();
        if (!CollectionUtils.isEmpty(allowDatasetNames)) {
            FilteredDatasourceInfo filteredDatasourceInfo = new FilteredDatasourceInfo();
            filteredDatasourceInfo.includedDatasetNames = allowDatasetNames;
            List<FilteredDatasourceInfo> filteredDatasourceInfos = new ArrayList<>();
            filteredDatasourceInfos.add(filteredDatasourceInfo);
            publishParameter.datasourceInfos = filteredDatasourceInfos;
        }
    }

    @Override
    public MessageResult<String> doPublish(String iServerUrl, String token) {
        // 【注意】每个工作空间下的数据服务、空间分析服务对应的服务实例最多只能有一个，故需要在此过滤出是否需要真正发布数据服务和空间分析服务
        try {
            if (null == publishParameter) {
                return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION.getDescribe()).build();
            }
            String workspaceName = "";
            // 数据库型工作空间，直接从工作空间获取
            if (null != dbWorkspace) {
                workspaceName = dbWorkspace.getCaption();
            } else {
                // 文件型工作空间，从连接信息中获取文件名称
                String workspaceConnectionInfo = publishParameter.workspaceConnectionInfo;
                workspaceName = FilenameUtils.getBaseName(workspaceConnectionInfo);
            }
            // 1、过滤得到需要的服务发布类型
            ServiceType[] servicesTypes = publishParameter.servicesTypes;
            List<ServiceType> serviceTypes = filterServiceType(servicesTypes, workspaceName);

            if (serviceTypes.size() > 0 && doPublishServer(iServerUrl, token, serviceTypes)) {
                // 得到需要发布的服务类型进行发布
                return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION.getDescribe()).build();
            }

            return MessageResult.<String>successe(String.class).build();
        } catch (Exception e) {
            logger.error(PublishStatusCode.PUBLISH_EXCEPTION.getDescribe(), e);
            return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION.getDescribe()).build();
        }
    }

    /**
     * 执行服务发布
     */
    private boolean doPublishServer(String iServerUrl, String token, List<ServiceType> serviceTypes) throws JsonProcessingException {
        ServiceType[] needPublishServiceTypes = new ServiceType[serviceTypes.size()];
        serviceTypes.toArray(needPublishServiceTypes);
        publishParameter.servicesTypes = needPublishServiceTypes;
        // 2、执行发布
        String parameter = MAPPER.writeValueAsString(publishParameter);
        String publishWorkspaceUrl = UrlConfig.getPublishWorkspaceUrl(iServerUrl, token);
        Object respStr = HttpClientUtil.execute(publishWorkspaceUrl, parameter);
        if (null == respStr) {
            return true;
        }
        return false;
    }


    /**
     * 数据源可达性校验
     */
    private boolean checkDatasourceAvailable(WorkspaceParameter workspaceParameter, String workspaceType) {
        boolean isDatasourceAvailable = Boolean.TRUE;
        if (WorkspaceParameter.WORKSPACETYPE.FILE.name().equalsIgnoreCase(workspaceType)) {
            FileServer fileServer = workspaceParameter.getFileServer();
            if (null == fileServer || StringUtils.isEmpty(fileServer.getFilePath())) {
                isDatasourceAvailable = Boolean.FALSE;
            }
        } else if (WorkspaceParameter.WORKSPACETYPE.DB.name().equalsIgnoreCase(workspaceType)) {
            isDatasourceAvailable = checkDBDatasourceAvailable(workspaceParameter, isDatasourceAvailable);
        }
        if (!isDatasourceAvailable) {
            return true;
        }
        return false;
    }

    /**
     * 数据库数据源可达性校验
     */
    private boolean checkDBDatasourceAvailable(WorkspaceParameter workspaceParameter, boolean isDatasourceAvailable) {
        DBServer dbServer = workspaceParameter.getDbServer();
        if (null == dbServer) {
            isDatasourceAvailable = Boolean.FALSE;
        }
        // 数据库型连接信息连通性校验
        // a、获取连接信息
        DatasourceConnectionInfo datasourceConnectionInfo = workspaceParser.getDatasourceConnectionInfo(dbServer);
        if (null == datasourceConnectionInfo) {
            isDatasourceAvailable = Boolean.FALSE;
        }
        // b、创建工作空间
        Workspace tempDbWorkspace = null;
        try {
            tempDbWorkspace = workspaceParser.createWorkspace(datasourceConnectionInfo, null);
            if (null == tempDbWorkspace) {
                isDatasourceAvailable = Boolean.FALSE;
            }
        } catch (Exception e) {
            isDatasourceAvailable = Boolean.FALSE;
            logger.error(PublishStatusCode.PUBLISH_EXCEPTION_DATASOURCE_UNAVAILABLE.getDescribe(), e);
        } finally {
            if (null != tempDbWorkspace) {
                datasourceConnectionInfo.dispose();
                tempDbWorkspace.close();
                tempDbWorkspace.dispose();
            }
        }
        return isDatasourceAvailable;
    }

    /**
     * 校验服务发布类型
     */
    private boolean checkServiceTypes(WorkspaceParameter workspaceParameter) {
        List<String> serviceTypes = workspaceParameter.getServiceTypes();
        if (CollectionUtils.isEmpty(serviceTypes)) {
            return true;
        }
        WorkspaceParameter.SUPPORTSERVICETYPE[] supportServiceTypes = WorkspaceParameter.SUPPORTSERVICETYPE.values();
        Map<String, String> supportServiceTypeMap = new HashMap<String, String>(16);
        for (WorkspaceParameter.SUPPORTSERVICETYPE supportServiceType : supportServiceTypes) {
            supportServiceTypeMap.put(supportServiceType.name(), supportServiceType.name());
        }
        for (String serviceType : serviceTypes) {
            if (!supportServiceTypeMap.containsKey(serviceType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 过滤得到需要发布的服务类型
     */
    private List<ServiceType> filterServiceType(ServiceType[] servicesTypes, String workspaceName) {
        List<ServiceType> serviceTypeList = new ArrayList<>();
        // 获取当前iserver上的服务实例，判定当前服务是否需要发布
        Set<String> instanceSet = iServerManager.getCurrentInstanceName();
        for (ServiceType serviceType : servicesTypes) {
            if (RESTDATA.name().equalsIgnoreCase(serviceType.name())) {
                String instanceName = String.format("data-%s", workspaceName);
                if (!instanceSet.contains(instanceName)) {
                    serviceTypeList.add(serviceType);
                }
            } else if (RESTSPATIALANALYST.name().equalsIgnoreCase(serviceType.name())) {
                String instanceName = String.format("spatialAnalysis-%s", workspaceName);
                if (!instanceSet.contains(instanceName)) {
                    serviceTypeList.add(serviceType);
                }
            } else {
                serviceTypeList.add(serviceType);
            }
        }
        return serviceTypeList;
    }

    @Override
    public void dispose() {
        if (null != publishParameter) {
            publishParameter = null;
        }
        if (null != dbWorkspace) {
            dbWorkspace.close();
            dbWorkspace.dispose();
            dbWorkspace = null;
        }
    }

    @Override
    public String supportPublisher() {
        return PublisherType.WORKSPACE.getType();
    }

    /**
     * 应用地图模板
     */
    private MessageResult<String> applyMapTemplate(WorkspaceParameter workspaceParameter) {
        logger.info(PublishStatusCode.PUBLISH_EXCEPTION_MAP_TEMPLATE_APPLY_START.getDescribe());
        try {
            Map<String, List<MapTemplate>> mapTemplateMap = getMapTemplate(workspaceParameter);
            if (MapUtils.isEmpty(mapTemplateMap)) {
                logger.warn(PublishStatusCode.PUBLISH_EXCEPTION_MAP_TEMPLATE_UNAVAILABLE.getDescribe());
            }
            // 1.3 加载地图模板到工作空间
            Datasource datasource = dbWorkspace.getDatasources().get(0);
            Datasets datasets = datasource.getDatasets();
            List<String> allowDatasetNames = workspaceParameter.getAllowDatasetNames();
            // 是否设置允许发布的数据集名称标志
            boolean isSetAllowDatasetNamesFlag = Boolean.TRUE;
            if (CollectionUtils.isEmpty(allowDatasetNames)) {
                isSetAllowDatasetNamesFlag = Boolean.FALSE;
            }
            for (int i = 0; i < datasets.getCount(); i++) {
                Dataset dataset = datasets.get(i);
                String datasetName = dataset.getName();
                if (isSetAllowDatasetNamesFlag) {
                    // 只生成允许发布的数据集对应的地图
                    if (allowDatasetNames.contains(datasetName)) {
                        generateMap(mapTemplateMap, datasource, dataset, datasetName);
                    }
                } else {// 生成全部空间数据集对应的地图
                    generateMap(mapTemplateMap, datasource, dataset, datasetName);
                }
            }
            return MessageResult.<String>successe(String.class).build();
        } catch (Exception e) {
            logger.error(PublishStatusCode.PUBLISH_EXCEPTION_MAP_TEMPLATE_APPLY_ERROR.getDescribe(), e);
            return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_MAP_TEMPLATE_APPLY_ERROR.getDescribe()).build();
        }
    }

    /**
     * 获取地图模板
     */
    private Map<String, List<MapTemplate>> getMapTemplate(WorkspaceParameter workspaceParameter) {
        Map<String, List<MapTemplate>> mapTemplateMap = new HashMap<String, List<MapTemplate>>(16);
        // 1.1、获取从参数中获取地图模板
        Map<String, Set<String>> mapTemplateParameterMap = workspaceParameter.getMapTemplates();
        if (MapUtils.isNotEmpty(mapTemplateParameterMap)) {
            for (Map.Entry<String, Set<String>> entry : mapTemplateParameterMap.entrySet()) {
                groupByDataset(mapTemplateMap, entry);
            }
        }
        // 1.2 配置的默认地图模板
        Map<String, MapTemplate> defaultMapTemplate = mapTemplateService.getDefaultMapTemplate();
        if (MapUtils.isNotEmpty(defaultMapTemplate)) {
            for (Map.Entry<String, MapTemplate> entry : defaultMapTemplate.entrySet()) {
                mapTemplateMap.put(entry.getKey(), Arrays.asList(entry.getValue()));
            }
        }
        return mapTemplateMap;
    }

    /**
     * 根据数据集名称将模板分组
     */
    private void groupByDataset(Map<String, List<MapTemplate>> mapTemplateMap, Map.Entry<String, Set<String>> entry) {
        List<MapTemplate> mapTemplates = new ArrayList<>();
        Set<String> urls = entry.getValue();
        for (String url : urls) {
            MessageResult<MapTemplate> mapTemplateMessageResult = mapTemplateService.getMapTemplateContentByUrl(url);
            if (mapTemplateMessageResult.isSuccessed() && null != mapTemplateMessageResult.getData()) {
                mapTemplates.add(mapTemplateMessageResult.getData());
            }
        }
        if (!CollectionUtils.isEmpty(mapTemplates)) {
            mapTemplateMap.put(entry.getKey(), mapTemplates);
        }
    }

    /**
     * 生成地图，一个数据集可对应多个地图模板
     */
    private boolean generateMap(Map<String, List<MapTemplate>> mapTemplateMap, Datasource datasource, Dataset dataset, String datasetName) {

        // 是否使用默认模板
        boolean isUseDefaultTemplate = Boolean.FALSE;

        List<MapTemplate> mapTemplates = mapTemplateMap.get(datasetName);
        if (null == mapTemplates) {
            String datasetType = dataset.getType().name();
            if (datasetType.contains(POINT)) {
                mapTemplates = mapTemplateMap.get("Default_Point");
            } else if (datasetType.contains(LINE)) {
                mapTemplates = mapTemplateMap.get("Default_Line");
            } else if (datasetType.contains(REGION)) {
                mapTemplates = mapTemplateMap.get("Default_Region");
            }
            isUseDefaultTemplate = Boolean.TRUE;
        }
        if (CollectionUtils.isEmpty(mapTemplates)) {
            return Boolean.TRUE;
        }

        // 是否一个数据集存在多个模板
        boolean isMultiInstance = mapTemplates.size() > 1;

        for (int i = 0; i < mapTemplates.size(); i++) {
            MapTemplate mapTemplate = mapTemplates.get(i);
            // 创建临时地图
            com.supermap.mapping.Map tempMap = templateMapParser.getTemplateMap(datasource.getAlias(), datasetName, mapTemplate.content, dbWorkspace).getData();
            // 设置天地图比例尺
            tempMap.setVisibleScalesEnabled(Boolean.TRUE);
            tempMap.setVisibleScales(SCALES);
            // 设置视图范围bounds
            tempMap.viewEntire();
            Rectangle2D rectangle2D = tempMap.getBounds();
            if (rectangle2D != null && rectangle2D.getLeft() > 0 && rectangle2D.getRight() > 0 && rectangle2D.getTop() > 0 && rectangle2D.getBottom() > 0) {
                tempMap.setViewBounds(tempMap.getBounds());
            }
            /** 保存地图到所使用的的工作空间下
             地图名称设置规则：1、多个模板为：模板名+数据集名；2、1个模板或0个模板（即使用默认模板）为：数据集名**/
            // 1个模板或0个模板
            String mapName = datasetName;
            if (isMultiInstance) {
                // 多个模板
                mapName = String.format("%s_%s", tempMap.getName(), datasetName);
            }
            com.supermap.gaf.data.mgt.util.MapUtils.saveMap(dbWorkspace, tempMap, mapName);
        }
        return Boolean.FALSE;
    }

    /**
     * 为工作空间加载资源
     * 资源地址必须要是能找到的磁盘地址，故现在下载资源到本地临时文件，然后加载到工作空间
     */
    private MessageResult<String> loadResource(WorkspaceParameter workspaceParameter) {
        Map<String, String> resourceMap = workspaceParameter.getResources();
        if (MapUtils.isEmpty(resourceMap)) {
            logger.info(PublishStatusCode.PUBLISH_EXCEPTION_MAP_RESOURCE_NULL.getDescribe());
            return MessageResult.successe(String.class).build();
        }

        logger.info(PublishStatusCode.PUBLISH_EXCEPTION_MAP_RESOURCE_LOAD_START.getDescribe());

        Resources resource = dbWorkspace.getResources();
        String resourceDirPath = "/tmp/mapresource";
        try {

            // 1、下载资源到临时本地文件
            FileUtil.download(resourceMap.get("MarkerLibrary"), resourceDirPath, "MarkerLibrary.sym");
            FileUtil.download(resourceMap.get("LineLibrary"), resourceDirPath, "LineLibrary.lsl");
            FileUtil.download(resourceMap.get("FillLibrary"), resourceDirPath, "FillLibrary.bru");

            // 2、加载资源到工作空间
            resource.getMarkerLibrary().fromFile(resourceDirPath + File.separatorChar + "MarkerLibrary.sym");
            resource.getLineLibrary().fromFile(resourceDirPath + File.separatorChar + "LineLibrary.lsl");
            resource.getFillLibrary().fromFile(resourceDirPath + File.separatorChar + "FillLibrary.bru");
            dbWorkspace.save();
            logger.info(PublishStatusCode.PUBLISH_EXCEPTION_MAP_RESOURCE_LOAD_SUCCESS.getDescribe());
        } catch (Exception e) {
            logger.error(PublishStatusCode.PUBLISH_EXCEPTION_MAP_REOSURCE_LOAD_ERROR.getDescribe(), e);
            return MessageResult.failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_MAP_REOSURCE_LOAD_ERROR.getDescribe() + e.getMessage()).build();
        }
        return MessageResult.successe(String.class).build();
    }
}

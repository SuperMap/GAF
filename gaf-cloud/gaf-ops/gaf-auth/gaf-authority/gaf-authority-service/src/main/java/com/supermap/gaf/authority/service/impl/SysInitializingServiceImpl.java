/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthResourceApi;
import com.supermap.gaf.authority.commontype.SysComponent;
import com.supermap.gaf.authority.constant.DbFieldNameConstant;
import com.supermap.gaf.authority.constant.SysCatalogConstant;
import com.supermap.gaf.authority.enums.ComponentTypeEnum;
import com.supermap.gaf.authority.enums.ResourceApiMethodEnum;
import com.supermap.gaf.authority.service.AuthResourceApiService;
import com.supermap.gaf.authority.service.SysComponentService;
import com.supermap.gaf.authority.service.SysInitializingService;
import com.supermap.gaf.sys.mgt.commontype.SysCatalog;
import com.supermap.gaf.sys.mgt.service.SysCatalogService;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.supermap.gaf.sys.mgt.enums.CatalogTypeEnum.API_GROUP_TYPE;


/**
 * 系统初始化服务实现类
 *
 * @author dqc
 * @date:2021/3/25
 */
@Service
public class SysInitializingServiceImpl implements SysInitializingService {
    private final SysComponentService sysComponentService;
    private final SysCatalogService sysCatalogService;
    private final AuthResourceApiService authResourceApiService;


    @Value("${spring.application.name}")
    private String applicationName;

    private static final String APPLICATION_NAME_CN = "系统与权限控制";

    public SysInitializingServiceImpl(SysComponentService sysComponentService, SysCatalogService sysCatalogService, AuthResourceApiService authResourceApiService) {
        this.sysComponentService = sysComponentService;
        this.sysCatalogService = sysCatalogService;
        this.authResourceApiService = authResourceApiService;
    }

    @Override
    public SysComponent initializeSysComponent() {
        List<SysComponent> sysComponents = sysComponentService.selectByOneField(DbFieldNameConstant.NAME, applicationName);
        if (CollectionUtils.isEmpty(sysComponents)) {
            SysComponent sysComponent =
                    SysComponent.builder()
                            .sysComponentId(UUID.randomUUID().toString())
                            .type(ComponentTypeEnum.FRONT_BACK_END.getValue())
                            .name(applicationName)
                            .nameCn(applicationName)
                            .type(ComponentTypeEnum.BACKEND.getValue())
                            .status(true)
                            .build();
            sysComponentService.insertSysComponent(sysComponent);
            return sysComponent;
        }
        return sysComponents.get(0);
    }

    @Override
    public void initializeSysCatalog(SysComponent sysComponent, Swagger swagger) {
        //寻找父目录
        List<SysCatalog> sysCatalogByComponentAndType = sysCatalogService.getByComponentAndType(sysComponent.getSysComponentId(), API_GROUP_TYPE.getValue());
        List<SysCatalog> sysCatalogByComponentAndTypeAndParentId = sysCatalogByComponentAndType.stream().filter(item -> item.getParentId().equals(SysCatalogConstant.PLATFORM_LEVEL)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(sysCatalogByComponentAndTypeAndParentId)) {
            if (sysCatalogService.insertSysCatalog(sysComponent, API_GROUP_TYPE)) {
                sysCatalogByComponentAndType = sysCatalogService.getByComponentAndType(sysComponent.getSysComponentId(), API_GROUP_TYPE.getValue());
                sysCatalogByComponentAndTypeAndParentId = sysCatalogByComponentAndType.stream().filter(item -> item.getParentId().equals(SysCatalogConstant.PLATFORM_LEVEL)).collect(Collectors.toList());
            }
        }
        SysCatalog parentSysCatalog = sysCatalogByComponentAndTypeAndParentId.get(0);
        //构建目录
        Map<String, Path> swaggerPathMap = swagger.getPaths();
        Set<String> keySet = swaggerPathMap.keySet();
        for (String key : keySet) {
            Path path = swaggerPathMap.get(key);
            //忽略具体方法，取第一个operation,主要获取tag，生成目录
            List<String> tags = path.getOperations().get(0).getTags();
            //tags从后到前插入数据库目录表
            String parentId = parentSysCatalog.getCatalogId();
            //每一个api插入所有tags生成目录后，重新获取目录列表
            List<SysCatalog> reSelectSysCatalogByComponentAndType = sysCatalogService.getByComponentAndType(sysComponent.getSysComponentId(), API_GROUP_TYPE.getValue());
            for (int i = tags.size() - 1; i >= 0; i--) {
                String tag = tags.get(i);
                //重新获取目录
                List<SysCatalog> sysCatalogFilterByName = reSelectSysCatalogByComponentAndType.stream().filter(item -> item.getName().equals(tag)).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(sysCatalogFilterByName)) {
                    //如果name不匹配，插入目录，并使pid为新增的目录id
                    SysCatalog sysCatalog =
                            SysCatalog.builder()
                                    .status(true)
                                    .type(API_GROUP_TYPE.getValue())
                                    .parentId(parentId)
                                    .sysComponentId(sysComponent.getSysComponentId())
                                    .name(tag)
                                    .catalogId(UUID.randomUUID().toString())
                                    .build();
                    sysCatalogService.insertSysCatalog(sysCatalog);
                    parentId = sysCatalog.getCatalogId();
                } else {
                    //如果name匹配，pid为匹配的目录id
                    parentId = sysCatalogFilterByName.get(0).getCatalogId();
                }
                if (i == 0) {
                    //如果每个api的tags遍历完，重置pid为根部目录id
                    parentId = parentSysCatalog.getCatalogId();
                }
            }
        }
    }

    @Override
    public void initializeSysResourceApi(SysComponent sysComponent, Swagger swagger) {
        Map<String, Path> swaggerPathMap = swagger.getPaths();
        Set<String> keySet = swaggerPathMap.keySet();
        List<SysCatalog> sysCatalogByComponentAndType = sysCatalogService.getByComponentAndType(sysComponent.getSysComponentId(), API_GROUP_TYPE.getValue());
        Map<String, String> sysCatalogNameIdMap = sysCatalogByComponentAndType.stream().collect(Collectors.toMap(SysCatalog::getName, SysCatalog::getCatalogId));
        for (String key : keySet) {
            Path path = swaggerPathMap.get(key);
            Operation operationGet = path.getGet();
            Operation operationPost = path.getPost();
            Operation operationPut = path.getPut();
            Operation operationDelete = path.getDelete();
            //查询此路径所属的catalog
            String catalogName = path.getOperations().get(0).getTags().get(0);
            String catalogId = sysCatalogNameIdMap.get(catalogName);
            //查询此catalog所有的resourceApi
            List<AuthResourceApi> authResourceApisByCatalog = authResourceApiService.listByCatalogId(catalogId);
            if (operationGet != null) {
                AuthResourceApi authResourceApi = covertOperationToAuthResourceApi(operationGet);
                authResourceApi.setMethod(ResourceApiMethodEnum.GET.getValue());
                authResourceApi.setRouteUrl(key);
                authResourceApi.setApiCatalogId(catalogId);
                authResourceApi.setSysComponentId(sysComponent.getSysComponentId());
                if (!haveSameResourceApi(authResourceApisByCatalog, authResourceApi)) {
                    authResourceApi.setResourceApiId(UUID.randomUUID().toString());
                    authResourceApiService.insertAuthResourceApi(authResourceApi);
                }
            }
            if (operationPost != null) {
                AuthResourceApi authResourceApi = covertOperationToAuthResourceApi(operationPost);
                authResourceApi.setMethod(ResourceApiMethodEnum.POST.getValue());
                authResourceApi.setRouteUrl(key);
                authResourceApi.setApiCatalogId(catalogId);
                authResourceApi.setSysComponentId(sysComponent.getSysComponentId());
                if (!haveSameResourceApi(authResourceApisByCatalog, authResourceApi)) {
                    authResourceApi.setResourceApiId(UUID.randomUUID().toString());
                    authResourceApiService.insertAuthResourceApi(authResourceApi);
                }
            }
            if (operationPut != null) {
                AuthResourceApi authResourceApi = covertOperationToAuthResourceApi(operationPut);
                authResourceApi.setMethod(ResourceApiMethodEnum.PUT.getValue());
                authResourceApi.setRouteUrl(key);
                authResourceApi.setApiCatalogId(catalogId);
                authResourceApi.setSysComponentId(sysComponent.getSysComponentId());
                if (!haveSameResourceApi(authResourceApisByCatalog, authResourceApi)) {
                    authResourceApi.setResourceApiId(UUID.randomUUID().toString());
                    authResourceApiService.insertAuthResourceApi(authResourceApi);
                }
            }
            if (operationDelete != null) {
                AuthResourceApi authResourceApi = covertOperationToAuthResourceApi(operationDelete);
                authResourceApi.setMethod(ResourceApiMethodEnum.DELETE.getValue());
                authResourceApi.setRouteUrl(key);
                authResourceApi.setApiCatalogId(catalogId);
                authResourceApi.setSysComponentId(sysComponent.getSysComponentId());
                if (!haveSameResourceApi(authResourceApisByCatalog, authResourceApi)) {
                    authResourceApi.setResourceApiId(UUID.randomUUID().toString());
                    authResourceApiService.insertAuthResourceApi(authResourceApi);
                }
            }

        }
    }

    /**
     * swagger的Operation对象转为AuthResourceApi
     *
     * @return 接口（api）
     */
    private AuthResourceApi covertOperationToAuthResourceApi(Operation operation) {
        return AuthResourceApi.builder()
                .name(operation.getSummary())
                .status(true)
                .type(API_GROUP_TYPE.getValue())
                .build();
    }

    /**
     * 判断列表里是否有相同结构的api
     *
     * @param authResourceApis api集合
     * @param authResourceApi  接口（api）
     * @return true 有 false 没有
     */

    private boolean haveSameResourceApi(List<AuthResourceApi> authResourceApis, AuthResourceApi authResourceApi) {
        List<AuthResourceApi> sameApis = authResourceApis.stream()
                .filter(
                        item -> item.getRouteUrl().equals(authResourceApi.getRouteUrl())
                                && item.getMethod().equals(authResourceApi.getMethod())
                                && item.getName().equals(authResourceApi.getName()))
                .collect(Collectors.toList());
        return !CollectionUtils.isEmpty(sameApis);
    }
}

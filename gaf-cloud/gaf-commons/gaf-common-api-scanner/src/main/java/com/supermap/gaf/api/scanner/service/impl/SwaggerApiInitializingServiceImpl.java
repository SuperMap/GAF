/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.api.scanner.service.impl;

import com.supermap.gaf.api.scanner.entity.AuthResourceApi;
import com.supermap.gaf.api.scanner.entity.SysCatalog;
import com.supermap.gaf.api.scanner.enums.CatalogTypeEnum;
import com.supermap.gaf.api.scanner.enums.ResourceApiMethodEnum;
import com.supermap.gaf.api.scanner.enums.ResourceApiTypeEnum;
import com.supermap.gaf.api.scanner.service.SwaggerApiInitializingService;
import com.supermap.gaf.api.scanner.service.SwaggerAuthResourceApiService;
import com.supermap.gaf.api.scanner.service.SwaggerSysCatalogService;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2021/2/1 9:33 AM
 */
@Slf4j
@Service
public class SwaggerApiInitializingServiceImpl implements SwaggerApiInitializingService {
    @Autowired
    private SwaggerSysCatalogService swaggerSysCatalogService;
    @Autowired
    private SwaggerAuthResourceApiService swaggerAuthResourceApiService;




    @Override
    public void initializeSysCatalog(Swagger swagger) {
        // todo: wxl 是否有问题
        SysCatalog parentSysCatalog = new SysCatalog();
        parentSysCatalog.setCatalogId("0");
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
            List<SysCatalog> reSelectSysCatalogByType = swaggerSysCatalogService.listByType(CatalogTypeEnum.API_GROUP_TYPE.getValue());
            for (int i = tags.size() - 1; i >= 0; i--) {
                String tag = tags.get(i);
                //重新获取目录
                List<SysCatalog> sysCatalogFilterByName = reSelectSysCatalogByType.stream().filter(item -> item.getName().equals(tag)).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(sysCatalogFilterByName)) {
                    //如果name不匹配，插入目录，并使pid为新增的目录id
                    SysCatalog sysCatalog =
                            SysCatalog.builder()
                                    .status(true)
                                    .type(CatalogTypeEnum.API_GROUP_TYPE.getValue())
                                    .parentId(parentId)
                                    .name(tag)
                                    .catalogId(UUID.randomUUID().toString())
                                    .build();
                    swaggerSysCatalogService.insertSysCatalog(sysCatalog);
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
    public void initializeSysResourceApi( Swagger swagger) {
        Map<String, Path> swaggerPathMap = swagger.getPaths();
        Set<String> keySet = swaggerPathMap.keySet();
        List<SysCatalog> sysCatalogs = swaggerSysCatalogService.listByType(CatalogTypeEnum.API_GROUP_TYPE.getValue());
        Map<String, String> sysCatalogNameIdMap = sysCatalogs.stream().collect(Collectors.toMap(SysCatalog::getName, SysCatalog::getCatalogId));
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
            List<AuthResourceApi> authResourceApisByCatalog = swaggerAuthResourceApiService.listByCatalogId(catalogId);
            String basePath = swagger.getBasePath();
            if (operationGet != null) {
                AuthResourceApi authResourceApi = covertOperationToAuthResourceApi(operationGet);
                authResourceApi.setMethod(ResourceApiMethodEnum.GET.getValue());
                authResourceApi.setRouteUrl(basePath + key);
                authResourceApi.setApiCatalogId(catalogId);
                if (!haveSameResourceApi(authResourceApisByCatalog, authResourceApi)) {
                    authResourceApi.setResourceApiId(UUID.randomUUID().toString());
                    swaggerAuthResourceApiService.insertAuthResourceApi(authResourceApi);
                }
            }
            if (operationPost != null) {
                AuthResourceApi authResourceApi = covertOperationToAuthResourceApi(operationPost);
                authResourceApi.setMethod(ResourceApiMethodEnum.POST.getValue());
                authResourceApi.setRouteUrl(basePath + key);
                authResourceApi.setApiCatalogId(catalogId);
                if (!haveSameResourceApi(authResourceApisByCatalog, authResourceApi)) {
                    authResourceApi.setResourceApiId(UUID.randomUUID().toString());
                    swaggerAuthResourceApiService.insertAuthResourceApi(authResourceApi);
                }
            }
            if (operationPut != null) {
                AuthResourceApi authResourceApi = covertOperationToAuthResourceApi(operationPut);
                authResourceApi.setMethod(ResourceApiMethodEnum.PUT.getValue());
                authResourceApi.setRouteUrl(basePath + key);
                authResourceApi.setApiCatalogId(catalogId);
                if (!haveSameResourceApi(authResourceApisByCatalog, authResourceApi)) {
                    authResourceApi.setResourceApiId(UUID.randomUUID().toString());
                    swaggerAuthResourceApiService.insertAuthResourceApi(authResourceApi);
                }
            }
            if (operationDelete != null) {
                AuthResourceApi authResourceApi = covertOperationToAuthResourceApi(operationDelete);
                authResourceApi.setMethod(ResourceApiMethodEnum.DELETE.getValue());
                authResourceApi.setRouteUrl(basePath + key);
                authResourceApi.setApiCatalogId(catalogId);
                if (!haveSameResourceApi(authResourceApisByCatalog, authResourceApi)) {
                    authResourceApi.setResourceApiId(UUID.randomUUID().toString());
                    swaggerAuthResourceApiService.insertAuthResourceApi(authResourceApi);
                }
            }

        }
    }

    /**
     * swagger的Operation对象转为AuthResourceApi
     *
     * @return
     */
    private AuthResourceApi covertOperationToAuthResourceApi(Operation operation) {
        if (StringUtils.isEmpty(operation.getSummary())) {
            log.info("API {} 缺失swagger注解信息,请补全",operation);
        }
        return AuthResourceApi.builder()
                .name(StringUtils.isEmpty(operation.getSummary()) ? operation.getOperationId(): operation.getSummary())
                .status(true)
                .type(ResourceApiTypeEnum.SYSTEM.getCode())
                .build();
    }

    /**
     * 判断列表里是否有相同结构的api
     *
     * @param authResourceApis
     * @param authResourceApi
     * @return
     */
    private boolean haveSameResourceApi(List<AuthResourceApi> authResourceApis, AuthResourceApi authResourceApi) {
        List<AuthResourceApi> sameApis = authResourceApis.stream()
                .filter(
                        item -> item.getRouteUrl().equals(authResourceApi.getRouteUrl())
                                && item.getMethod().equals(authResourceApi.getMethod()) )
                .collect(Collectors.toList());
        return !CollectionUtils.isEmpty(sameApis);
    }
}

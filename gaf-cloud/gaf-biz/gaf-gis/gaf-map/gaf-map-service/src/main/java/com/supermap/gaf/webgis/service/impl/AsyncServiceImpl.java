/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.webgis.cache.RegistryResultCacheI;
import com.supermap.gaf.webgis.entity.WebgisService;
import com.supermap.gaf.webgis.service.AsyncService;
import com.supermap.gaf.webgis.service.WebgisServiceService;
import com.supermap.gaf.webgis.util.WebgisCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.supermap.gaf.webgis.service.impl.WebgisServiceServiceImpl.REGISTRY_TYPE_SERVER;

/**
 * @author heykb
 * @date:2021/3/25
 */
@Component
public class AsyncServiceImpl implements AsyncService {

    @Autowired
    private WebgisServiceService webgisServiceService;
    @Autowired
    public RegistryResultCacheI registryResultCacheI;

    @Async
    @Override
    public CompletableFuture<Void> batchRegistryWebgis(WebgisService webgisService, String type, String resultCode, @Nullable String sourceId, @Nullable Integer sourceType) {
        Map<String, WebgisService> services = new HashMap<>();
        List<WebgisService> lists = new ArrayList<>();
        String url = webgisService.getAddress();
        RestTemplate restTemplate = new RestTemplate();
        if (WebgisServiceServiceImpl.REGISTRY_TYPE_BATCH.equals(type)) {
            try {
                String typeCode = webgisService.getTypeCode();
                WebgisCommonUtils.listService(typeCode,url, service -> {
                    try {
                        service.setDescription(webgisService.getDescription());
                        service.setTimeAttribute(webgisService.getTimeAttribute());
                        service.setMoreProperties(webgisService.getMoreProperties());
                        webgisServiceService.registryWebgis(service,sourceId,sourceType);
                        registryResultCacheI.success(resultCode);
                    } catch (Exception e) {
                        if (e instanceof GafException && ((GafException) e).getCode() == GafException.CONFLICT) {
                            registryResultCacheI.exist(resultCode);
                        } else {
                            registryResultCacheI.fail(resultCode);
                        }
                    }
                    return null;
                });
            } catch (Exception e) {
                registryResultCacheI.error(resultCode);
            }

        } else if (REGISTRY_TYPE_SERVER.equals(type)) {
            try {
                JSONArray list = restTemplate.getForObject(url + "/services.json", JSONArray.class);
                for (int i = 0; i < list.size(); ++i) {
                    JSONObject item = list.getJSONObject(i);
                    String typeCode = WebgisCommonUtils.parseServiceType(item.getString("interfaceType"), item.getString("componentType"));
                    if (typeCode == null) {
                        continue;
                    }
                    try {
                        WebgisCommonUtils.listService(typeCode,item.getString("url"), service -> {
                            try {
                                lists.add(service);
                                service.setDescription(webgisService.getDescription());
                                service.setTimeAttribute(webgisService.getTimeAttribute());
                                service.setMoreProperties(webgisService.getMoreProperties());
                                webgisServiceService.registryWebgis(service,null,null);
                                registryResultCacheI.success(resultCode);
                            } catch (Exception e) {
                                if (e instanceof GafException && ((GafException) e).getCode() == 409) {
                                    if (services.containsKey(service.getAddress())) {
                                        System.out.println(service);
                                    }
                                    services.put(service.getAddress(), service);
                                    registryResultCacheI.exist(resultCode);
                                } else {
                                    registryResultCacheI.fail(resultCode);
                                }

                            }
                            return null;
                        });
                    } catch (Exception e) {
                    }
                }
            } catch (RestClientException e) {
                e.printStackTrace();
                throw new GafException("服务解析失败");
            }
        }
        return CompletableFuture.completedFuture(null);
    }


}

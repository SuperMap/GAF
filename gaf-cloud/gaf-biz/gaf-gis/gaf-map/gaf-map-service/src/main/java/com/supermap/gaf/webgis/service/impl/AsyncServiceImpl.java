/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.webgis.cache.RegistryResultCacheI;
import com.supermap.gaf.webgis.entity.WebgisService;
import com.supermap.gaf.webgis.service.AsyncService;
import com.supermap.gaf.webgis.service.WebgisServiceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
    public CompletableFuture<Void> batchRegistryWebgis(WebgisService webgisService, String type, String resultCode) {

        Map<String, WebgisService> services = new HashMap<>();
        List<WebgisService> lists = new ArrayList<>();
        String address = webgisService.getAddress();
        RestTemplate restTemplate = new RestTemplate();
        if (WebgisServiceServiceImpl.REGISTRY_TYPE_BATCH.equals(type)) {
            String typeCode = webgisService.getTypeCode();
            try {
                webgisService.setTypeCode(typeCode);
                webgisService.setAddress(address);
                listService(webgisService, service -> {
                    try {
                        webgisServiceService.registryWebgis(service);
                        registryResultCacheI.success(resultCode);
                    } catch (Exception e) {
                        if (e instanceof GafException && ((GafException) e).getCode() == GafException.CONFLICT) {
                            registryResultCacheI.exist(resultCode);
                        } else {
                            registryResultCacheI.fail(resultCode);
                        }
                    }
                });
            } catch (Exception e) {
                registryResultCacheI.error(resultCode);
            }

        } else if (REGISTRY_TYPE_SERVER.equals(type)) {
            try {
                JSONArray list = restTemplate.getForObject(address + "/services.json", JSONArray.class);
                for (int i = 0; i < list.size(); ++i) {
                    JSONObject item = list.getJSONObject(i);
                    String typeCode = parseServiceType(item.getString("interfaceType"), item.getString("componentType"));
                    if (typeCode == null) {
                        continue;
                    }
                    try {
                        webgisService.setTypeCode(typeCode);
                        webgisService.setAddress(item.getString("url"));
                        listService(webgisService, service -> {
                            try {
                                lists.add(service);
                                webgisServiceService.registryWebgis(service);
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
                        });
                    } catch (Exception e) {
                    }
                }
            } catch (RestClientException e) {
                e.printStackTrace();
                throw new GafException("服务解析失败");
            }
        }
        System.out.println(JSON.toJSONString(lists));
        return CompletableFuture.completedFuture(null);
    }

    String parseServiceType(String interfaceType, String componentType) {
        if ("com.supermap.services.rest.RestServlet".equals(interfaceType) ||
                "com.supermap.services.rest.JaxrsServletForJersey".equals(interfaceType) ||
                "com.supermap.services.rest.BaiduRestServlet".equals(interfaceType) ||
                "com.supermap.services.rest.OSMRestServlet".equals(interfaceType) ||
                "com.supermap.services.rest.TMSRestServlet".equals(interfaceType)) {
            if ("com.supermap.services.components.impl.DataImpl".equals(componentType) || "com.supermap.services.components.impl.DataHistoryImpl".equals(componentType)) {
                // 数据服务组件
                return "RESTDATA";
            } else if ("com.supermap.services.components.impl.MapImpl".equals(componentType)) {
                // 地图服务组件
                return "RESTMAP";
            } else if ("com.supermap.services.components.impl.RealspaceImpl".equals(componentType)) {
                // 三维服务组件
                return "RESTREALSPACE";
            } else if ("com.supermap.services.components.impl.SpatialAnalystImpl".equals(componentType)) {
                // 空间分析服务组件
                return "RESTSPATIALANALYST";
            } else {
                //
                return null;
            }
        } else if ("com.supermap.services.components.spi.ogc.WMS".equals(interfaceType)) {
            return "WMS";
        } else if ("com.supermap.services.wmts.WMTSServlet".equals(interfaceType)) {
            return "WMTS";
        } else if ("com.supermap.services.protocols.wfs.WFS".equals(interfaceType)) {
            return "WFS";
        } else if ("com.supermap.services.rest.RestServlet".equals(interfaceType)) {
            return "WCS";
        } else if ("com.supermap.services.protocols.wcs.WCS".equals(interfaceType)) {
            return "WPS";
        } else if ("com.supermap.services.rest.AGSRestServlet".equals(interfaceType)) {
            return "ARCGISMAP";
        }
        return null;
    }


    void listService(WebgisService source, RegistryProgress registryProgress) throws Exception {
        String typeCode = source.getTypeCode();
        String url = source.getAddress();
        String name = source.getName();
        WebgisService webgisService = new WebgisService();
        BeanUtils.copyProperties(source, webgisService);
        RestTemplate restTemplate = new RestTemplate();
        if ("RESTMAP".equals(typeCode)) {
            url += "/maps.json";
            JSONArray list = restTemplate.getForObject(url, JSONArray.class);
            for (int i = 0; i < list.size(); ++i) {
                JSONObject item = list.getJSONObject(i);
                webgisService.setAddress(item.getString("path"));
                webgisService.setName(item.getString("name"));
                webgisService.setTypeCode(typeCode);
                registryProgress.registry(webgisService);
            }
        } else if ("RESTDATA".equals(typeCode)) {
            url += "/data/datasources.json";
            // 遍历数据集
            JSONObject datasources = restTemplate.getForObject(url, JSONObject.class);
            JSONArray list = datasources.getJSONArray("childUriList");
            for (int i = 0; i < list.size(); ++i) {
                String childUrl = list.getString(i);
                JSONObject datasets = restTemplate.getForObject(childUrl += "/datasets.json", JSONObject.class);
                JSONArray names = datasets.getJSONArray("datasetNames");
                JSONArray urls = datasets.getJSONArray("childUriList");
                for (int j = 0; j < urls.size(); ++j) {
                    webgisService.setAddress(urls.getString(j));
                    webgisService.setName(names.getString(j));
                    webgisService.setTypeCode(typeCode);
                    registryProgress.registry(webgisService);
                }
            }

        } else if ("RESTREALSPACE".equals(typeCode)) {
            url += "/realspace/datas.json";
            JSONArray list = restTemplate.getForObject(url, JSONArray.class);
            for (int i = 0; i < list.size(); ++i) {
                JSONObject item = list.getJSONObject(i);
                webgisService.setAddress(item.getString("path"));
                webgisService.setName(item.getString("name"));
                webgisService.setTypeCode(typeCode);
                registryProgress.registry(webgisService);
            }
        } else if ("RESTSPATIALANALYST".equals(typeCode)) {
            url += "/spatialanalyst/datasets.json";
            JSONArray list = restTemplate.getForObject(url, JSONArray.class);
            for (int i = 0; i < list.size(); ++i) {
                JSONObject item = list.getJSONObject(i);
                webgisService.setAddress(item.getString("path"));
                webgisService.setName(item.getString("name"));
                webgisService.setTypeCode(typeCode);
                registryProgress.registry(webgisService);
            }
        } else {
            registryProgress.registry(source);
        }
    }

    static interface RegistryProgress {
        void registry(WebgisService webgisService);
    }
}

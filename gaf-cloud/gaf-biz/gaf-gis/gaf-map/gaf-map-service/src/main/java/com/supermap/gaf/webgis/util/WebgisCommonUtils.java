package com.supermap.gaf.webgis.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.gaf.webgis.entity.WebgisService;
import org.springframework.web.client.RestTemplate;

import java.util.function.Function;

public class WebgisCommonUtils {
    public static  void listService(String typeCode,String url, Function<WebgisService,Object> forEach) {
//        String typeCode = source.getTypeCode();
//        String url = source.getAddress();
        WebgisService webgisService = new WebgisService();
        RestTemplate restTemplate = new RestTemplate();
        if ("RESTMAP".equals(typeCode)) {
            url += "/maps.json";
            JSONArray list = restTemplate.getForObject(url, JSONArray.class);
            for (int i = 0; i < list.size(); ++i) {
                JSONObject item = list.getJSONObject(i);
                webgisService.setAddress(item.getString("path"));
                webgisService.setName(item.getString("name"));
                webgisService.setTypeCode(typeCode);
                forEach.apply(webgisService);
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
                    forEach.apply(webgisService);
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
                forEach.apply(webgisService);
            }
        } else if ("RESTSPATIALANALYST".equals(typeCode)) {
            url += "/spatialanalyst/datasets.json";
            JSONArray list = restTemplate.getForObject(url, JSONArray.class);
            for (int i = 0; i < list.size(); ++i) {
                JSONObject item = list.getJSONObject(i);
                webgisService.setAddress(item.getString("path"));
                webgisService.setName(item.getString("name"));
                webgisService.setTypeCode(typeCode);
                forEach.apply(webgisService);
            }
        } else {
            forEach.apply(webgisService);
        }
    }

    public static   String parseServiceType(String interfaceType, String componentType) {
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
        return "OTHER";
    }
}

package com.supermap.gaf.webgis.enums;

import com.supermap.services.components.spi.ogc.WMS;
import com.supermap.services.protocols.wcs.WCS;
import com.supermap.services.protocols.wfs.WFS;
import com.supermap.services.wps.WPS;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务类型
 * @author wxl
 */
public enum ServiceTypeEnum {
//    RESTMAP:地图服务,
//    RESTDATA:数据服务,
//    RESTREALSPACE:三维服务,
//    RESTSPATIALANALYST:空间分析服务,
//    ARCGISMAP:ArcGIS地图服务,
//    WMS:WMS服务,
//    WMTS:WMTS服务,
//    WFS:WFS服务,
//    WCS:WCS服务,
//    WPS:WPS服务,
//    DXZG:栅格地形,
//    DXTIN:TIN地形,
//    YINGXIANG:影像
    /**
     * 数据服务
     */
    RESTDATA("RESTDATA", "数据服务"),
    /**
     * 地图服务
     */
    RESTMAP("RESTMAP", "地图服务"),
    /**
     * 三维服务
     */
    RESTREALSPACE("RESTREALSPACE", "三维服务"),
    /**
     * 空间分析服务
     */
    RESTSPATIALANALYST("RESTSPATIALANALYST", "空间分析服务"),
    /**
     * ArcGIS地图服务
     */
    ARCGISMAP("ARCGISMAP", "ArcGlS地图服务"),
    /**
     * WMS服务
     */
    WMS("WMS", "WMS服务"),
    /**
     * WMTS服务
     */
    WMTS("WMTS", "WMTS服务"),
    /**
     * WFS服务
     */
    WFS("WFS", "WFS服务"),
    /**
     * WCS服务
     */
    WCS("WCS", "WCS服务"),

    /**
     * WCS服务
     */
    WPS("WPS", "WPS服务"),

    /**
     * 栅格地形
     */
    DXZG("DXZG", "栅格地形"),
    /**
     * TIN地形
     */
    DXTIN("DXTIN", "TIN地形"),
    /**
     * TIN地形
     */
    MAPWORLD("MAPWORLD", "天地图"),
    /**
     * 影像
     */
    YINGXIANG("YINGXIANG", "影像");

    private String code;
    /**
     * 模板类型说明
     */
    private String name;

    ServiceTypeEnum(String type, String description) {
        this.code = type;
        this.name = description;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Map<String,String> getMap() {
        ServiceTypeEnum[] values = values();
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            map.putIfAbsent(values[i].getCode(),values[i].getName());
        }
        return map;
    }
}

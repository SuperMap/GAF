package com.supermap.gaf.webgis.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务类型
 *
 * @author wxl
 */
public enum ServiceTypeEnum {
    /**
     * 数据服务
     */
    RESTDATA("RESTDATA", "数据服务"),

    /**
     * 三维服务
     */
    RESTREALSPACE("RESTREALSPACE","三维服务"),
    /**
     * 地图服务
     */
    RESTMAP("RESTMAP", "地图服务"),
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
    GRID_DEM("GRID_DEM", "栅格地形"),
    /**
     * TIN地形
     */
    TIN_DEM("TIN_DEM", "TIN地形"),
    /**
     * TIN地形
     */
    MAPWORLD("MAPWORLD", "天地图"),
    /**
     * 影像
     */
    YINGXIANG("YINGXIANG", "影像"),

    /**
     * 百度REST
     */
    BAIDUREST("BAIDUREST","百度REST"),
    /**
     * 谷歌REST
     */
    GOOGLEREST("GOOGLEREST","谷歌REST"),
    /**
     * 其它
     */
    OTHER("OTHER","其它");


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

    public static String getNameByCode(String code) {
        ServiceTypeEnum[] values = values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].getCode().equals(code)) {
                return values[i].getName();
            }
        }
        throw new IllegalArgumentException("不支持的服务类型"+code);
    }

    public static Map<String, String> getMap() {
        ServiceTypeEnum[] values = values();
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            map.putIfAbsent(values[i].getCode(), values[i].getName());
        }
        return map;
    }
}

package com.supermap.gaf.data.mgt.util;

import com.supermap.data.Enum;
import com.supermap.data.*;
import com.supermap.gaf.data.mgt.model.CoordSysInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * 坐标系工具类
 * 主要用于解析指定格式的字符串 得到坐标系
 * 坐标系分为平面坐标系 地理坐标系 投影坐标系
 *
 * @author wxl
 * @since 2021/8/18
 */
public class PrjCoordSysUtil {


    public static final String PLANER_PREFIX = PrjCoordSysType.PCS_NON_EARTH.name() + "_";
    public static final String GEO_PREFIX = PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE.name() + "_";

    /**
     * 解析指定格式的字符串 得到坐标系
     *
     * 注意：暂时不支持自定义坐标系, 后续可以添加逻辑解析指定格式的字符串得到自定义坐标系
     *
     * 坐标系分为平面坐标系 地理坐标系 投影坐标系
     * 坐标系字符串格式是
     * 1. 平面坐标系：
     * PCS_NON_EARTH_${坐标系单位}, 坐标系单位有CENTIMETER(厘米) DECIMETER分米 FOOT英尺 INCH英寸 KILOMETER千米
     * METER米 MILE英里 MILIMETER毫米 ,具体可查看iobjects for java 在线帮助文档搜索Unit枚举类(com.supermap.data.Unit)
     * 例如：PCS_NON_EARTH_METER 或者 PCS_NON_EARTH_MILE
     * 2. 地理坐标系
     * PCS_EARTH_LONGITUDE_LATITUDE_${地理坐标系类型名}
     * 地理坐标系类型名可参考iobjects for java 在线帮助文档 GeoCoordSysType 枚举类(com.supermap.data.GeoCoordSysType)
     * 例如：GeoCoordSysType枚举类中的实例GCS_CHINA_2000,则得到地理坐标系字符串为PCS_EARTH_LONGITUDE_LATITUDE_GCS_CHINA_2000
     * 3. 投影坐标系
     * 可参考iobjects for java 在线帮助文档, 搜索PrjCoordSysType枚举类(com.supermap.data.PrjCoordSysType)
     * 每个枚举实例名就代表投影坐标系,除了PCS_NON_EARTH  PCS_EARTH_LONGITUDE_LATITUDE PCS_USER_DEFINED
     * 例如：PrjCoordSysType枚举类实例 PCS_WGS_1984_UTM_1N,则得到投影坐标系字符串为 PCS_WGS_1984_UTM_1N
     *
     * @param prjCoordSysStr 坐标系字符串
     * @return 坐标系对象 如果参数为空则返回默认的单位为米的平面坐标系
     */
    public static PrjCoordSys parse(String prjCoordSysStr) {
        try {
            if (prjCoordSysStr == null || prjCoordSysStr.isEmpty()) {
                return new PrjCoordSys(PrjCoordSysType.PCS_NON_EARTH);
            }
            if (prjCoordSysStr.startsWith(PLANER_PREFIX)) {
                String unitStr = prjCoordSysStr.replaceFirst(PLANER_PREFIX, "");
                Unit unit = (Unit) Unit.parse(Unit.class, unitStr);
                PrjCoordSys prjCoordSys = new PrjCoordSys(PrjCoordSysType.PCS_NON_EARTH);
                prjCoordSys.setCoordUnit(unit);
                return prjCoordSys;
            }
            if (prjCoordSysStr.startsWith(GEO_PREFIX)) {
                String geoCoordSysTypeStr = prjCoordSysStr.replaceFirst(GEO_PREFIX, "");
                GeoCoordSysType geoCoordSysType = (GeoCoordSysType) GeoCoordSysType.parse(GeoCoordSysType.class, geoCoordSysTypeStr);
                GeoCoordSys geoCoordSys = new GeoCoordSys(geoCoordSysType,GeoSpatialRefType.SPATIALREF_EARTH_LONGITUDE_LATITUDE);
                PrjCoordSys prjCoordSys = new PrjCoordSys(PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE);
                prjCoordSys.setGeoCoordSys(geoCoordSys);
                return prjCoordSys;
            }

            PrjCoordSysType prjCoordSysType = (PrjCoordSysType) PrjCoordSysType.parse(PrjCoordSysType.class, prjCoordSysStr);
            return  new PrjCoordSys(prjCoordSysType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("解析指定格式的坐标系字符串失败,原因:"+ e.getMessage(),e);
        }
    }


    public static List<CoordSysInfo> listFrequentlyUsed() {
        List<CoordSysInfo> all = new LinkedList<>();
        all.add(getPlanerPrjCoordSysInfo(Unit.METER));

        all.add(getPrjCoordSysInfo(PrjCoordSysType.PCS_WGS_1984_WORLD_MERCATOR));

        all.add(getGeoPrjCoordSysInfo(GeoCoordSysType.GCS_CHINA_2000));
        all.add(getGeoPrjCoordSysInfo(GeoCoordSysType.GCS_WGS_1984));
        all.add(getGeoPrjCoordSysInfo(GeoCoordSysType.GCS_XIAN_1980));
        all.add(getGeoPrjCoordSysInfo(GeoCoordSysType.GCS_BEIJING_1954));
        return all;
    }

    /**
     * 获取所有已预定义坐标系信息
     * @return 所有已预定义坐标系信息
     */
    public static List<CoordSysInfo> listPrjCoordSysInfo() {
        List<CoordSysInfo> all = new LinkedList<>();
        // 平面坐标系
        all.add(getPlanerPrjCoordSysInfo(Unit.MILIMETER));
        all.add(getPlanerPrjCoordSysInfo(Unit.CENTIMETER));
        all.add(getPlanerPrjCoordSysInfo(Unit.DECIMETER));
        all.add(getPlanerPrjCoordSysInfo(Unit.METER));
        all.add(getPlanerPrjCoordSysInfo(Unit.KILOMETER));
        all.add(getPlanerPrjCoordSysInfo(Unit.INCH));
        all.add(getPlanerPrjCoordSysInfo(Unit.FOOT));
        all.add(getPlanerPrjCoordSysInfo(Unit.YARD));
        all.add(getPlanerPrjCoordSysInfo(Unit.MILE));
        // 地理坐标系
        Enum[] enums = GeoCoordSysType.getEnums(GeoCoordSysType.class);
        for (Enum anEnum : enums) {
            GeoCoordSysType geoCoordSysType = (GeoCoordSysType) anEnum;
            if (GeoCoordSysType.GCS_USER_DEFINE.equals(geoCoordSysType)) {
                continue;
            }
            CoordSysInfo geoCoordSysInfo = getGeoPrjCoordSysInfo(geoCoordSysType);
            all.add(geoCoordSysInfo);
        }
        // 投影坐标系
        Enum[] prjEnums = PrjCoordSysType.getEnums(PrjCoordSysType.class);
        for (Enum prjEnum : prjEnums) {
            PrjCoordSysType prjCoordSysType = (PrjCoordSysType) prjEnum;
            if (PrjCoordSysType.PCS_USER_DEFINED.equals(prjCoordSysType) || PrjCoordSysType.PCS_NON_EARTH.equals(prjCoordSysType) || PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE.equals(prjCoordSysType)) {
                continue;
            }
            all.add(getPrjCoordSysInfo(prjCoordSysType));
        }
        return all;
    }
    private static CoordSysInfo getPrjCoordSysInfo(PrjCoordSysType prjCoordSysType) {
        PrjCoordSys prjCoordSys = new PrjCoordSys(prjCoordSysType);
        return new CoordSysInfo( prjCoordSysType.name(),prjCoordSys.getName(),GeoSpatialRefType.SPATIALREF_EARTH_PROJECTION.name(),prjCoordSys.getEPSGCode());
    }


    private static CoordSysInfo getGeoPrjCoordSysInfo(GeoCoordSysType geoCoordSysType) {
        GeoCoordSys geoCoordSys = new GeoCoordSys(geoCoordSysType,GeoSpatialRefType.SPATIALREF_EARTH_LONGITUDE_LATITUDE);
        PrjCoordSys prjCoordSys = new PrjCoordSys(PrjCoordSysType.PCS_EARTH_LONGITUDE_LATITUDE);
        prjCoordSys.setGeoCoordSys(geoCoordSys);
        return new CoordSysInfo(GEO_PREFIX + geoCoordSysType.name(),prjCoordSys.getName(),GeoSpatialRefType.SPATIALREF_EARTH_LONGITUDE_LATITUDE.name(),prjCoordSys.getEPSGCode());
    }

    private static CoordSysInfo getPlanerPrjCoordSysInfo(Unit unit) {
        PrjCoordSys prjCoordSys = new PrjCoordSys(PrjCoordSysType.PCS_NON_EARTH);
        prjCoordSys.setCoordUnit(unit);
        return new CoordSysInfo(PLANER_PREFIX + unit.name(),prjCoordSys.getName(),GeoSpatialRefType.SPATIALREF_NONEARTH.name(),prjCoordSys.getEPSGCode());
    }


}

package com.supermap.gaf.commontypes;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;

public enum GAFLicenseModuleType {

    /**
     * gaf核心组件
     */
    GAF_CORE(17600),
    /**
     * gaf地图组件
     */
    GAF_MAP(17601),
    /**
     * gaf数据中心组件
     */
    GAF_DATA(17602),
    /**
     * gaf用户授权组件
     */
    GAAF_USER(17603),
    /**
     * gaf门户组件
     */
    GAF_PORTAL(17604);

    private int value;
    private static final List<GAFLicenseModuleType> ALL_TYPES = EnumUtils.getEnumList(GAFLicenseModuleType.class);

    public int value() {
        return this.value;
    }

    // 必须是private的，否则编译错误
    private GAFLicenseModuleType(int value) {
        this.value = value;
    }

    public static GAFLicenseModuleType valueOf2(int value) {
        Iterator<GAFLicenseModuleType> types = ALL_TYPES.iterator();
        while (types.hasNext()) {
            GAFLicenseModuleType type = types.next();
            if (type.value == value) {
                return type;
            }
        }
        return GAFLicenseModuleType.GAF_CORE;
    }

}

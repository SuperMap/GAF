package com.supermap.gaf.data.access.commontypes;

/**
 * @program: app-landuse
 * @description: 统计单位枚举
 * @author: lidong
 * @create: 2018/11/14
 */
public enum TJDWTypeEnum {
    /**
     * 公顷
     */
    GQ("公顷"),
    /**
     * 亩
     */
    MU("亩"),
    /**
     * 平方米
     */
    PM("平方米");

    private String value;

    private TJDWTypeEnum(String value) {
        this.value = value;
    }

    public static String getValue(TJDWTypeEnum type) {
        String value = "";
        switch (type) {
            case GQ:
                value = TJDWTypeEnum.GQ.value;
                break;
            case MU:
                value = TJDWTypeEnum.MU.value;
                break;
            case PM:
                value = TJDWTypeEnum.PM.value;
                break;
        }
        return value;
    }
}

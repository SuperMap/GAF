package com.supermap.gaf.webgis.enums;

/**
 * @author heykb
 */
public enum PositionTypeEnum {
    top("1","上"),
    topLeft("2","左上"),
    bottomLeft("3","左上"),
    bottom("4","左上"),
    topRight("5","左上"),
    bottomRight("6","左上"),
    unknown("","未知");
    private String code;
    /**
     * 模板类型说明
     */
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    PositionTypeEnum(String type, String description) {
        this.code = type;
        this.name = description;
    }
    public static PositionTypeEnum get(String code) {
        for (PositionTypeEnum ele : values()) {
            if(ele.getCode().equals(code)) return ele;
        }
        return unknown;
    }
}

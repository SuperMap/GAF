package com.supermap.gaf.analysis.entity;


/**
 * overlay叠加分方法
 *
 * @author : yd
 * @since 2020-12-29
 */
public enum SpatialAnalysisOperatorEnum {

    /**
     * 裁剪
     */
    CLIP("叠加分析-裁剪"),
    /**
     *擦除
     */
    ERASE("叠加分析-擦除"),
    /**
     *相交
     */
    INTERSECT("叠加分析-相交"),
    /**
     *同一
     */
    IDENTITY("叠加分析-同一"),
    /**
     *合并
     */
    UNION("叠加分析-合并"),
    /**
     *对称差
     */
    XOR("叠加分析-对称差"),
    /**
     *更新
     */
    UPDATE("叠加分析-更新");

    private String value;

    SpatialAnalysisOperatorEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    public String getOverlayMethodName() {
        return this.name();
    }

    public String getGpOverlayMethodName() {
        String source = this.name();
        switch (source) {
            case "CLIP":
                return "Clip";
            case "ERASE":
                return "Erase";
            case "INTERSECT":
                return "Intersect";
            case "IDENTITY":
                return "Identity";
            case "UNION":
                return "Union";
            case "XOR":
                return "XOR";
            case "UPDATE":
                return "Update";
            default:
                return source;
        }
    }
}

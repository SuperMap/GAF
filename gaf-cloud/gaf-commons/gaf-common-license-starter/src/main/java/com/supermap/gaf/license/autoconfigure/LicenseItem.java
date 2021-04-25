package com.supermap.gaf.license.autoconfigure;

/**
 * 超图许可模块id与多个请求路径
 * 路径支持Ant路径匹配规则
 */
public class LicenseItem {
    /**
     * 向SuperMap许可中心申请的模块id
     */
    private int featureId;

    /**
     * 要拦截的path,顺序很重要，暂时不支持httpmethod
     */
    private String[] paths = new String[0];


    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public String[] getPaths() {
        return paths;
    }

    public void setPaths(String[] paths) {
        this.paths = paths;
    }
}

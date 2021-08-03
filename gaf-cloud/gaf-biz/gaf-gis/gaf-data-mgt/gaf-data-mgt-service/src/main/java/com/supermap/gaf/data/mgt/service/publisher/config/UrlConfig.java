package com.supermap.gaf.data.mgt.service.publisher.config;

/**
 * @author:yw
 * @Date 2021-3-12
 * 服务发布url配置
 */
public final class UrlConfig {

    private UrlConfig() {
    }

    /**
     * 获取发布工作空间url
     */
    public static String getPublishWorkspaceUrl(String iServerHostUrl, String token) {
        return String.format("%s/manager/workspaces.json?token=%s", iServerHostUrl, token);
    }

    /**
     * 获取发布工作空间url （带返回值）
     */
    public static String getPublishWorkspaceUrlWithReturn(String iServerHostUrl, String token) {
        return String.format("%s/manager/workspaces.rjson?returnContent=true&token=%s", iServerHostUrl, token);
    }

    /**
     * 获取发布服务提供者路径
     */
    public static String getProvidersManagerUrl(String iServerHostUrl, String token) {
        return String.format("%s/manager/providers.json?token=%s", iServerHostUrl, token);
    }

    /**
     * 获取发布服务组件路径
     */
    public static String getComponentManagerUrl(String iServerHostUrl, String token) {
        return String.format("%s/manager/components.json?token=%s", iServerHostUrl, token);
    }

    /**
     * 获取生成token访问路径
     */
    public static String getServerTokenPostUrl(String iServerHostUrl) {
        return String.format("%s/services/security/tokens.rjson", iServerHostUrl);
    }

    /**
     * 获取管理服务实例url，主要用于删除服务实例
     */
    public static String getInstanceManagerDelUrl(String iServerHostUrl, String instanceName, String token) {
        return String.format("%s/manager/services/%s.json?token=%s", iServerHostUrl, instanceName, token);
    }

    /**
     * 获取服务管理实例url，主要用于启停服务实例
     */
    public static String getInstanceManagerUpUrl(String iServerHostUrl, String token) {
        return String.format("%s/manager/services.json?token=%s", iServerHostUrl, token);
    }

    /**
     * 获取工作空间管理url，主要用于删除工作空间
     */
    public static String getWorkspaceManagerUrl(String iServerHostUrl, String token) {
        return String.format("%s/manager/workspace.json?token=%s", iServerHostUrl, token);
    }

    /**
     * 获取地图服务下地图列表url
     */
    public static String getRestMapChildUrl(String serverUrl, String token) {
        return String.format("%s/maps.json?token=%s", serverUrl, token);
    }

    /**
     * 获取空间分析服务下数据集列表url
     */
    public static String getRestSpatialanalystChildUrl(String serverUrl, String token) {
        return String.format("%s/spatialanalyst/datasets.json?token=%s", serverUrl, token);
    }

    /**
     * 获取三维服务下场景列表url
     */
    public static String getRestRealspaceChildUrl(String serverUrl, String token) {
        return String.format("%s/realspace/scenes.json?token=%s", serverUrl, token);
    }

    /**
     * 获取数据服务下场景列表url
     */
    public static String getRestDataChildUrl(String serverUrl, String datasourceAlise, String token) {
        return String.format("%s/data/datasources/%s/datasets.json?token=%s", serverUrl, datasourceAlise, token);
    }

    /**
     * 获取iServer下服务实例列表url
     */
    public static String getInstanceNameUrl(String serverUrl, String token) {
        return String.format("%s/manager/instances.json?token=%s", serverUrl, token);
    }
}

package com.supermap.gaf.microservice;

/**
 * @program: gaf-microservice-mgt
 * @description: 配置提供者常量
 * @author: lidong
 * @create: 2019/08/24
 */
public final class ConfigProviderConst {
    /**
     * springcloud的configServer
     */
    public static final String SPRING_CLOUD_CONFIG_SERVER = "CONFIG_SERVER";
    /**
     * k8s的configMap
     */
    public static final String K8S_CONFIG_MAP = "CONFIG_MAP";

    /**
     * 管理服务key，可以通过服务依赖来动态更改
     */
    public static final String MGT_SERVICE_CONFIG_SERVER = "mgt-service-config-server";
}

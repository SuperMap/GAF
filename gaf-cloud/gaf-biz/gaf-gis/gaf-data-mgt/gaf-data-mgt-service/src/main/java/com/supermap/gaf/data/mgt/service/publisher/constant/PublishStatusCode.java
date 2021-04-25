package com.supermap.gaf.data.mgt.service.publisher.constant;

/**
* @author:yw
* @Date 2021-3-12
 * 发布异常代码
 */
public enum PublishStatusCode {
    /**
     * 发布异常
     **/
    PUBLISH_EXCEPTION("发布异常"),
    /**
     * 发布中
     **/
    PUBLISH_ING("发布中"),
    /**
     * 不支持的服务类型
     **/
    PUBLISH_EXCEPTION_SERVICE_TYPE_NOT_SUPPORTED("不支持的服务类型"),

    //----------------------------队列
    /**
     * 不支持的服务类型
     **/
    PUBLISH_EXCEPTION_QUEUE_FULL("当前服务发布操作太多"),

    //----------------------------队列end

    //----------------------------参数
    /**
     * 参数校验异常
     **/
    PUBLISH_EXCEPTION_PARAMETER_VALID_ERROR("参数校验异常"),
    /**
     * 参数初始化异常
     **/
    PUBLISH_EXCEPTION_PARAMETER_INIT_ERROR("参数初始化异常"),
    /**
     * 参数不能为空
     **/
    PUBLISH_EXCEPTION_PARAMETER_NULL("参数不能为空"),
    /**
     * 参数不合法
     **/
    PUBLISH_EXCEPTION_PARAMETER_ILLEGAL("参数不合法"),

    //----------------------------参数end

    //----------------------------数据源
    /**
     * 不支持的数据源类型
     **/
    PUBLISH_EXCEPTION_DATASOURCE_TYPE_NOT_SUPPORTED("不支持的数据源类型"),

    //----------------------------数据源end

    //-----------------------------工作空间
    /**
     * 不支持的工作空间类型
     **/
    PUBLISH_EXCEPTION_WORKSPACE_TYPE_NOT_SUPPORTED("不支持的工作空间类型"),
    /**
     * 数据源连接信息不可用
     **/
    PUBLISH_EXCEPTION_DATASOURCE_UNAVAILABLE("数据源连接信息不可用"),
    /**
     * 工作空间不可用
     **/
    PUBLISH_EXCEPTION_WORKSPACE_UNAVAILABLE("工作空间不可用"),

    //-----------------------------工作空间end

    //-----------------------------地图模板
    /**
     * 未获取到任何地图模板
     **/
    PUBLISH_EXCEPTION_MAP_TEMPLATE_UNAVAILABLE("未获取到任何地图模板"),
    /**
     * 开始套用地图模板
     **/
    PUBLISH_EXCEPTION_MAP_TEMPLATE_APPLY_START("开始套用地图模板"),
    /**
     * 套用地图模板异常
     **/
    PUBLISH_EXCEPTION_MAP_TEMPLATE_APPLY_ERROR("套用地图模板异常"),

    //-----------------------------地图模板end

    //-----------------------------地图资源
    /**
     * 地图资源为空，跳过加载
     **/
    PUBLISH_EXCEPTION_MAP_RESOURCE_NULL("地图资源为空，跳过加载"),
    /**
     * 开始加载地图资源
     **/
    PUBLISH_EXCEPTION_MAP_RESOURCE_LOAD_START("开始加载地图资源"),
    /**
     * 加载地图资源成功
     **/
    PUBLISH_EXCEPTION_MAP_RESOURCE_LOAD_SUCCESS("加载地图资源成功"),
    /**
     * 加载地图资源异常
     **/
    PUBLISH_EXCEPTION_MAP_REOSURCE_LOAD_ERROR("加载地图资源异常");

    //-----------------------------地图资源end

    //-----------------------------iServer


    //-----------------------------iServer end



    private String describe;

    PublishStatusCode(String describe) {
        this.describe = describe;
    }


    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}

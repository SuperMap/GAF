package com.supermap.gaf.data.mgt.service.publisher.core;

/**
 * @author:yw
 * @Date 2021-3-12
 * 发布者接口，只用于标识发布方式
 */
public interface IPublisher {
    /**
     * 支持的发布方式
     *
     * @return
     */
    String supportPublisher();
}

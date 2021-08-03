/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service.publisher.core.registry;

import com.supermap.gaf.data.mgt.service.publisher.core.IPublisher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:yw
 * @Date 2021-3-12
 * @date:2021/3/25 服务发布者注册器，用于存储实现的服务发布者，供分发者调用进行实际的服务发布
 */
@Component
public class PublisherRegistry implements InitializingBean {

    @Autowired
    private ApplicationContext context;

    /**
     * 所有服务发布者的缓存
     */
    private final Map<String, IPublisher> publisherMap = new HashMap<>();

    /**
     * 获取服务发布者
     */
    public IPublisher getPublisher(String publisherTypeName) {
        return publisherMap.get(publisherTypeName);
    }

    /**
     * 初始化注册器
     */
    @Override
    public void afterPropertiesSet() {
        Map<String, IPublisher> publishers = context.getBeansOfType(IPublisher.class);
        for (IPublisher publisher : publishers.values()) {
            this.publisherMap.put(publisher.supportPublisher(), publisher);
        }
    }
}

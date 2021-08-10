/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service.publisher.manager;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.common.IServerManager;
import com.supermap.gaf.data.mgt.entity.PublishServiceParameter;
import com.supermap.gaf.data.mgt.service.publisher.constant.PublishStatusCode;
import com.supermap.gaf.data.mgt.service.publisher.core.AbstractPublisher;
import com.supermap.gaf.data.mgt.service.publisher.core.IPublisher;
import com.supermap.gaf.data.mgt.service.publisher.core.registry.PublisherRegistry;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author:yw
 * @Date 2021-3-12
 * @date:2021/3/25 服务发布分发者，负责服务发布流程的调度
 */
@Component
public class PublishDispatcher implements IPublishDispatcher {

    private static Logger logger = LogUtil.getLocLogger(PublishDispatcher.class);

    @Autowired
    private PublisherRegistry publisherRegistry;

    @Autowired
    private IServerManager iServerManager;

    @Override
    public MessageResult<String> dispatch(PublishServiceParameter originParameter) {
        // 0、内部调用分发者的参数校验方法，不合格直接返回异常消息，合格进入原始参数队列

        if (StringUtils.isEmpty(originParameter.getDatasourceType())) {
            return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_DATASOURCE_TYPE_NOT_SUPPORTED.getDescribe()).build();
        }
        // 数据源类型转为小写
        originParameter.setDatasourceType(originParameter.getDatasourceType().toLowerCase());
        IPublisher publisher = publisherRegistry.getPublisher(originParameter.getDatasourceType());
        if (null == publisher) {
            return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_DATASOURCE_TYPE_NOT_SUPPORTED.getDescribe()).build();
        }
        if (publisher instanceof AbstractPublisher) {
            AbstractPublisher abstractPublisher = (AbstractPublisher) publisher;
            logger.info("参数校验");
            MessageResult<String> validResult = abstractPublisher.verify(originParameter);
            if (!validResult.isSuccessed()) {
                return validResult;
            }
            logger.info("开始发布");
            return executePublish(originParameter);
        } else {
            return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_DATASOURCE_TYPE_NOT_SUPPORTED.getDescribe()).build();
        }
    }

    /**
     * 执行服务发布流程
     */
    private MessageResult<String> executePublish(PublishServiceParameter originParameter) {
        logger.info("开始申请iServer token");
        MessageResult<String> generateTokenResult = iServerManager.applyToken();
        return startPublish(generateTokenResult, originParameter);
    }

    /**
     * 【重要】使用同步方法，理由：
     * 1、防止快速多次触发，导致发布多个相同的服务
     * 2、在整个发布流程中保证事务的特性
     */
    private synchronized MessageResult<String> startPublish(MessageResult<String> generateTokenResult, PublishServiceParameter originParameter) {
        // 2、调用发布者初始化方法
        // 3、调用发布者开始发布方法
        // 4、调用发布者资源释放方法

        logger.info("准备开始发布服务流程，{}", originParameter);
        AbstractPublisher abstractPublisher = null;
        try {
            logger.info("获取服务发布者");
            IPublisher publisher = publisherRegistry.getPublisher(originParameter.getDatasourceType());
            if (null == publisher) {
                // 未找到服务发布者
                logger.error(PublishStatusCode.PUBLISH_EXCEPTION_DATASOURCE_TYPE_NOT_SUPPORTED.getDescribe());
                return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION_DATASOURCE_TYPE_NOT_SUPPORTED.getDescribe()).build();
            } else if (publisher instanceof AbstractPublisher) {
                abstractPublisher = (AbstractPublisher) publisher;

                // 将原始参数赋予服务发布者
                logger.info("原始参数赋予服务发布者");
                abstractPublisher.setParameter(originParameter);

                logger.info("初始化服务发布者");
                MessageResult<String> initResult = abstractPublisher.init();
                if (!initResult.isSuccessed()) {
                    logger.info("初始化失败");
                    return MessageResult.<String>failed(String.class).message(initResult.getMessage()).build();
                }
                logger.info("开始服务发布");
                if (generateTokenResult.isSuccessed()) {
                    return doPublish(generateTokenResult, abstractPublisher);
                }
            }
        } catch (Exception e) {
            logger.error("服务发布异常", e);
        } finally {
            logger.info("服务发布者资源释放");
            if (null != abstractPublisher) {
                abstractPublisher.dispose();
            }
            logger.info("服务发布结束");
        }
        return MessageResult.<String>failed(String.class).message(PublishStatusCode.PUBLISH_EXCEPTION.getDescribe()).build();
    }

    /**
     * 执行发布
     */
    private MessageResult<String> doPublish(MessageResult<String> generateTokenResult, AbstractPublisher abstractPublisher) {
        return abstractPublisher.doPublish(iServerManager.getAvailableIServerSetting().getHostServerUrl(), generateTokenResult.getData());
    }

}

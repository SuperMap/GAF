package com.supermap.gaf.data.mgt.service.publisher.core;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.PublishServiceParameter;

/**
 * @author:yw
 * @Date 2021-3-12
 * 服务发布抽象类，持有该发布者的原始参数 PublishServiceParameter
 **/
public abstract class AbstractPublisher implements IPublisher {

    /**
     * 参数
     */
    private PublishServiceParameter parameter;

    public PublishServiceParameter getParameter() {
        return parameter;
    }

    public void setParameter(PublishServiceParameter parameter) {
        this.parameter = parameter;
    }

    /**
     * 参数校验
     *
     * @param originParameter 服务发布参数
     * @return
     */
    public abstract MessageResult<String> verify(PublishServiceParameter originParameter);

    /**
     * 初始化，用于发布前临时参数构建
     *
     * @return
     */
    public abstract MessageResult<String> init();

    /**
     * 执行发布流程
     *
     * @param iServerUrl iserver地址
     * @param token      token
     * @return PublishResult
     */
    public abstract MessageResult<String> doPublish(String iServerUrl, String token);

    /**
     * 释放资源
     */
    public abstract void dispose();

}

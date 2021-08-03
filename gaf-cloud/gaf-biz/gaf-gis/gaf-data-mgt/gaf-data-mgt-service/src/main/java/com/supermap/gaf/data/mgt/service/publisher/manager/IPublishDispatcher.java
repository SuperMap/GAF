/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service.publisher.manager;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.PublishServiceParameter;

/**
 * @author:yw
 * @Date 2021-3-12
 * 服务发布分发者接口
 * @date:2021/3/25 【说明】该调度方式并未实现高并发能力
 */
public interface IPublishDispatcher {

    //------------参数校验合格后转为异步进行后面操作，异步实现方式为使用内存队列
    // 0、内部调用分发者的参数校验方法，不合格直接返回异常消息，合格进入【原始参数队列】

    // 此后的操作使用定时任务进行
    // 1、iserver的token获取
    // 2、调用发布者初始化方法
    // 3、调用发布者开始发布方法
    // 4、调用发布者资源释放方法

    //---------------调用iserver发布的结果，放入【发布结果队列】，使用另外定时任务进行处理，降低在调用iserver api进行服务发布的阻塞时间
    // 5、根据iserver返回发布的结果组装业务数据

    /**
     * 服务发布分发者，负责服务发布流程的调度
     *
     * @param originParameter
     * @return
     */
    MessageResult<String> dispatch(PublishServiceParameter originParameter);

}

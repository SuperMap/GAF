/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.utils;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.cal10n.LocLogger;

import com.supermap.gaf.services.resource.CommonResource;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class LogUtilTest {

    public static void main(String[] args) {
        LocLogger sysLogger = LogUtil.getLocLogger(LogUtilTest.class);
        sysLogger.info("测试系统日志输出");
        sysLogger.info("任务 {} 执行异常 {}", "a123", "a456");
        sysLogger.error("测试系统日志输出");

//        System.out.println(MessageFormat.format("任务 {} 执行异常 {}", "a123", "a456"));
        System.out.println(MessageFormat.format("任务 {0} 执行异常 {1}", "a123", "a456"));

        LocLogger operationLogger = LogUtil.getOperationLogger();
        operationLogger.info("测试操作日志");
        operationLogger.info("测试操作异常日志");

        Logger accessLogger = LogUtil.getAccessLogger();
        accessLogger.info("测试访问日志");
        accessLogger.info("测试访问异常日志");

        ResourceManager rm = new ResourceManager(CommonResource.class);
        LocLogger commonLogger = LogUtil.getLocLogger(CommonResource.class);
        commonLogger.info(CommonResource.DESUTIL_DECRYPTION_FAILED, "a123", "a456");
    }

}

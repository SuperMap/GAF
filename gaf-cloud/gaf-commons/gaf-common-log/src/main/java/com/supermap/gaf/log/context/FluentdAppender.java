/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.log.context;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.supermap.gaf.log.commontypes.FluentdConfig;
import org.fluentd.logger.FluentLogger;

import java.util.HashMap;
import java.util.Map;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class FluentdAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    public static FluentdConfig cfg;
    private FluentLogger fluentLogger;

    @Override
    public void start() {
        super.start();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (cfg != null && cfg.isEnable()) {
            if (fluentLogger == null) {
                FluentLogger testFluentLogger = FluentLogger.getLogger(cfg.getTagPrefix(), cfg.getHost(), cfg.getPort());
                if (testFluentLogger != null && testFluentLogger.isConnected()){
                    this.fluentLogger = testFluentLogger;
                }
            }
        } else {
            return;
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("level", eventObject.getLevel().toString());
        data.put("logger", eventObject.getLoggerName());
        data.put("message", eventObject.getMessage());
        data.put("application", cfg.getService());
        fluentLogger.log(cfg.getService(), data);
    }

    @Override
    public void stop() {
        try {
            super.stop();
        } finally {
            try {
                FluentLogger.closeAll();
            } catch (Exception e) {
                // pass
            }
        }
    }
}

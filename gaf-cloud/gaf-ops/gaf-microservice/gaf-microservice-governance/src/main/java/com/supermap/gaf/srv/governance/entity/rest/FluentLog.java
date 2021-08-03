/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.srv.governance.entity.rest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.SearchHit;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.supermap.gaf.srv.governance.constant.SrvConstant.*;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/12/2 8:54 AM
 */
@Data
@NoArgsConstructor
@ApiModel("日志记录对象")
public class FluentLog implements Serializable {
    private static final long serialVersionUID = -6732965418178501893L;

    @ApiModelProperty("日志记录id")
    private String id;

    @ApiModelProperty("日志级别")
    private String level;
    @ApiModelProperty("日志打印类")
    private String logger;
    @ApiModelProperty("日志消息")
    private String message;
    @ApiModelProperty("所属服务")
    private String application;
    @ApiModelProperty("日志时间戳")
    private String timestamp;

    public static FluentLog buildFromSearchHit(SearchHit searchHit) {
        FluentLog result = new FluentLog();
        if (searchHit == null) {
            return null;
        }
        Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
        if (sourceAsMap.containsKey(FLUENT_LEVEL_FILED_NAME)) {
            String level = (String) sourceAsMap.get(FLUENT_LEVEL_FILED_NAME);
            result.setLevel(level);
        }
        if (sourceAsMap.containsKey(FLUENT_LOGGER_FILED_NAME)) {
            String logger = (String) sourceAsMap.get(FLUENT_LOGGER_FILED_NAME);
            result.setLogger(logger);
        }
        if (sourceAsMap.containsKey(FLUENT_MESSAGE_FILED_NAME)) {
            String message = (String) sourceAsMap.get(FLUENT_MESSAGE_FILED_NAME);
            result.setMessage(message);
        }
        if (sourceAsMap.containsKey(FLUENT_APPLICATION_FILED_NAME)) {
            String application = (String) sourceAsMap.get(FLUENT_APPLICATION_FILED_NAME);
            result.setApplication(application);
        }
        if (sourceAsMap.containsKey(FLUENT_TIMESTAMP_FILED_NAME)) {
            String timestamp = (String) sourceAsMap.get(FLUENT_TIMESTAMP_FILED_NAME);
            timestamp = formatFluentTimestamp(timestamp);
            result.setTimestamp(timestamp);
        }
        result.setId(searchHit.getId());
        return result;
    }

    private static String formatFluentTimestamp(String fluentTimestamp) {
        SimpleDateFormat fluentFormat = new SimpleDateFormat(FLUENT_DATE_FORMAT);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        try {
            date = fluentFormat.parse(fluentTimestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return format.format(date);
    }
}

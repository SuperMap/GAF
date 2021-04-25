/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.srv.governance.service;


import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.sort.SortOrder;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/12/1 10:18 AM
 */
public interface FluentLogQueryService {

    /**
     * 获取fluent收集的日志
     * @param level
     * @param application
     * @param timestampSort
     * @param gte
     * @param lte
     * @param from
     * @param size
     * @return
     */
    SearchResponse searchLogResponse(String level, String application, SortOrder timestampSort, String gte, String lte, int from, int size);

}

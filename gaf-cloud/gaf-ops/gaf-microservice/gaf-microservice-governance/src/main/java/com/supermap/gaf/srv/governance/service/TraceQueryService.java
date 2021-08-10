/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.srv.governance.service;


import com.supermap.gaf.srv.governance.entity.rest.TraceTreeNodeResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/12/1 10:18 AM
 */
public interface TraceQueryService {

    /**
     * 获取trace收集的日志
     *
     * @param serviceName
     * @param username
     * @param timestampSort
     * @param gte
     * @param lte
     * @param from
     * @param size
     * @return
     */
    SearchResponse searchRootTraceResponse(String serviceName, String username, SortOrder timestampSort, String gte, String lte, int from, int size);

    /**
     * 通过traceIds查询所有span
     *
     * @param traceIds
     * @return
     */
    SearchResponse searchSpanResponseByTraceId(List<String> traceIds);

    /**
     * 获取结果trace树对象列表
     *
     * @param searchResponse
     * @return
     */
    TraceTreeNodeResponse getTraceTreeResponseBySpans(SearchResponse searchResponse);


    /**
     * step 1: 搜索所有根span，获取traceId
     * step 2: 获取所有属于traceId的span
     * step 3: 树形结构化span
     *
     * @param serviceName
     * @param username
     * @param timestampSort
     * @param gte
     * @param lte
     * @param from
     * @param size
     * @return
     */
    TraceTreeNodeResponse getTraceTreeNodeResponse(String serviceName, String username, SortOrder timestampSort, String gte, String lte, int from, int size);
}

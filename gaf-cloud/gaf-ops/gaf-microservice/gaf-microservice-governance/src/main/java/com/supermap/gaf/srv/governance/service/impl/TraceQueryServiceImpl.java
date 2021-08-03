/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.srv.governance.service.impl;

import com.supermap.gaf.srv.governance.entity.rest.TraceTreeNode;
import com.supermap.gaf.srv.governance.entity.rest.TraceTreeNodeResponse;
import com.supermap.gaf.srv.governance.service.TraceQueryService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.util.*;
import java.util.stream.Collectors;

import static com.supermap.gaf.srv.governance.constant.SrvConstant.*;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/12/1 10:18 AM
 */
@Slf4j
@Service
public class TraceQueryServiceImpl implements TraceQueryService {
    @Resource
    private RestHighLevelClient customClient;


    @Override
    public SearchResponse searchRootTraceResponse(String serviceName, String username, SortOrder timestampSort, String gte, String lte, int from, int size) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(from);
        sourceBuilder.size(size);
        sourceBuilder.sort(TRACE_TIMESTAMP_FILED_NAME, timestampSort);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        QueryBuilder timeRangeQuery = QueryBuilders.rangeQuery(TRACE_TIMESTAMP_FILED_NAME).gte(gte).lte(lte);
        QueryBuilder existFieldQuery = QueryBuilders.existsQuery(TRACE_MUST_EXIST_FILED_NAME);
        QueryBuilder kindMatchPhraseQuery = QueryBuilders.matchPhraseQuery(TRACE_KIND_FILED_NAME, TRACE_KIND_SERVER);
        QueryBuilder notExistFieldQuery = QueryBuilders.existsQuery(TRACE_MUST_NOT_EXIST_FILED_NAME);

        boolQueryBuilder.must(timeRangeQuery);
        boolQueryBuilder.must(existFieldQuery);
        boolQueryBuilder.must(kindMatchPhraseQuery);
        boolQueryBuilder.mustNot(notExistFieldQuery);

        if (!StringUtils.isEmpty(serviceName)) {
            QueryBuilder serviceNameMatchPhraseQuery = QueryBuilders.matchPhraseQuery(TRACE_SERVICENAME_FIELD_NAME, serviceName);
            boolQueryBuilder.must(serviceNameMatchPhraseQuery);
        }
        //tags里面的字段无索引，_q里面存放
        if (!StringUtils.isEmpty(username)) {
            QueryBuilder usernameMatchQuery = QueryBuilders.termQuery("_q", "username=" + username);
            boolQueryBuilder.must(usernameMatchQuery);
        }

        QueryBuilder rootQueryBuilder = QueryBuilders.boolQuery().filter(boolQueryBuilder);
        sourceBuilder.query(rootQueryBuilder);

        SearchRequest searchRequest = new SearchRequest(TRACE_PREFIX);
        searchRequest.source(sourceBuilder);


        SearchResponse response = null;
        try {
            response = customClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询链路追踪Root Trace数据错误");
        }
        return response;
    }

    @Override
    public SearchResponse searchSpanResponseByTraceId(List<String> traceIds) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(MAX_RESULT_WINDOW);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        QueryBuilder kindMatchPhraseQuery = QueryBuilders.matchPhraseQuery(TRACE_KIND_FILED_NAME, TRACE_KIND_SERVER);
        QueryBuilder traceIdsMatchPhraseQuery = QueryBuilders.termsQuery(TRACE_MUST_EXIST_FILED_NAME, traceIds.toArray());

        boolQueryBuilder.must(kindMatchPhraseQuery);
        boolQueryBuilder.must(traceIdsMatchPhraseQuery);


        QueryBuilder rootQueryBuilder = QueryBuilders.boolQuery().filter(boolQueryBuilder);
        sourceBuilder.query(rootQueryBuilder);

        SearchRequest searchRequest = new SearchRequest(TRACE_PREFIX);
        searchRequest.source(sourceBuilder);


        SearchResponse response = null;
        try {
            response = customClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询链路追踪Span of Trace数据错误");
        }
        return response;
    }

    @Override
    public TraceTreeNodeResponse getTraceTreeResponseBySpans(SearchResponse searchResponse) {
        //转换结构
        List<TraceTreeNode> traceTreeNodes = new ArrayList<>();
        SearchHits searchHits = searchResponse.getHits();
        for (SearchHit searchHit : searchHits) {
            traceTreeNodes.add(TraceTreeNode.buildTraceTreeNode(searchHit));
        }
        //分traceId整理
        Map<String, List<TraceTreeNode>> traceMap = new HashMap<>(16);
        for (TraceTreeNode node : traceTreeNodes) {
            String key = node.getTraceId();
            if (!traceMap.containsKey(key)) {
                traceMap.put(key, new ArrayList<TraceTreeNode>());
            }
            traceMap.get(key).add(node);
        }
        //构建TraceTreeNode
        TraceTreeNodeResponse response = new TraceTreeNodeResponse();
        List<TraceTreeNode> data = new ArrayList<>();
        Set<String> keys = traceMap.keySet();
        for (String key : keys) {
            TraceTreeNode node = TraceTreeNode.buildTraceTreeNodeToOne(traceMap.get(key));
            data.add(node);
        }
        response.setData(data);
        return response;
    }

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
    @Override
    public TraceTreeNodeResponse getTraceTreeNodeResponse(String serviceName, String username, SortOrder timestampSort, String gte, String lte, int from, int size) {
        //TODO es连接池检查
        SearchResponse rootTraceResponse = searchRootTraceResponse(serviceName, username, timestampSort, gte, lte, from, size);
        SearchHits searchHits = rootTraceResponse.getHits();
        List<String> traceIds = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            traceIds.add((String) hit.getSourceAsMap().get(TRACE_MUST_EXIST_FILED_NAME));
        }
        SearchResponse spansResponse = searchSpanResponseByTraceId(traceIds);
        TraceTreeNodeResponse response = getTraceTreeResponseBySpans(spansResponse);
        //排序
        List<TraceTreeNode> data = response.getData();
        data = data.stream().sorted(Comparator.comparingLong(item -> Long.valueOf(item.getTimestamp()))).collect(Collectors.toList());
        if (timestampSort.equals(SortOrder.DESC)) {
            Collections.reverse(data);
        }
        response.setData(data);
        response.setTotalHits(searchHits.getTotalHits().value);
        return response;
    }
}

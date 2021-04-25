/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.srv.governance.service.impl;

import com.supermap.gaf.srv.governance.service.FluentLogQueryService;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import static com.supermap.gaf.srv.governance.constant.SrvConstant.*;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/12/1 10:18 AM
 */
@Slf4j
@Service
public class FluentLogQueryServiceImpl implements FluentLogQueryService {
    @Resource
    private RestHighLevelClient customClient;

    @Override
    public SearchResponse searchLogResponse(String level, String application, SortOrder timestampSort, String gte, String lte, int from, int size) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(from);
        sourceBuilder.size(size);
        sourceBuilder.sort(FLUENT_TIMESTAMP_FILED_NAME,timestampSort);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        QueryBuilder timeRangeQuery = QueryBuilders.rangeQuery(FLUENT_TIMESTAMP_FILED_NAME).gte(gte).lte(lte);

        boolQueryBuilder.must(timeRangeQuery);

        if (!StringUtils.isEmpty(application)){
            QueryBuilder appMatchPhraseQuery = QueryBuilders.matchPhraseQuery(FLUENT_APPLICATION_FILED_NAME,application);
            boolQueryBuilder.must(appMatchPhraseQuery);
        }
        if (!StringUtils.isEmpty(level)){
            QueryBuilder levelMatchQuery = QueryBuilders.matchQuery(FLUENT_LEVEL_FILED_NAME,level);
            boolQueryBuilder.must(levelMatchQuery);
        }

        QueryBuilder rootQueryBuilder = QueryBuilders.boolQuery().filter(boolQueryBuilder);
        sourceBuilder.query(rootQueryBuilder);

        SearchRequest searchRequest = new SearchRequest(FLUENT_LOG_PREFIX);
        searchRequest.source(sourceBuilder);

        SearchResponse response = null;
        try {
            response = customClient.search(searchRequest, RequestOptions.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
            log.error("查询fluent日志错误");
        }
        return response;
    }


}

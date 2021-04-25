/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.srv.governance.configuration;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.srv.governance.entity.rest.TraceTreeNodeResponse;
import com.supermap.gaf.srv.governance.resources.LogQueryResource;
import com.supermap.gaf.srv.governance.resources.TraceQueryResource;
import com.supermap.gaf.srv.governance.service.FluentLogQueryService;
import com.supermap.gaf.srv.governance.service.TraceQueryService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchModule;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/12/1 8:54 AM
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dqc")
public class ESTest {
    @Resource
    private FluentLogQueryService fluentLogQueryService;
    @Autowired
    private LogQueryResource logQueryResource;
    @Autowired
    private TraceQueryService traceQueryService;
    @Autowired
    private TraceQueryResource traceQueryResource;

    @Test
    public void testFluent() throws Exception{
        logQueryResource.listLog(null,null,"asc","now/d","now",0,5);
//        logQueryResource.listLog("gaf-microservice-governance","INFO","asc","2020-12-01T10:00:00.000+08:00","now",0,5);
//        SearchResponse searchResponse = fluentLogQueryService.listLog("s","gaf-dev.gaf-microservice-governance", SortOrder.ASC,"2020-12-01T10:00:00.000+08:00","now",0,10);
        System.out.println(1);

    }

    @Test
    public void testTrace() throws Exception{
        traceQueryService.searchRootTraceResponse(null,null,SortOrder.ASC,"now-10d","now",0,5);
        System.out.println(1);

    }
    @Test
    public void testTraceForSpan() throws Exception{
        traceQueryService.searchSpanResponseByTraceId(Arrays.asList("6524d51905fcb2fd","3fab7a8e17f890db"));
        System.out.println(1);
    }

    @Test
    public void testBuildTraceTree() throws Exception{
        SearchResponse searchResponse = traceQueryService.searchSpanResponseByTraceId(Arrays.asList("6524d51905fcb2fd","3fab7a8e17f890db"));
        traceQueryService.getTraceTreeResponseBySpans(searchResponse);
        System.out.println(1);
    }

    @Test
    public void testTraceTreeResource() throws Exception{
        MessageResult<TraceTreeNodeResponse> response = traceQueryResource.listTrace(null,null,"asc","now-10d","now",0,5);
        System.out.println(1);
    }




}

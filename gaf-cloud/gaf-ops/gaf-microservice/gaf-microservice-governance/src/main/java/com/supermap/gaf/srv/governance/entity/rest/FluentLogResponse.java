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
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/12/2 8:54 AM
 */
@Data
@NoArgsConstructor
@ApiModel("日志记录查询对象")
public class FluentLogResponse implements Serializable {
    private static final long serialVersionUID = -3312643220836890171L;

    @ApiModelProperty("查询日志总数")
    private Long hitsTotal;
    @ApiModelProperty("日志数据列表")
    private List<FluentLog> fluentLogs;


    public static FluentLogResponse buildFromSearchResponse(SearchResponse searchResponse) {
        FluentLogResponse result = new FluentLogResponse();

        Long hitsTotal = searchResponse.getHits().getTotalHits().value;

        result.setHitsTotal(hitsTotal);

        List<FluentLog> fluentLogs = new ArrayList<>();
        result.setFluentLogs(fluentLogs);

        SearchHits searchHits = searchResponse.getHits();
        for (SearchHit searchHit : searchHits) {
            fluentLogs.add(FluentLog.buildFromSearchHit(searchHit));
        }
        return result;
    }


}

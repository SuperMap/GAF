/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.srv.governance.resources;


import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.srv.governance.entity.rest.FluentLogResponse;
import com.supermap.gaf.srv.governance.service.FluentLogQueryService;
import com.supermap.gaf.srv.governance.utils.TimestampUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author dqc
 * @date:2021/3/25
 * /srv-governance/log
 */
@Component
@Api(value = "日志查询接口")
public class LogQueryResource {

    @Autowired
    private FluentLogQueryService fluentLogQueryService;


    @ApiOperation(value = "日志查询", notes = "查询所有微服务的日志记录，支持分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "application",value = "微服务名",paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "level",value = "日志级别",paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "timeSortOrder",value = "时间排序",paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "startTime",value = "开始时间",paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "endTime",value = "结束时间",paramType = "query",dataType = "string"),
            @ApiImplicitParam(name = "pageIndex",value = "分页索引",paramType = "query",dataType = "integer"),
            @ApiImplicitParam(name = "pageSize",value = "每页条数",paramType = "query",dataType = "integer")
    })
    @GET
    @Path("/")
    @Produces(APPLICATION_JSON)
    public MessageResult<FluentLogResponse> listLog(@QueryParam("application") String application,
                                                    @QueryParam("level") String level,
                                                    @QueryParam("timeSortOrder")@DefaultValue("asc") String timeSortOrder,
                                                    @QueryParam("startTime")@DefaultValue("now/d") String startTime,
                                                    @QueryParam("endTime")@DefaultValue("now") String endTime,
                                                    @QueryParam("pageIndex")@DefaultValue("0") Integer pageIndex,
                                                    @QueryParam("pageSize")@DefaultValue("20") Integer pageSize) {
        int size = pageSize;
        int from = pageIndex * size;
        SortOrder sortOrder = SortOrder.fromString(timeSortOrder);
        String startTimeCalendar = startTime;
        String endTimeCalendar = endTime;
        if (StringUtils.isNumeric(startTime)){
            startTimeCalendar = TimestampUtils.timestampToCalendar(Long.valueOf(startTime));
        }
        if (StringUtils.isNumeric(endTime)){
            endTimeCalendar = TimestampUtils.timestampToCalendar(Long.valueOf(endTime));
        }
        SearchResponse searchResponse = fluentLogQueryService.searchLogResponse(level,application,sortOrder,startTimeCalendar,endTimeCalendar,from,size);
        FluentLogResponse fluentLogResponse = FluentLogResponse.buildFromSearchResponse(searchResponse);
        return MessageResult.successe(FluentLogResponse.class).data(fluentLogResponse).build();
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.analysis.resource.spatial;


import com.supermap.gaf.analysis.constant.DbFieldNameConstant;
import com.supermap.gaf.analysis.dao.SpatialAnalysisResultDao;
import com.supermap.gaf.analysis.entity.RecordSetVO;
import com.supermap.gaf.analysis.entity.OverlayParamVO;
import com.supermap.gaf.analysis.entity.SpatialAnalysisResult;
import com.supermap.gaf.analysis.entity.SpatialAnalysisResultSearchParameter;
import com.supermap.gaf.analysis.service.OverlayService;
import com.supermap.gaf.commontypes.MessageResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


/**
 * @author dqc
 * @date:2021/3/25
 * /analysis/spatial/overlay
 */
@Path("/")
@Api(value = "空间分析-叠加分析接口")
@Component
public class OverlayResource {

    @Autowired
    private OverlayService overlayService;
    @Autowired
    private SpatialAnalysisResultDao spatialAnalysisResultDao;

    @ApiOperation(value = "叠加分析", notes = "通过OverlayParamVO实体类的参数，对两个数据集或一个数据集一个Geo对象进行叠加分析")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "overlayParamVO",value = "叠加分析请求参数",paramType = "body",dataTypeClass = OverlayParamVO.class)
    })
    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public MessageResult<String> overlay(OverlayParamVO overlayParamVO) {
        return overlayService.overlay(overlayParamVO);
    }

    @ApiOperation(value = "叠加分析结果查询", notes = "通过结果记录ID查询对应分析任务的结果，支持分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resultId",value = "结果记录ID",paramType = "path",dataType = "string",required = true,example = "abcde"),
            @ApiImplicitParam(name = "pageIndex",value = "分页索引",paramType = "query",dataType = "integer",example = "0"),
            @ApiImplicitParam(name = "pageSize",value = "每页条数",paramType = "query",dataType = "integer",example = "10")
    })
    @GET
    @Path("/result/{resultId}")
    @Produces(APPLICATION_JSON)
    public MessageResult<RecordSetVO> getAnalysisResult(@PathParam("resultId") String resultId,
                                                        @DefaultValue("0") @QueryParam("pageIndex") Integer pageIndex,
                                                        @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        return overlayService.getResultRecordSet(resultId, pageIndex, pageSize);
    }

    @ApiOperation(value = "分析结果记录查询", notes = "通过分析任务名称过滤所有分析结果记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "分析任务名称",paramType = "path",dataType = "string",required = true,example = "test"),
            @ApiImplicitParam(name = "pageNum",value = "分页索引",paramType = "query",dataType = "integer",example = "0"),
            @ApiImplicitParam(name = "pageSize",value = "每页条数",paramType = "query",dataType = "integer",example = "10")
    })
    @GET
    @Path("/result/records")
    @Produces(APPLICATION_JSON)
    public MessageResult<Map> getAnalysisResultRecords(@QueryParam("name") String name,
                                                        @DefaultValue("0") @QueryParam("pageNum") Integer pageNum,
                                                        @DefaultValue("10") @QueryParam("pageSize") Integer pageSize) {
        if (pageNum == null || pageNum < 1){
            pageNum = 1;
        }
        Integer offset = (pageNum - 1) * pageSize;
        SpatialAnalysisResultSearchParameter searchParameter = new SpatialAnalysisResultSearchParameter();
        searchParameter.setKeyword(name);
        searchParameter.setOffset(offset);
        searchParameter.setPageSize(pageSize);
        Map<String, Object> result = new HashMap<>(16);
        List<SpatialAnalysisResult> pageList = spatialAnalysisResultDao.querySpatialAnalysisResults(searchParameter);
        int totalCount = spatialAnalysisResultDao.pageListCount();
        result.put(DbFieldNameConstant.PAGE_LIST, pageList);
        result.put(DbFieldNameConstant.TOTAL_COUNT, totalCount);
        return MessageResult.successe(Map.class).data(result).status(200).message("查询成功").build();
    }


}

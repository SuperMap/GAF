/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.analysis.resource.spatial;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.gaf.analysis.entity.DatasetOverlayParamVO;
import com.supermap.gaf.analysis.utils.HttpClientUtil;
import com.supermap.gaf.commontypes.MessageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.ws.rs.*;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


/**
 * @author dqc
 * @date:2021/3/25
 * /analysis/spatial/overlay-iserver
 */
@Path("/")
@Api(value = "空间分析-叠加分析接口-iserver")
@Component
public class OverlayIserverResource {


    /**
     * 此接口直接请求iserver分析服务接口，转发分析结果.
     * 设置query参数returnContent=true，可以直接返回记录集，而不是记录集资源id及地址
     * @param datasetOverlayParamVO
     * @return
     */
    @ApiOperation(value = "数据集叠加分析(Iserver实现)", notes = "对两个数据集或一个数据集一个Geo对象进行叠加分析(Iserver实现)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "datasetOverlayParamVO",value = "叠加分析请求参数",paramType = "body",dataTypeClass = DatasetOverlayParamVO.class)
    })
    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Object overlayViaIserver(DatasetOverlayParamVO datasetOverlayParamVO){
        if (datasetOverlayParamVO == null){
            return MessageResult.failed(JSONObject.class).message("未传递iserver叠加分析参数").build();
        }
        if (StringUtils.isEmpty(datasetOverlayParamVO.getSpatialAnalystDatasetOverlayUrl())){
            return MessageResult.failed(JSONObject.class).message("未传递iserver叠加分析地址参数").build();
        }
        //设置参数
        String datasetOverlayPostUrl = datasetOverlayParamVO.buildDatasetOverlayPostUrl();
        String datasetOverlayPostParam = JSON.toJSONString(datasetOverlayParamVO.buildDatasetOverlayPostParameter());

        return HttpClientUtil.execute(datasetOverlayPostUrl,datasetOverlayPostParam);
    }




    @ApiOperation(value = "获取iserver分析结果记录集", notes = "通过分析结果资源地址获取iserver分析结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "overlayResultResource",value = "分析结果资源地址",paramType = "query",dataType = "string")
    })
    @Path("/result")
    @GET
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Object getIserverOverlayResultResource(@QueryParam("overlayResultResource") String overlayResultResource){
        return HttpClientUtil.get(overlayResultResource);
    }


}

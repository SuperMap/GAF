/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.analysis.resource.spatial;


import com.alibaba.fastjson.JSON;
import com.supermap.gaf.analysis.entity.OverlayAnalystParamVO;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.services.components.commontypes.Recordset;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


/**
 * @author dqc
 * @date:2021/3/25
 * /analysis/spatial/overlay-geoprocessing
 */
@Path("/")
@Api(value = "空间分析-叠加分析接口-Geoprocessing")
@Component
public class OverlayGeoprocessingResource {


    @ApiOperation(value = "数据集叠加分析(Geoprocessing实现)", notes = "对两个数据集或一个数据集一个Geo对象进行叠加分析(Geoprocessing实现)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "overlayAnalystParamVO",value = "叠加分析请求参数",paramType = "body",dataTypeClass = OverlayAnalystParamVO.class)
    })
    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public MessageResult<Recordset> overlayViaGeoprocessing(OverlayAnalystParamVO overlayAnalystParamVO){
        String data = "{ \"features\": [ { \"stringID\": null, \"fieldNames\": [ \"SmID\", \"SmUserID\", \"SmArea\", \"SmPerimeter\", \"SmGeometry\", \"F_1\", \"F_2\", \"F_3\" ], \"geometry\": { \"center\": { \"x\": -35.25968747465302, \"y\": -364.38706534025 }, \"parts\": [ 6 ], \"style\": null, \"prjCoordSys\": null, \"id\": 1, \"type\": \"REGION\", \"partTopo\": [ 1 ], \"points\": [ { \"x\": 57.78609257251628, \"y\": -320.3197375638 }, { \"x\": -144.36459134939997, \"y\": -320.3197375638 }, { \"x\": -144.36459134939997, \"y\": -408.4543931167 }, { \"x\": 83.39704355915455, \"y\": -408.4543931167 }, { \"x\": 73.88259154188108, \"y\": -364.4896252707613 }, { \"x\": 57.78609257251628, \"y\": -320.3197375638 } ] }, \"fieldValues\": [ \"1\", \"0\", \"2.121957325913705E14\", \"0.0\", \"\", \"10\", \"绿化区\", \"13.0\" ], \"ID\": 1 }, { \"stringID\": null, \"fieldNames\": [ \"SmID\", \"SmUserID\", \"SmArea\", \"SmPerimeter\", \"SmGeometry\", \"F_1\", \"F_2\", \"F_3\" ], \"geometry\": { \"center\": { \"x\": 739.9808389975277, \"y\": -364.38706534025 }, \"parts\": [ 6 ], \"style\": null, \"prjCoordSys\": null, \"id\": 2, \"type\": \"REGION\", \"partTopo\": [ 1 ], \"points\": [ { \"x\": 380.6629742691254, \"y\": -408.4543931167 }, { \"x\": 1100.2035749439, \"y\": -408.4543931167 }, { \"x\": 1100.2035749439, \"y\": -320.3197375638 }, { \"x\": 381.6831808034282, \"y\": -320.3197375638 }, { \"x\": 379.24799978400415, \"y\": -376.06393805193994 }, { \"x\": 380.6629742691254, \"y\": -408.4543931167 } ] }, \"fieldValues\": [ \"2\", \"0\", \"1.1639249128831946E11\", \"0.0\", \"\", \"10\", \"绿化区\", \"13.0\" ], \"ID\": 2 }, { \"stringID\": null, \"fieldNames\": [ \"SmID\", \"SmUserID\", \"SmArea\", \"SmPerimeter\", \"SmGeometry\", \"F_1\", \"F_2\", \"F_3\" ], \"geometry\": { \"center\": { \"x\": 968.0015916143964, \"y\": 386.0928804289 }, \"parts\": [ 6 ], \"style\": null, \"prjCoordSys\": null, \"id\": 3, \"type\": \"REGION\", \"partTopo\": [ 1 ], \"points\": [ { \"x\": 933.1270845008979, \"y\": 1028.4075671103 }, { \"x\": 899.8975395961963, \"y\": 992.163787309977 }, { \"x\": 899.8975395961963, \"y\": -256.2218062525001 }, { \"x\": 1036.1056436325964, \"y\": -256.2218062525001 }, { \"x\": 1036.1056436325964, \"y\": 1028.4075671103 }, { \"x\": 933.1270845008979, \"y\": 1028.4075671103 } ] }, \"fieldValues\": [ \"3\", \"0\", \"1.7116531688756862E14\", \"0.0\", \"\", \"1\", \"休息区\", \"16.0\" ], \"ID\": 3 } ], \"fieldCaptions\": [ \"SmID\", \"SmUserID\", \"SmArea\", \"SmPerimeter\", \"SmGeometry\", \"人口数\", \"区域\", \"绿化率\" ], \"fieldTypes\": [ \"INT32\", \"INT32\", \"DOUBLE\", \"DOUBLE\", \"LONGBINARY\", \"INT32\", \"TEXT\", \"DOUBLE\" ], \"datasetName\": null, \"fields\": [ \"SmID\", \"SmUserID\", \"SmArea\", \"SmPerimeter\", \"SmGeometry\", \"F_1\", \"F_2\", \"F_3\" ] }";
        Recordset resultData = JSON.parseObject(data,Recordset.class);
        return MessageResult.successe(Recordset.class).data(resultData).build();
    }


}

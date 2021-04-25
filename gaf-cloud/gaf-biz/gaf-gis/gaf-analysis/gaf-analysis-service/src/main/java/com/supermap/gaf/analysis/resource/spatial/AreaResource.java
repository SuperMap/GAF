/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.analysis.resource.spatial;


import com.supermap.gaf.analysis.service.AreaService;
import com.supermap.gaf.analysis.utils.CommontypeConversionUtilsExt;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.services.components.commontypes.DatasourceConnectionInfo;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.services.providers.util.CommontypesConversion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


/**
 * @author dqc
 * @date:2021/3/25
 * /analysis/spatial/area
 */
@Path("/")
@Api(value = "空间分析-面积查询接口")
@Component
public class AreaResource {
    @Autowired
    private AreaService iObjectService;

    @ApiOperation(value = "计算数据集面积", notes = "计算数据集面积")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSetName",value = "数据集名称",paramType = "path",dataType = "string",required = true,example = "region"),
            @ApiImplicitParam(name = "connectionInfo",value = "数据源连接信息",paramType = "body",dataTypeClass = DatasourceConnectionInfo.class)
    })
    @Path("/dataset/{dataSetName}")
    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public MessageResult<String> getDataSetArea(@PathParam("dataSetName") String dataSetName,
                                                DatasourceConnectionInfo connectionInfo) {
        com.supermap.data.DatasourceConnectionInfo ugcDatasourceConnectionInfo = CommontypeConversionUtilsExt.getUgcDatasourceConnectionInfo(connectionInfo);
        String result = iObjectService.calculateDataSetArea(ugcDatasourceConnectionInfo, dataSetName);
        return MessageResult.successe(String.class).data(result).build();
    }

    @ApiOperation(value = "计算传入图斑面积", notes = "计算传入图斑面积")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "geometries",value = "地理几何对象列表",paramType = "body",allowMultiple = true,dataTypeClass = Geometry.class,required = true)
    })
    @Path("/geometry")
    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public MessageResult<String> getGeometryArea(List<Geometry> geometries) {
        List<com.supermap.data.Geometry> ugoGeometries = new ArrayList<>();
        for (Geometry geo : geometries) {
            ugoGeometries.add(CommontypesConversion.getUGOGeometry(geo));
        }
        String result = iObjectService.getGeometryArea(ugoGeometries);
        return MessageResult.successe(String.class).data(result).build();
    }


}

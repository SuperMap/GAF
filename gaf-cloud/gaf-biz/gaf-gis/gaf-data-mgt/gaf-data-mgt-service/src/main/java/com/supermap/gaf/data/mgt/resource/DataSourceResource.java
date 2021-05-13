/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.BuildS3MParameter;
import com.supermap.gaf.data.mgt.entity.DataSourceInfo;
import com.supermap.gaf.data.mgt.entity.GDataset;
import com.supermap.gaf.data.mgt.service.DataSourceService;
import com.supermap.gaf.data.mgt.service.S3MCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author:yd
 * @date:2021/3/25
 * @Date 2021-3-01
 */

@Component
@Api(value = "数据源接口")
public class DataSourceResource {

    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private S3MCacheService s3mCacheService;

    @ApiOperation(value = "查找数据源下的数据集列表", notes = "根据数据源信息查找数据集列表")
    @ApiImplicitParam(name = "dataSourceInfo",value = "数据源实体类",paramType = "body",dataTypeClass = DataSourceInfo.class)
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/list-dataset")
    public MessageResult<List<GDataset>> listDatasets(DataSourceInfo dataSourceInfo) {
        List<GDataset> result = dataSourceService.listDataset(dataSourceInfo);
        return MessageResult.data(result).message("查询成功").build();
    }

    @ApiOperation(value = "根据数据源id查找数据源下的数据集列表",notes = "数据源应该是空间数据源，若未非空间数据源则返回空集合")
    @ApiImplicitParam(name = "datasourceId",value = "数据源id",paramType = "path",dataTypeClass = String.class)
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{datasourceId}/datasets")
    public MessageResult<List<GDataset>> listDatasetsById(@PathParam("datasourceId") String datasourceId) {
        List<GDataset> result = dataSourceService.listDataset(datasourceId);
        return MessageResult.data(result).message("查询成功").build();
    }



    @ApiOperation(value = "生成s3m切片", notes = "生成s3m切片")
    @ApiImplicitParam(name = "buildS3MParameter",value = "构建s3m实体类",paramType = "body",dataTypeClass = BuildS3MParameter.class)
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/s3m")
    public MessageResult<String> buildS3m(BuildS3MParameter buildS3mParameter) {
        return s3mCacheService.buildS3M(buildS3mParameter.getDataSourceInfo(), buildS3mParameter.getDatasetNames(), buildS3mParameter.isOverWrite(), buildS3mParameter.getOutputFolder());
    }

}

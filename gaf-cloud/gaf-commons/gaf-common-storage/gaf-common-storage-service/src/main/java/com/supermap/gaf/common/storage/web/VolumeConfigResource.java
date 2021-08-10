/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.common.storage.web;

import com.supermap.gaf.common.storage.handler.MinioConfigHandlerI;
import com.supermap.gaf.commontypes.MessageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author:yj
 * @date:2021/3/25 The type Minio service resource.
 */
@Api("配置操作相关接口")
public class VolumeConfigResource {

    private MinioConfigHandlerI minioConfigHandlerI;

    public VolumeConfigResource(MinioConfigHandlerI minioConfigHandlerI) {
        this.minioConfigHandlerI = minioConfigHandlerI;
    }

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取挂载配置", notes = "获取挂载配置")
    @Path("/config-ini")
    public MessageResult<String> getVolumeConfigIni(@QueryParam("isWin") boolean isWin) {
        return MessageResult.successe(String.class).data(minioConfigHandlerI.getVolumeConfigIni(isWin)).status(200).message("查询成功").build();
    }

}

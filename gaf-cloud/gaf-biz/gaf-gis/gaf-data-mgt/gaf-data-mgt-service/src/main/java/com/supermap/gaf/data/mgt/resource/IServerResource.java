/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.resource;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.common.IServerManager;
import com.supermap.gaf.data.mgt.entity.HostServerSetting;
import com.supermap.gaf.data.mgt.entity.IServerWorkspace;
import com.supermap.gaf.data.mgt.service.IServerWorkspaceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author : yd
 * @date : 19
 */
@Component
@Api(value = "iserver接口")
public class IServerResource {

    @Autowired
    private IServerWorkspaceService iServerWorkspaceService;

    @Autowired
    private IServerManager iServerManager;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/setting")
    public MessageResult<HostServerSetting> querySetting() {
        HostServerSetting hostServerSetting = iServerManager.getAvailableIServerSetting();
        return MessageResult.successe(HostServerSetting.class).data(hostServerSetting).status(200).message("查询成功").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/workspaces/list-by-type")
    public MessageResult<List> queryWorkspaces(@QueryParam("serviceType") String serviceType) {
        List<IServerWorkspace> workspaceList = iServerWorkspaceService.queryWorkspaces(serviceType);
        return MessageResult.successe(List.class).data(workspaceList).status(200).message("查询成功").build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/workspaces/remove-by-name")
    public MessageResult<Boolean> deleteWorkspaces(@QueryParam("workspaceName") String workspaceName) {
        return iServerWorkspaceService.deleteWorkspacesByName(workspaceName);
    }

}

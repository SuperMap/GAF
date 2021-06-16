/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service.impl;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.common.IServerManager;
import com.supermap.gaf.data.mgt.entity.IServerWorkspace;
import com.supermap.gaf.data.mgt.service.IServerWorkspaceService;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * iserver工作空间服务实现类
 *
 * @author : yd
 * @date : 2021-3-19
 */
@Service
public class IServerWorkspaceServiceImpl implements IServerWorkspaceService {
    private static final Logger logger = LogUtil.getLocLogger(IServerWorkspaceServiceImpl.class);

    @Autowired
    private IServerManager iServerManager;

    @Override
    public List<IServerWorkspace> queryWorkspaces(String serviceType) {
        return iServerManager.queryWorkspacesByType(serviceType);
    }

    @Override
    public MessageResult<Boolean> deleteWorkspacesByName(String workspaceName) {
        return iServerManager.removeWorkspace(workspaceName);
    }


}

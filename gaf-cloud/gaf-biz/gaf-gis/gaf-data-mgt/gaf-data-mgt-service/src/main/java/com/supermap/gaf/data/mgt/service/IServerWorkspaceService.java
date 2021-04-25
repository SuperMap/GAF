/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.data.mgt.entity.IServerWorkspace;

import java.util.List;

/**
 * iserver工作空间服务
 *
 * @author : yd
 * @date:2021/3/25
 * @Date : 2021-3-19
 */
public interface IServerWorkspaceService {

    /**
     * 查询iserver上工作空间列表信息
     *
     * @param serviceType 服务类型，为空则查询所有工作空间
     * @return
     */
    List<IServerWorkspace> queryWorkspaces(String serviceType);

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.DataWorkspace;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.DataWorkspaceSelectVo;
import com.supermap.gaf.data.mgt.vo.WorkspaceIdServiceType;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.List;

/**
 * 工作空间服务类
 * @author zrc 
 * @date yyyy-mm-dd
 */
public interface DataWorkspaceService {

    /**
     * id查询工作空间
     * @param workspaceId
     * @return
     */
    DataWorkspace getById(String workspaceId);
	
	/**
     * 分页条件查询
     * @param dataWorkspaceSelectVo 查询条件
     * @param pageNum 当前页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
	Page<DataWorkspace> listByPageCondition(DataWorkspaceSelectVo dataWorkspaceSelectVo, int pageNum, int pageSize);

    /**
     * 新增工作空间
     * @param dataWorkspace
     * @return
     */
    DataWorkspace insertDataWorkspace(DataWorkspace dataWorkspace);

    /**
     * 创建空的smwu工作空间到服务器指定目录，不注册
     * @param path
     * @throws AuthenticationException
     */
    void createWorkspace(String path) throws AuthenticationException;

    /**
     * 批量插入
     * @param dataWorkspaces
     */
    void batchInsert(List<DataWorkspace> dataWorkspaces);

    /**
     * 删除工作空间
     * @param workspaceId
     */
    void deleteDataWorkspace(String workspaceId) throws AuthenticationException;

    /**
     * 批量删除
     * @param workspaceIds
     */
    void batchDelete(List<String> workspaceIds) throws AuthenticationException;

    /**
     * 更新工作空间
     * @param dataWorkspace
     * @return
     */
    DataWorkspace updateDataWorkspace(DataWorkspace dataWorkspace);

    /**
     * 根据工作空间id和服务类型集合 发布服务
     * @param workspaceIdServiceTypes 工作空间id和服务类型集合
     * @return 发布结果
     */
    List<MessageResult<Object>> publishService(List<WorkspaceIdServiceType> workspaceIdServiceTypes) throws IOException;
}

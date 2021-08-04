/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.mapper;

import com.supermap.gaf.data.mgt.entity.DataWorkspace;
import com.supermap.gaf.data.mgt.vo.DataWorkspaceSelectVo;

import java.util.*;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * 工作空间数据访问类
 *
 * @author zrc
 * @date yyyy-mm-dd
 */
@Mapper
@Component
public interface DataWorkspaceMapper {
    /**
     * 根据主键 workspaceId 查询
     *
     * @param workspaceId 工作空间ID
     * @return 数据工作空间数据对象
     */
    DataWorkspace select(@Param("workspaceId") String workspaceId);

    /**
     * 批量ip查询
     *
     * @param workspaceIds
     * @return
     */
    List<DataWorkspace> selectByIds(List<String> workspaceIds);

    /**
     * 多条件查询
     *
     * @param dataWorkspaceSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
    List<DataWorkspace> selectList(DataWorkspaceSelectVo dataWorkspaceSelectVo);

    /**
     * 新增
     *
     * @param dataWorkspace 数据工作空间对象
     * @return
     */
    int insert(DataWorkspace dataWorkspace);

    /**
     * 批量插入
     *
     * @param dataWorkspaces 工作空间数组对象
     * @return
     */
    int batchInsert(List<DataWorkspace> dataWorkspaces);

    /**
     * 批量删除
     *
     * @param workspaceIds
     * @return
     */
    int batchDelete(List<String> workspaceIds);

    /**
     * 刪除
     *
     * @param workspaceId
     * @return
     */
    int delete(@Param("workspaceId") String workspaceId);

    /**
     * 更新
     *
     * @param dataWorkspace
     * @return
     */
    int update(DataWorkspace dataWorkspace);
}

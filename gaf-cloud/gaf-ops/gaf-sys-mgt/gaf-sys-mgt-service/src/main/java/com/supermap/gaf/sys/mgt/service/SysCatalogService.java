/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.sys.mgt.service;

import com.supermap.gaf.authority.vo.SelectOptionVo;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.commontypes.Page;
import com.supermap.gaf.sys.mgt.commontype.SysCatalog;
import com.supermap.gaf.sys.mgt.enums.CatalogTypeEnum;

import java.util.List;

/**
 * 目录服务类
 *
 * @author wangxiaolong
 * @date:2021/3/25
 */
public interface SysCatalogService {

    /**
     * 根据id查询目录
     *
     * @param catalogId 目录id
     * @return 目录 若未查询到则返回null
     */
    SysCatalog getById(String catalogId);

    /**
     * 新增目录
     *
     * @param sysCatalog 目录
     * @return 目录
     */
    SysCatalog insertSysCatalog(SysCatalog sysCatalog);

    /**
     * 批量插入目录
     *
     * @param sysCatalogs 目录集合
     */
    void batchInsert(List<SysCatalog> sysCatalogs);



    /**
     * 删除目录
     *
     * @param catalogId 目录id
     * @return
     */
    SysCatalog deleteSysCatalog(String catalogId);


    /**
     * 批量删除目录
     *
     * @param catalogIds 目录id集合
     */
    void batchDelete(List<String> catalogIds);


    /**
     * 更新目录
     *
     * @param sysCatalog 目录
     * @return 目录
     */
    SysCatalog updateSysCatalog(SysCatalog sysCatalog);


    /**
     * 根据目录id查询其父目录的路径
     *
     * @param catalogId 目录id
     * @return 父目录的路径
     */
    List<String> getParentPath(String catalogId);

    /**
     * 根据类型获取目录树节点
     *
     * @param type 目录类型 参考CatalogTypeEnum
     * @return 目录树节点 未组织为树结构
     */
    List<TreeNode> getNodesByType(String type);

    /**
     * 组合条件等值查询
     *
     * @param queryCatalog 查询参数
     * @return 目录集合
     */
    List<SysCatalog> getByCombination(SysCatalog queryCatalog);

    /**
     * 通过类型查询有效目录
     * 注意返回的目录包含组件一一对应的名字相同的根目录
     * 或者返回的目录包含租户一一对应的名字相同的根目录
     *
     * @param type 目录类型 参考CatalogTypeEnum
     * @return 若未查询到则返回空集合
     */
    List<SysCatalog> getByType(String type);


    /**
     * 分页查询根目录，条件是单字段模糊匹配和目录类型
     *
     * @param searchFieldName  字段名
     * @param searchFieldValue 字段值  模糊匹配
     * @param orderFieldName   排序字段名
     * @param orderMethod      排序方法
     * @param pageNum          页码
     * @param pageSize         页面大小
     * @param catalogType      目录类型
     * @return 分页条件查询结果
     */
    Page<SysCatalog> listByPageCondition(String searchFieldName, String searchFieldValue, String orderFieldName, String orderMethod, Integer pageNum, Integer pageSize, CatalogTypeEnum catalogType);

    /**
     * 根据目录id 查询目录树 包括该目录
     *
     * @param catalogId 目录id
     * @return 若未查询到则返回空集合
     */
    List<TreeNode> getTreeByCatalogId(String catalogId);

    /**
     * 查询目录的业务类型
     *
     * @return 若未查询到则返回空集合
     */
    List<SelectOptionVo> getBizTypes();

    /**
     * 根据某个结点以它为根结点的所有层次的子结点
     *
     * @param rootCatalogId
     * @return
     */
    List<SysCatalog> getCatalogTreeListByRootId(String rootCatalogId);
}

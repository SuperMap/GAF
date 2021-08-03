/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.sys.mgt.dao;

import com.supermap.gaf.sys.mgt.commontype.SysCatalog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 目录mapper
 *
 * @author wangxiaolong
 * @date:2021/3/25
 */
@Mapper
@Component
public interface SysCatalogMapper {
    /**
     * 根据id查询目录
     *
     * @param catalogId 目录id
     * @return 目录 未查询到则返回null
     */
    com.supermap.gaf.sys.mgt.commontype.SysCatalog select(@Param("catalogId") String catalogId);


    /**
     * 新增目录
     *
     * @param sysCatalog 目录
     * @return 新增的数量
     */
    int insert(com.supermap.gaf.sys.mgt.commontype.SysCatalog sysCatalog);

    /**
     * 批量新增目录
     *
     * @param sysCatalogs 目录集合
     * @return 新增的数量
     */
    int batchInsert(List<com.supermap.gaf.sys.mgt.commontype.SysCatalog> sysCatalogs);

    /**
     * 根据id集合批量删除目录
     *
     * @param catalogIds 目录id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> catalogIds);

    /**
     * 根据目录id删除目录
     *
     * @param catalogId 目录id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("catalogId") String catalogId);

    /**
     * 更新目录
     *
     * @param sysCatalog 目录
     * @return 影响的行数即更新的数量
     */
    int update(com.supermap.gaf.sys.mgt.commontype.SysCatalog sysCatalog);

    /**
     * 单字段等值条件查询 若字段名为null则查询所有数据
     *
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @return 目录集合 若未查询到则返回空集合
     */
    List<com.supermap.gaf.sys.mgt.commontype.SysCatalog> selectByOneField(@Param("fieldName") String fieldName, @Param("fieldValue") String fieldValue);

    /**
     * 单字段等值条件查询 若字段名为null则查询所有数据
     *
     * @param fieldName  字段名 不能为空
     * @param fieldValue 字段值 不能为空
     * @param status     状态 true表示有效 false表示无效
     * @return 目录集合 若未查询到则返回空集合
     */
    List<com.supermap.gaf.sys.mgt.commontype.SysCatalog> selectByOneFieldAndStatus(@Param("fieldName") String fieldName, @Param("fieldValue") String fieldValue, @Param("status") Boolean status);

    /**
     * 获取所有parentId
     *
     * @return 所有parentId
     */
    Set<String> selectParentIds();

    /**
     * 根据parentId 和 状态是否有效 条件查询目录
     *
     * @param parentId 父Id
     * @param status   true 表示有效 false表示无效
     * @return 目录集合 若未查询到则返回空集合
     */
    List<com.supermap.gaf.sys.mgt.commontype.SysCatalog> selectByParentIdAndStatus(@Param("parentId") String parentId, @Param("status") Boolean status);

    /**
     * 根据id集合和状态批量获取目录
     *
     * @param catalogIds 目录id集合
     * @param status     true 表示有效 false表示无效
     * @return 目录集合 若未查询到则返回空集合
     */
    List<com.supermap.gaf.sys.mgt.commontype.SysCatalog> selectByIdsAndStatus(@Param("catalogIds") Set<String> catalogIds, @Param("status") Boolean status);

    /**
     * 根据名字模糊匹配 和 状态条件查询目录
     *
     * @param name   目录名
     * @param status true 表示有效 false表示无效
     * @return 件查询目录集合
     */
    List<com.supermap.gaf.sys.mgt.commontype.SysCatalog> selectByNameAndStatus(@Param("name") String name, @Param("status") Boolean status);

    /**
     * 根据id和状态条件查询
     *
     * @param id     目录id
     * @param status true 表示有效 false表示无效
     * @return 目录
     */
    com.supermap.gaf.sys.mgt.commontype.SysCatalog selectByIdAndStatus(@Param("id") String id, @Param("status") Boolean status);


    /**
     * 根据parentId(父id)或租户id 条件查询有效目录
     *
     * @param parentId 父id
     * @param tenantId 租户id
     * @return 目录集合 若未查询到则返回空集合
     */
    List<com.supermap.gaf.sys.mgt.commontype.SysCatalog> selectByParentIdOrTenantId(@Param("parentId") String parentId, @Param("tenantId") String tenantId);

    /**
     * 根据两个租户id 条件查询有效目录
     *
     * @param tenantIdOne 租户Id
     * @param tenantIdTwo 租户Id
     * @return 目录集合 若未查询到则返回空集合
     */
    List<com.supermap.gaf.sys.mgt.commontype.SysCatalog> selectByTenantId(@Param("tenantIdOne") String tenantIdOne, @Param("tenantIdTwo") String tenantIdTwo);


    /**
     * 根据组合条件 更新范围内的有效子目录 不包含边界
     *
     * @param queryCondition 查询条件 注意queryCondition 至少有一个属性不为null
     * @param addOrSub       序号+1 还是 -1
     * @param startSortSn    开始序号
     * @param endSortSn      结束序号
     */
    void updateSortSnByCombination(@Param("queryCondition") com.supermap.gaf.sys.mgt.commontype.SysCatalog queryCondition, @Param("addOrSub") Boolean addOrSub, @Param("startSortSn") Integer startSortSn, @Param("endSortSn") Integer endSortSn);

    /**
     * 根据父id查询有效子目录数
     *
     * @param parentId 父id
     * @return 目录数
     */
    Integer countByParentId(String parentId);

    /**
     * 统计根据组合条件等值查询结果的数量
     * 注意： 至少有一个属性不为null
     *
     * @param queryCatalog 查询参数
     * @return 查询结果的数量
     */
    Integer countByCombination(com.supermap.gaf.sys.mgt.commontype.SysCatalog queryCatalog);

    /**
     * 根据父id集合 查询有效子目录
     *
     * @param parentIds 父id集合
     * @param status    状态 true 有效 false 无效
     * @return 目录
     */
    List<com.supermap.gaf.sys.mgt.commontype.SysCatalog> selectByParentIdsAndStatus(@Param("parentIds") Set<String> parentIds, @Param("status") Boolean status);

    /**
     * 根据id 批量更新目录状态
     *
     * @param all    目录id集合
     * @param status 状态
     */
    void updateStatusBatch(@Param("all") Set<String> all, @Param("status") Boolean status);

    /**
     * 根据状态、 租户id、 状态、 等值条件查询目录
     *
     * @param tenantId 租户id
     * @param type     目录类型 应为枚举类CatalogTypeEnum值
     * @param status   状态 true 表示有效 false表示无效
     * @return 若未查询到则返回空集合 list.size() = 0
     */
    List<com.supermap.gaf.sys.mgt.commontype.SysCatalog> selectByTenantIdAndTypeAndStatus(@Param("tenantId") String tenantId, @Param("type") String type, @Param("status") boolean status);

    /**
     * 组合条件等值查询
     * 注意： queryCatalog 至少有一个属性不为null
     *
     * @param queryCatalog 查询参数
     * @return 查询结果
     */
    List<com.supermap.gaf.sys.mgt.commontype.SysCatalog> selectByCombination(com.supermap.gaf.sys.mgt.commontype.SysCatalog queryCatalog);

    /**
     * 通过component和type获取目录
     *
     * @param componentId 组件id
     * @param type        目录类型 应为枚举类CatalogTypeEnum值
     * @return 目录集合 若未查询到则返回空集合
     */
    List<com.supermap.gaf.sys.mgt.commontype.SysCatalog> getByComponentAndType(@Param("componentId") String componentId, @Param("type") String type);

    /**
     * 查询根目录，条件是单字段模糊匹配和目录字段等值匹配
     * 模糊匹配的字段不能和等值匹配的字段冲突
     *
     * @param searchFieldName  字段名
     * @param searchFieldValue 字段值  模糊匹配
     * @param orderFieldName   排序字段名
     * @param orderMethod      排序方法
     * @param queryCatalog     目录查询条件
     * @return 分页条件查询结果
     */
    List<com.supermap.gaf.sys.mgt.commontype.SysCatalog> selectByCondition(@Param("searchFieldName") String searchFieldName,
                                                                           @Param("searchFieldValue") String searchFieldValue,
                                                                           @Param("orderFieldName") String orderFieldName,
                                                                           @Param("orderMethod") String orderMethod,
                                                                           @Param("queryCatalog") com.supermap.gaf.sys.mgt.commontype.SysCatalog queryCatalog);


    /**
     * 根据某个结点以它为根结点的所有层次的子结点
     *
     * @param rootId 目录id
     * @return 所有子目录
     */
    List<SysCatalog> getCatalogTreeListByRootId(@Param("rootId") String rootId);
}

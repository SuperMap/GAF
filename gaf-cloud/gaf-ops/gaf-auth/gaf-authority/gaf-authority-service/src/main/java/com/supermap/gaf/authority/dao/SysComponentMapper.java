/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.SysComponent;
import com.supermap.gaf.authority.vo.SysComponentSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * 组件mapper
 * @date:2021/3/25
 * @author zhm
 */
@Mapper
@Component
public interface SysComponentMapper{
    /**
     * 根据id查询组件
     * @param sysComponentId 组件id
     * @return 组件 未查询到则返回null
     */
    SysComponent select(@Param("sysComponentId") String sysComponentId);

    /**
     * 分页查询组件
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 组件集合
     */
    List<SysComponent> pageList(SysComponentSelectVo selectVo);

    /**
     * 分页模糊查询组件
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 组件集合
     */
    List<SysComponent> searchList(SysComponentSelectVo selectVo);

    /**
     * 分页等值查询组件
     * @param selectVo 分页参数 如偏移量，每页条数, 等值查询字段名 字段值 排序字段名 排序方式
     * @return 组件集合
     */
    List<SysComponent> searchWholeMatchList(SysComponentSelectVo selectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 组件集合
     */
    List<SysComponent> bigOffsetPageList(SysComponentSelectVo selectVo);

    /**
     * 查询组件总数
     * @return 数量
     */
    int pageListCount();
    /**
     * 统计单字段模糊匹配条件查询后的结果数量
     * @param fieldName 字段名
     * @param fieldValue 字段值
     * @return 数量
     */
	Integer countOneField(@Param("fieldName") String fieldName, @Param("fieldValue") Object fieldValue);

    /**
     * 新增组件
     * @param sysComponent 组件
     * @return 新增的数量
     */
    int insert(SysComponent sysComponent);
    /**
     * 批量新增组件
     * @param sysComponents  组件集合
     * @return 新增的数量
     */
    int batchInsert(List<SysComponent> sysComponents);
    /**
     * 根据组件id集合删除组件
     * @param sysComponentIds 组件id集合
     * @return 影响的行数即删除的数量
     */
    int batchDelete(List<String> sysComponentIds);
    /**
     * 根据组件id删除组件
     * @param sysComponentId 组件id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("sysComponentId") String sysComponentId);
    /**
     * 更新组件
     * @param sysComponent   组件
     * @return 影响的行数即更新的数量
     */
    int update(SysComponent sysComponent);

    /**
     * 获取所有组件的id和中文名
     * @return 组件集合
     */
    List<SysComponent> getAllIdAndNameCn();

    /**
     * 单字段等值条件查询 若字段名为null则查询所有数据
     * @param fieldName 字段名
     * @param fieldValue 字段值
     * @return 组件集合
     */
    List<SysComponent> selectByOneField(@Param("fieldName") String fieldName, @Param("fieldValue") String fieldValue);

}

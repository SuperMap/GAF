/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthResourceModule;
import com.supermap.gaf.authority.vo.AuthResourceModuleSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * 模块mapper
 * @date:2021/3/25
 * @author zhm
 */
@Mapper
@Component
public interface AuthResourceModuleMapper{
    /**
     * 根据id查询模块
     * @param resourceModuleId 模块id
     * @return 模块 未查询到则返回null
     */
    AuthResourceModule select(@Param("resourceModuleId") String resourceModuleId);

    /**
     * 查询所有
     * @return 模块集合
     */
    List<AuthResourceModule> listModules();

    /**
     * 分页查询模块
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 模块集合
     */
    List<AuthResourceModule> pageList(AuthResourceModuleSelectVo selectVo);

    /**
     * 分页模糊查询接口
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 接口集合
     */
    List<AuthResourceModule> searchList(AuthResourceModuleSelectVo selectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 模块集合
     */
    List<AuthResourceModule> bigOffsetPageList(AuthResourceModuleSelectVo selectVo);

    /**
     * 查询模块总数
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
     * 新增模块
     * @param authResourceModule 模块
     * @return 新增的数量
     */
    int insert(AuthResourceModule authResourceModule);
    /**
     * 批量新增模块
     * @param authResourceModules  模块集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthResourceModule> authResourceModules);
    /**
     * 根据id集合批量删除模块
     * @param resourceModuleIds 模块id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> resourceModuleIds);
    /**
     * 根据模块id删除模块
     * @param resourceModuleId 模块id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("resourceModuleId") String resourceModuleId);
    /**
     * 更新模块
     * @param authResourceModule   模块
     * @return 影响的行数即更新的数量
     */
    int update(AuthResourceModule authResourceModule);

    /**
     * 统计条件查询的结果数量
     * @param queryModule 查询条件 注意： 该对象的属性值至少有一个不能为null
     * @return 若未查询到则返回0
     */
    Integer countByCombination(AuthResourceModule queryModule);
}

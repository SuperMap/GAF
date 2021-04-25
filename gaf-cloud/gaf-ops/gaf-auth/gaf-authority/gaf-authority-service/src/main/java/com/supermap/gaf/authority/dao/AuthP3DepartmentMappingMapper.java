/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthP3DepartmentMapping;
import com.supermap.gaf.authority.vo.AuthP3DepartmentMappingSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 第三方部门映射mapper
 * @date:2021/3/25
 * @author yangdong
 */
@Mapper
@Component
public interface AuthP3DepartmentMappingMapper {
    /**
     * 新增第三方部门映射
     * @param authP3DepartmentMapping 第三方部门映射
     * @return 新增的数量
     */
    int insert(AuthP3DepartmentMapping authP3DepartmentMapping);

    /**
     * 根据第三方部门映射id删除第三方部门映射
     * @param departmentMappingId 第三方部门映射id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("departmentMappingId") String departmentMappingId);

    /**
     * 更新第三方部门映射
     * @param authP3DepartmentMapping   第三方部门映射
     * @return 影响的行数即更新的数量
     */
    int update(AuthP3DepartmentMapping authP3DepartmentMapping);

    /**
     * 根据id查询第三方部门映射
     * @param departmentMappingId 第三方部门映射id
     * @return 第三方部门映射 未查询到则返回null
     */
    AuthP3DepartmentMapping select(@Param("departmentMappingId") String departmentMappingId);

    /**
     * 根据第三方组件id和部门id查询第三方部门映射
     * @param p3ComponentId 第三方组件id
     * @param departmentId 部门id
     * @return 第三方部门映射
     */
    AuthP3DepartmentMapping getByDepartmentId(@Param("p3ComponentId") String p3ComponentId, @Param("departmentId") String departmentId);

    /**
     * 分页查询第三方部门映射
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 第三方部门映射集合
     */
    List<AuthP3DepartmentMapping> pageList(AuthP3DepartmentMappingSelectVo selectVo);


    /**
     * 分页模糊查询第三方部门映射
     * @param selectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 第三方部门映射集合
     */
    List<AuthP3DepartmentMapping> searchList(AuthP3DepartmentMappingSelectVo selectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 第三方部门映射集合
     */
    List<AuthP3DepartmentMapping> bigOffsetPageList(AuthP3DepartmentMappingSelectVo selectVo);

    /**
     * 查询第三方部门映射总数
     * @return 数量
     */
    int pageListCount();

    /**
     * 批量新增第三方部门映射
     * @param authP3DepartmentMappings  第三方部门映射集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthP3DepartmentMapping> authP3DepartmentMappings);

    /**
     * 根据id集合批量删除第三方部门映射
     * @param departmentMappingIds 第三方部门映射id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> departmentMappingIds);

}

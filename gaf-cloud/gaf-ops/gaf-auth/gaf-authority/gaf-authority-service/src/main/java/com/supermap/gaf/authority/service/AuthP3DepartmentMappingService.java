/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthP3DepartmentMapping;
import com.supermap.gaf.authority.vo.AuthP3DepartmentMappingSelectVo;

import java.util.List;
import java.util.Map;

/**
 * 第三方部门映射服务类
 *
 * @author yangdong
 * @date:2021/3/25
 */
public interface AuthP3DepartmentMappingService {
    /**
     * 新增第三方部门映射
     *
     * @param authP3DepartmentMapping 第三方部门映射
     * @return 第三方部门映射
     */
    AuthP3DepartmentMapping insertAuthP3DepartmentMapping(AuthP3DepartmentMapping authP3DepartmentMapping);


    /**
     * 根据id查询第三方部门映射
     *
     * @param departmentMappingId 第三方部门映射id
     * @return 第三方部门映射
     */
    AuthP3DepartmentMapping selectById(String departmentMappingId);

    /**
     * 根据第三方组件id、部门id获取第三方部门映射
     *
     * @param p3ComponentId 第三方组件id
     * @param departmentId  部门id
     * @return 第三方部门映射
     */
    AuthP3DepartmentMapping getByDepartmentId(String p3ComponentId, String departmentId);

    /**
     * 分页查询第三方部门映射
     *
     * @param authP3DepartmentMappingSelectVo 分页参数 如偏移量，每页条数
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> pageList(AuthP3DepartmentMappingSelectVo authP3DepartmentMappingSelectVo);

    /**
     * 分页模糊查询第三方部门映射
     *
     * @param authP3DepartmentMappingSelectVo 分页参数 如偏移量，每页条数, 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 分页结果 如查询到的总数 分页后的结果
     */
    Map<String, Object> searchList(AuthP3DepartmentMappingSelectVo authP3DepartmentMappingSelectVo);

    /**
     * 批量插入
     *
     * @param authP3DepartmentMappings 第三方部门映射集合
     */
    void batchInsert(List<AuthP3DepartmentMapping> authP3DepartmentMappings);

    /**
     * 批量删除
     *
     * @param departmentMappingIds 第三方部门映射id集合
     */
    void batchDelete(List<String> departmentMappingIds);

}

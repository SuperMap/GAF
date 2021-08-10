/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthDepartment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author zhm
 * @date:2021/3/25
 */
@Mapper
@Component
public interface AuthDepartmentQueryMapper {
    /**
     * [查询] 根据主键 departmentId 查询
     *
     * @author zhm
     **/
    AuthDepartment select(@Param("departmentId") String departmentId);
}

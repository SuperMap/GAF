/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthDepartment;
import com.supermap.gaf.authority.constant.CacheGroupConstant;
import com.supermap.gaf.authority.dao.AuthDepartmentQueryMapper;
import com.supermap.gaf.authority.service.AuthDepartmentQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author zhm
 * @date:2021/3/25
 * 部门服务类
 */
@Service
public class AuthDepartmentQueryServiceImpl implements AuthDepartmentQueryService {
    @Autowired
    private AuthDepartmentQueryMapper authDepartmentQueryMapper;

    /**
     * id查询部门
     *
     * @return
     */
    @Cacheable(value = CacheGroupConstant.DEPARTMENT, key = "#departmentId")
    @Override
    public AuthDepartment getById(String departmentId) {
        if (departmentId == null) {
            throw new IllegalArgumentException("departmentId不能为空");
        }
        return authDepartmentQueryMapper.select(departmentId);
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthDepartment;

/**
 * @author zhm
 * @date:2021/3/25
 * 部门服务类
 */
public interface AuthDepartmentQueryService {

    /**
     * id查询部门
     *
     * @return
     */
    AuthDepartment getById(String departmentId);


}

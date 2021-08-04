/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthRoleApi;
import com.supermap.gaf.authority.vo.AuthRoleApiSelectVo;

import java.util.List;
import java.util.Map;

/**
 * @author dqc
 * @date:2021/3/25 角色授权接口服务类
 */
public interface AuthRoleApiQueryService {


    /**
     * roleId查询角色授权接口
     *
     * @return
     */
    List<AuthRoleApi> listByRole(String roleId);


}

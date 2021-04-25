/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthP3TenantMapping;
import com.supermap.gaf.authority.vo.AuthP3TenantMappingSelectVo;

import java.util.List;
import java.util.Map;

/**
 * @author yangdong
 * @date:2021/3/25
 * 第三方租户映射表服务类
 */
public interface AuthP3TenantMappingQueryService {

    /**
     * tenantId查询第三方租户映射表
     *
     * @return
     */
    AuthP3TenantMapping getByTenantId(String p3ComponentId, String tenantId);


}

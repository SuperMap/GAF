/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthTenant;
import com.supermap.gaf.authority.commontype.AuthUser;
import com.supermap.gaf.authority.vo.AuthTenantSelectVo;
import com.supermap.gaf.authority.vo.TenantInitVo;

import java.util.List;
import java.util.Map;

/**
 * @author dqc
 * @date:2021/3/25
 * 租户服务类
 */
public interface AuthTenantQueryService {


	/**
    * id查询租户
    * @return
    */
    AuthTenant getById(String tenantId);

}

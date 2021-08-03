/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthP3TenantMapping;
import com.supermap.gaf.authority.vo.AuthP3TenantMappingSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yangdong
 * @date:2021/3/25
 */
@Mapper
@Component
public interface AuthP3TenantMappingQueryMapper {

    /**
     * [查询] 根据 tenantId 查询
     *
     * @author yangdong
     **/
    AuthP3TenantMapping getByTenantId(@Param("p3ComponentId") String p3ComponentId, @Param("tenantId") String tenantId);


}

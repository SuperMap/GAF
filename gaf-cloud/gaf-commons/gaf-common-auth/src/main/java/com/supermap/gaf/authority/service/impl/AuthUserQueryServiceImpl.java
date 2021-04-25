/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthUser;
import com.supermap.gaf.authority.constant.CacheGroupConstant;
import com.supermap.gaf.authority.dao.AuthUserQueryMapper;
import com.supermap.gaf.authority.service.AuthUserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


/**
 * @author dqc
 * @date:2021/3/25
 * 服务类
 */
@Service
public class AuthUserQueryServiceImpl implements AuthUserQueryService {
    @Autowired
    private AuthUserQueryMapper authUserQueryMapper;


    /**
     * id查询
     * @return
     */
    @Cacheable(value = CacheGroupConstant.USER, key = "#userId")
    @Override
    public AuthUser getById(String userId) {
        return authUserQueryMapper.select(userId);
    }

    @Override
    public AuthUser getByUserName(String username) {
        return authUserQueryMapper.getByUserName(username);
    }


}

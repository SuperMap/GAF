/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthUserParttime;
import com.supermap.gaf.authority.dao.AuthUserParttimeQueryMapper;
import com.supermap.gaf.authority.service.AuthUserParttimeQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yangdong
 * @date:2021/3/25
 * 用户挂职服务类
 */
@Service
public class AuthUserParttimeQueryServiceImpl implements AuthUserParttimeQueryService {
    @Autowired
    private AuthUserParttimeQueryMapper authUserParttimeQueryMapper;

    /**
     * id查询用户挂职
     *
     * @return
     */
    @Override
    public List<AuthUserParttime> listByUserId(String userId) {
        return authUserParttimeQueryMapper.listByUserId(userId);
    }



}

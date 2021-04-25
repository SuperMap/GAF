/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthP3UserMapping;
import com.supermap.gaf.authority.dao.AuthP3UserMappingQueryMapper;
import com.supermap.gaf.authority.service.AuthP3UserMappingQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangdong
 * @date:2021/3/25
 * 第三方用户映射表服务类
 */
@Service
public class AuthP3UserMappingQueryServiceImpl implements AuthP3UserMappingQueryService {
    @Autowired
    private AuthP3UserMappingQueryMapper authP3UserMappingQueryMapper;


    /**
     * id查询第三方用户映射表
     *
     * @return
     */
    @Override
    public AuthP3UserMapping getByUserId(String p3ComponentId, String userId) {
        if (userId == null || p3ComponentId == null) {
            throw new IllegalArgumentException("userId、p3ComponentId不能为空");
        }
        return authP3UserMappingQueryMapper.getByUserId(p3ComponentId, userId);
    }

}

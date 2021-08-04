/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;


import com.supermap.gaf.authority.commontype.*;
import com.supermap.gaf.authority.dao.AuthResourceApiQueryMapper;
import com.supermap.gaf.authority.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author dqc
 * @date:2021/3/25
 */
@Service
public class AuthResourceApiQueryServiceImpl implements AuthResourceApiQueryService {

    @Autowired
    private AuthResourceApiQueryMapper authResourceApiQueryMapper;


    /**
     * id查询api资源
     *
     * @return
     */
    @Override
    public AuthResourceApi getById(String resourceApiId) {
        return authResourceApiQueryMapper.select(resourceApiId);
    }


}

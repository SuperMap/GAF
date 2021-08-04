/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthModuleApi;
import com.supermap.gaf.authority.dao.AuthModuleApiQueryMapper;
import com.supermap.gaf.authority.service.AuthModuleApiQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;


/**
 * @author dqc
 * @date:2021/3/25 模块API关联服务类
 */
@Service
public class AuthModuleApiQueryServiceImpl implements AuthModuleApiQueryService {
    @Autowired
    private AuthModuleApiQueryMapper authModuleApiQueryMapper;


    /**
     * 根据模块id查询与之关联的API
     *
     * @return
     */
    @Override
    public List<AuthModuleApi> getByModuleId(String moduleId, Boolean status) {
        if (StringUtils.isEmpty(moduleId)) {
            return new ArrayList<>();
        }
        return authModuleApiQueryMapper.getByModuleId(moduleId, status);
    }


}

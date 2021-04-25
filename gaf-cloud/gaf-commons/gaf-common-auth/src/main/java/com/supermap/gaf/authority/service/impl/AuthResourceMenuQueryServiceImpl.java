/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthResourceMenu;
import com.supermap.gaf.authority.dao.AuthResourceMenuQueryMapper;
import com.supermap.gaf.authority.service.AuthResourceMenuQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wxl
 * @date:2021/3/25
 * 菜单服务类
 */
@Service
public class AuthResourceMenuQueryServiceImpl implements AuthResourceMenuQueryService {
    @Autowired
    private AuthResourceMenuQueryMapper authResourceMenuMapper;

    /**
     * id查询菜单
     *
     * @return
     */
    @Override
    public AuthResourceMenu getById(String resourceMenuId) {
        return authResourceMenuMapper.select(resourceMenuId);
    }
}

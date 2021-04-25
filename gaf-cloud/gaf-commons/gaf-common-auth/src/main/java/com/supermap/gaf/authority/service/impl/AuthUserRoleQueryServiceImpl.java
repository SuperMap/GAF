/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthUserRole;
import com.supermap.gaf.authority.dao.AuthUserRoleQueryMapper;
import com.supermap.gaf.authority.service.AuthUserRoleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author yangdong
 * @date:2021/3/25
 * 用户角色服务类
 */
@Service
public class AuthUserRoleQueryServiceImpl implements AuthUserRoleQueryService {
    @Autowired
    private AuthUserRoleQueryMapper authUserRoleQueryMapper;





    @Override
    public List<AuthUserRole> listByUser(String userId) {
        return authUserRoleQueryMapper.listByUser(userId);
    }

}

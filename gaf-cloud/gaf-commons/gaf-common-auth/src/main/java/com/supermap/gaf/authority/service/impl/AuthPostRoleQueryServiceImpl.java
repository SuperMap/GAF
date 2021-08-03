/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.AuthPostRole;
import com.supermap.gaf.authority.dao.AuthPostRoleQueryMapper;
import com.supermap.gaf.authority.service.AuthPostRoleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author yangdong
 * @date:2021/3/25 岗位角色服务类
 */
@Service
public class AuthPostRoleQueryServiceImpl implements AuthPostRoleQueryService {

    @Autowired
    private AuthPostRoleQueryMapper authPostRoleQueryMapper;

    /**
     * 根据岗位查询岗位角色
     *
     * @return
     */
    @Override
    public List<AuthPostRole> listByPost(String postId) {
        if (StringUtils.isEmpty(postId)) {
            return new ArrayList<>();
        }
        return authPostRoleQueryMapper.listByPost(postId);
    }


}

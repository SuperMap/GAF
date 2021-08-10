/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthUserRole;
import com.supermap.gaf.authority.vo.AuthUserRoleSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dqc
 * @date:2021/3/25
 */
@Component
public interface AuthUserRoleQueryMapper {

    /**
     * [查询] 用户id
     *
     * @author dqc
     **/
    List<AuthUserRole> listByUser(@Param("userId") String userId);


}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthResourceMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author wxl
 * @date:2021/3/25
 */
@Mapper
@Component
public interface AuthResourceMenuQueryMapper {
    /**
     * [查询] 根据主键 resourceMenuId 查询
     *
     * @author wxl
     **/
    AuthResourceMenu select(@Param("resourceMenuId") String resourceMenuId);

    /**
     * 根据菜单id集合查询菜单
     * @param resourceMenuIds 菜单id集合
     * @return 菜单
     */
    List<AuthResourceMenu> selectByIds(@Param("menuIdList") Collection<String> resourceMenuIds);
}

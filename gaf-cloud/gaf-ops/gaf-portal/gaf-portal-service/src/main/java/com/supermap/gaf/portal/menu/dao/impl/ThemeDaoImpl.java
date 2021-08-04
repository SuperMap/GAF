/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.portal.menu.dao.impl;

import com.supermap.gaf.portal.menu.commontypes.ThemeInfo;
import com.supermap.gaf.portal.menu.dao.ThemeDao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
@Service
public class ThemeDaoImpl implements ThemeDao {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public int addTheme(ThemeInfo theme) {
        return sqlSession.insert("addTheme", theme);
    }

    @Override
    public int updateTheme(ThemeInfo theme) {
        return sqlSession.update("updateTheme", theme);
    }

    @Override
    public ThemeInfo queryTheme(@Param("user") String user) {
        return sqlSession.selectOne("queryTheme", user);
    }

}

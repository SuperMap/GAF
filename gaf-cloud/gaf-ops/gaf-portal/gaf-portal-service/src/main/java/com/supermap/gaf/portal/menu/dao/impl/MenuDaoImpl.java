/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.portal.menu.dao.impl;

import com.supermap.gaf.portal.menu.commontypes.MenuInfo;
import com.supermap.gaf.portal.menu.dao.MenuDao;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
@Service
public class MenuDaoImpl implements MenuDao {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public int addMenu(MenuInfo menu) {
        return sqlSession.insert("addMenu", menu);
    }

    @Override
    public int deleteMenuById(@Param("id") String menuId) {
        return sqlSession.delete("deleteMenuById", menuId);
    }

    @Override
    public int deleteMenuByFlag(@Param("flag") String flag) {
        return sqlSession.delete("deleteMenuByFlag", flag);
    }

    @Override
    public int deleteMenuByIds(List<String> menuIds) {
        return sqlSession.delete("deleteMenuByIds", menuIds);
    }

    @Override
    public int updateMenu(MenuInfo menu) {
        return sqlSession.update("updateMenu", menu);
    }

    @Override
    public MenuInfo queryMenuById(@Param("id") String menuId) {
        return sqlSession.selectOne("queryMenuById", menuId);
    }

    @Override
    public List<MenuInfo> queryMenus() {
        return sqlSession.selectList("queryMenus");
    }
}

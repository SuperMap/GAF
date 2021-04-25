/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.portal.menu.dao.impl;

import com.supermap.gaf.portal.menu.commontypes.CustomationInfo;
import com.supermap.gaf.portal.menu.dao.CustomationDao;
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
public class CustomationDaoImpl implements CustomationDao {
    @Autowired
    private SqlSession sqlSession;

    @Override
    public int addCustomation(CustomationInfo customation) {
        return sqlSession.insert("addCustomation", customation);
    }

    @Override
    public int updateCustomation(CustomationInfo customation) {
        return sqlSession.update("updateCustomation", customation);
    }

    @Override
    public int updateConfigInfo(CustomationInfo customationInfo) {
        return sqlSession.update("updateConfigInfo", customationInfo);
    }

    @Override
    public int updateConfigInfo2Default(CustomationInfo customationInfo) {
        return sqlSession.update("updateConfigInfo2Default", customationInfo);
    }

    @Override
    public int updateDefault2ConfigInfo(CustomationInfo customationInfo) {
        return sqlSession.update("updateDefault2ConfigInfo", customationInfo.getTenantId());
    }

    @Override
    public CustomationInfo queryCustomation(@Param("tenantId")String tenantId) {
        return sqlSession.selectOne("queryCustomation", tenantId);
    }

}

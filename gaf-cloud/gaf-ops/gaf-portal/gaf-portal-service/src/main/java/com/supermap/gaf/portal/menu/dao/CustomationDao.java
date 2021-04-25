/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.portal.menu.dao;

import com.supermap.gaf.portal.menu.commontypes.CustomationInfo;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
public interface CustomationDao {
    /**
     * 添加门户信息
     * @param customation 门户信息对象
     * @return int
     */
    int addCustomation(CustomationInfo customation);

    /**
     * 修改门户信息
     * @param customation 门户信息对象
     * @return int
     */
    int updateCustomation(CustomationInfo customation);

    /**
     * 保存首页设置
     * @param customationInfo 门户信息对象
     * @return int
     */
    int updateConfigInfo(CustomationInfo customationInfo);

    /**
     * 同步首页设置到默认
     * @param customationInfo 门户信息对象
     * @return int
     */
    int updateConfigInfo2Default(CustomationInfo customationInfo);

    /**
     * 从默认恢复首页设置
     * @param customationInfo 门户信息对象
     * @return int
     */
    int updateDefault2ConfigInfo(CustomationInfo customationInfo);

    /**
     * 查询租户门户订制配置
     * @param tenantId 租户id
     * @return CustomationInf对象
     */
    CustomationInfo queryCustomation(String tenantId);
}

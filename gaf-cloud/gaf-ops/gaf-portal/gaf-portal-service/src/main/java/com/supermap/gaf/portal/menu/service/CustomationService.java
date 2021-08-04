/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.portal.menu.service;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.portal.menu.commontypes.CustomationInfo;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
public interface CustomationService {
    /**
     * 新增|更新主题配置
     *
     * @param customation 门户信息对象
     * @return
     */
    String saveCustomation(CustomationInfo customation); //新增|更新主题配置

    /**
     * 修改租户门户订制配置
     *
     * @param customationInfo 门户信息对象
     * @return
     */
    MessageResult<Boolean> updateCustomation(CustomationInfo customationInfo);

    /**
     * 门户订制查询
     *
     * @param tenantId 租户id
     * @return
     */
    String queryCustomation(String tenantId);

    /**
     * 保存定制结果
     *
     * @param customationInfo
     * @return
     */
    MessageResult<Boolean> updateConfigInfo(CustomationInfo customationInfo);//保存定制结果

    /**
     * 同步定制结果到默认
     *
     * @param customationInfo
     * @return
     */
    MessageResult<Boolean> updateConfigInfo2Default(CustomationInfo customationInfo);//同步定制结果到默认

    /**
     * 恢复默认
     *
     * @param customationInfo
     * @return
     */
    MessageResult<Boolean> updateDefault2ConfigInfo(CustomationInfo customationInfo);//恢复默认
}

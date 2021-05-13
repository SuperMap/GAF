/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.service;

import com.supermap.gaf.sys.mgt.commontype.SysResourceDatasource;

/**
 * 数据源服务类
 * @author wangxiaolong
 * @date:2021/3/25
 * 
 */
public interface SysResourceDatasourceQueryService {

    /**
     * 根据id查询数据源
     * @param datasourceId 数据源id
     * @return  数据源 若未查询到则返回null
     */
    SysResourceDatasource getById(String datasourceId);

}

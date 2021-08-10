/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.support;

import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;

/**
 *
 * 数据源连接转换
 *
 * @author wxl
 * @since 2021/7/22
 */
public interface ConnectionInfoConverter {
    /**
     * 将sysResourceDatasource数据源的连接信息转为jdbc所需的连接信息
     * @param sysResourceDatasource 数据源的连接信息
     * @return jdbc所需的连接信息
     */
    JdbcConnectionInfo convert2JdbcConnectionInfo(SysResourceDatasource sysResourceDatasource);
}

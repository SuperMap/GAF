/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.dao;

import com.supermap.gaf.sys.mgt.commontype.SysResourceDatasource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 数据源mapper
 * @date:2021/3/25
 * @author wangxiaolong
 */
@Mapper
@Component
public interface SysResourceDatasourceQueryMapper {
    /**
     * 根据数据源id查询数据源
     * @param datasourceId 数据源id
     * @return 数据源 未查询到则返回null
     */
    SysResourceDatasource select(@Param("datasourceId") String datasourceId);

}

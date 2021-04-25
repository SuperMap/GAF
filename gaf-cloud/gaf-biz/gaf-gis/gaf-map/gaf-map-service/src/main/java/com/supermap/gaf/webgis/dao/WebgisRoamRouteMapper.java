/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.dao;

import com.supermap.gaf.webgis.entity.WebgisRoamRoute;
import com.supermap.gaf.webgis.vo.WebgisRoamRouteSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 漫游路线数据访问类
 * @author wangxiaolong 
 * @date yyyy-mm-dd
 */
@Mapper
@Component
public interface WebgisRoamRouteMapper{
	/**
     * 根据主键 gisRoamRouteId 查询
     *
	 */
    WebgisRoamRoute select(@Param("gisRoamRouteId")String gisRoamRouteId);
	
	/**
     * 多条件查询
     * @param webgisRoamRouteSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
	List<WebgisRoamRoute> selectList(WebgisRoamRouteSelectVo webgisRoamRouteSelectVo);

    /**
     * 新增
     *
	 */
    int insert(WebgisRoamRoute webgisRoamRoute);
	
	/**
     * 批量插入
     * 
	 */
    int batchInsert(List<WebgisRoamRoute> webgisRoamRoutes);
	
	/**
     * 批量删除
     * 
	 */
    int batchDelete(List<String> gisRoamRouteIds);

	/**
     * 刪除
     *
	 */
    int delete(@Param("gisRoamRouteId")String gisRoamRouteId);

    /**
    * 更新
    * 
    **/
    int update(WebgisRoamRoute webgisRoamRoute);
}

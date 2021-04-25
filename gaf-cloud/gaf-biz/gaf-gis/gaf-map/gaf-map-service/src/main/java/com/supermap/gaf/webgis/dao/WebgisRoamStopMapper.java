/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.dao;

import com.supermap.gaf.webgis.entity.WebgisRoamStop;
import com.supermap.gaf.webgis.vo.WebgisRoamStopSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 漫游站点数据访问类
 * @author wangxiaolong 
 * @date yyyy-mm-dd
 */
@Mapper
@Component
public interface WebgisRoamStopMapper{
	/**
     * 根据主键 gisRoamStopId 查询
     *
	 */
    WebgisRoamStop select(@Param("gisRoamStopId")String gisRoamStopId);
	
	/**
     * 多条件查询
     * @param webgisRoamStopSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
	List<WebgisRoamStop> selectList(WebgisRoamStopSelectVo webgisRoamStopSelectVo);

    /**
     * 新增
     *
	 */
    int insert(WebgisRoamStop webgisRoamStop);
	
	/**
     * 批量插入
     * 
	 */
    int batchInsert(List<WebgisRoamStop> webgisRoamStops);
	
	/**
     * 批量删除
     * 
	 */
    int batchDelete(List<String> gisRoamStopIds);

	/**
     * 刪除
     *
	 */
    int deleteById(@Param("gisRoamStopId")String gisRoamStopId);

    /**
    * 更新
    * 
    **/
    int update(WebgisRoamStop webgisRoamStop);

	/**
	 * 根据等值查询条件删除站点
	 * @param query
	 */
	int delete(WebgisRoamStop query);
}

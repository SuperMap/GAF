/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.dao;
import com.supermap.gaf.webgis.entity.WebgisDataServiceField;
import com.supermap.gaf.webgis.vo.WebgisDataServiceFieldSelectVo;
import java.util.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * GIS数据服务字段数据访问类
 * @author wangxiaolong 
 * @date yyyy-mm-dd
 */
@Mapper
@Component
public interface WebgisDataServiceFieldMapper{
	/**
     * 根据主键 gisServiceFieldId 查询
     *
	 */
    WebgisDataServiceField select(@Param("gisServiceFieldId")String gisServiceFieldId);
	
	/**
     * 单字段条件模糊查询
     * @param webgisDataServiceFieldSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
	List<WebgisDataServiceField> selectByOneField(WebgisDataServiceFieldSelectVo webgisDataServiceFieldSelectVo);

    /**
     * 新增
     *
	 */
    int insert(WebgisDataServiceField webgisDataServiceField);
	
	/**
     * 批量插入
     * 
	 */
    int batchInsert(List<WebgisDataServiceField> webgisDataServiceFields);
	
	/**
     * 批量删除
     * 
	 */
    int batchDelete(List<String> gisServiceFieldIds);

	/**
     * 刪除
     *
	 */
    int delete(@Param("gisServiceFieldId")String gisServiceFieldId);

    /**
    * 更新
    * 
    **/
    int update(WebgisDataServiceField webgisDataServiceField);

	/**
	 * 等值条件查询
	 * @param query 查询条件，至少有一个属性不为null
	 * @return 若未查询到则返回空集合
	 */
	List<WebgisDataServiceField> selectByCombination(WebgisDataServiceField query);
}

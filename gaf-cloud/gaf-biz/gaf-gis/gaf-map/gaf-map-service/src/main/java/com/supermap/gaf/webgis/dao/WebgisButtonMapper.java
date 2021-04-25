/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.dao;
import com.supermap.gaf.webgis.entity.WebgisButton;
import com.supermap.gaf.webgis.vo.WebgisButtonSelectVo;
import java.util.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * 地图按钮数据访问类
 * @author zhurongcheng 
 * @date 2020-12-05
 */
@Mapper
@Component
public interface WebgisButtonMapper{
	/**
     * 根据主键 buttonId 查询
     *
	 */
    WebgisButton select(@Param("buttonId")String buttonId);
	
	/**
     * 单字段条件模糊查询
     * @param webgisButtonSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
	List<WebgisButton> selectList(WebgisButtonSelectVo webgisButtonSelectVo);

    /**
     * 新增
     *
	 */
    int insert(WebgisButton webgisButton);
	
	/**
     * 批量插入
     * 
	 */
    int batchInsert(List<WebgisButton> webgisButtons);
	
	/**
     * 批量删除
     * 
	 */
    int batchDelete(List<String> buttonIds);

	/**
     * 刪除
     *
	 */
    int delete(@Param("buttonId")String buttonId);

    /**
    * 更新
    * 
    **/
    int update(WebgisButton webgisButton);
}

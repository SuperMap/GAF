/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.service;
import com.supermap.gaf.webgis.entity.WebgisButton;
import com.supermap.gaf.webgis.vo.WebgisButtonSelectVo;
import com.supermap.gaf.webgis.util.Page;
import java.util.*;

/**
 * 地图按钮服务类
 * @author zhurongcheng 
 * @date 2020-12-05
 */
public interface WebgisButtonService {
	
	/**
    * id查询地图按钮
    * @return
    */
    WebgisButton getById(String buttonId);
	
	/**
     * 分页条件查询
     * @param webgisButtonSelectVo 查询条件
     * @param pageNum 当前页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
	Page<WebgisButton> listByPageCondition(WebgisButtonSelectVo webgisButtonSelectVo, int pageNum, int pageSize);

    /**
     * 新增地图按钮
     * @return 
     */
    WebgisButton insertWebgisButton(WebgisButton webgisButton);
	
	/**
     * 批量插入
     *
	 */
    void batchInsert(List<WebgisButton> webgisButtons);

    /**
     * 删除地图按钮
     * 
     */
    void deleteWebgisButton(String buttonId);

    /**
     * 批量删除
     * 
	 */
    void batchDelete(List<String> buttonIds);

    /**
     * 更新地图按钮
     * @return 
     */
    WebgisButton updateWebgisButton(WebgisButton webgisButton);

    void checkAvailable(WebgisButton button, boolean isUpdate);
}

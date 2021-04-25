/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.service;
import com.supermap.gaf.webgis.domain.WebgisServiceDo;
import com.supermap.gaf.webgis.entity.WebgisService;
import com.supermap.gaf.webgis.entity.WebgisServiceAssociation;
import com.supermap.gaf.webgis.vo.WebgisServiceAssociationSelectVo;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisServiceSelectVo;

import java.util.*;

/**
 * GIS服务关联服务类
 * @author wangxiaolong 
 * @date yyyy-mm-dd
 */
public interface WebgisServiceAssociationService {
	
	/**
    * id查询GIS服务关联
    * @return
    */
    WebgisServiceAssociation getById(String gisServiceAssocId);
	
	/**
     * 分页条件查询
     * @param webgisServiceAssociationSelectVo 查询条件
     * @param pageNum 当前页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
	Page<WebgisServiceAssociation> listByPageCondition(WebgisServiceAssociationSelectVo webgisServiceAssociationSelectVo, int pageNum, int pageSize);
	
	
    /**
     * 新增GIS服务关联
     * @return 
     */
    WebgisServiceAssociation insertWebgisServiceAssociation(WebgisServiceAssociation webgisServiceAssociation);
	
	/**
     * 批量插入
     *
	 */
    void batchInsert(List<WebgisServiceAssociation> webgisServiceAssociations);

    /**
     * 删除GIS服务关联
     * 
     */
    void deleteWebgisServiceAssociation(String gisServiceAssocId);

    /**
     * 批量删除
     * 
	 */
    void batchDelete(List<String> gisServiceAssocIds);

    /**
     * 更新GIS服务关联
     * @return 
     */
    WebgisServiceAssociation updateWebgisServiceAssociation(WebgisServiceAssociation webgisServiceAssociation);

    /**
     * 分页查询webgis服务关联的服务
     * @param webgisServiceId webgis服务id
     * @param webgisServiceSelectVo 查询条件
     * @param pageNum 页面
     * @param pageSize 页面大小
     * @return 分页对象
     */
    Page<WebgisServiceDo> listAssociationServices(String webgisServiceId, WebgisServiceSelectVo webgisServiceSelectVo, Integer pageNum, Integer pageSize);

    /**
     * 分页查询webgis服务不关联的服务
     * @param webgisServiceId webgis服务id
     * @param webgisServiceSelectVo 查询条件
     * @param pageNum 页面
     * @param pageSize 页面大小
     * @return 分页对象
     */
    Page<WebgisServiceDo> listUnrelatedServices(String webgisServiceId, WebgisServiceSelectVo webgisServiceSelectVo, Integer pageNum, Integer pageSize);
}

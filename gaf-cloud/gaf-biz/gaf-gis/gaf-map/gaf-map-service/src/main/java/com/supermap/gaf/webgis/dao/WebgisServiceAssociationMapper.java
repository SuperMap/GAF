/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.dao;
import com.supermap.gaf.webgis.domain.WebgisServiceDo;
import com.supermap.gaf.webgis.entity.WebgisService;
import com.supermap.gaf.webgis.entity.WebgisServiceAssociation;
import com.supermap.gaf.webgis.vo.WebgisServiceAssociationSelectVo;
import java.util.*;

import com.supermap.gaf.webgis.vo.WebgisServiceSelectVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * GIS服务关联数据访问类
 * @author wangxiaolong 
 * @date yyyy-mm-dd
 */
@Mapper
@Component
public interface WebgisServiceAssociationMapper{
	/**
     * 根据主键 gisServiceAssocId 查询
     *
	 */
    WebgisServiceAssociation select(@Param("gisServiceAssocId")String gisServiceAssocId);
	
	/**
     * 单字段条件模糊查询
     * @param webgisServiceAssociationSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
	List<WebgisServiceAssociation> selectByOneField(WebgisServiceAssociationSelectVo webgisServiceAssociationSelectVo);

    /**
     * 新增
     *
	 */
    int insert(WebgisServiceAssociation webgisServiceAssociation);
	
	/**
     * 批量插入
     * 
	 */
    int batchInsert(List<WebgisServiceAssociation> webgisServiceAssociations);
	
	/**
     * 批量删除
     * 
	 */
    int batchDelete(List<String> gisServiceAssocIds);

	/**
     * 刪除
     *
	 */
    int delete(@Param("gisServiceAssocId")String gisServiceAssocId);

    /**
    * 更新
    * 
    **/
    int update(WebgisServiceAssociation webgisServiceAssociation);

	/**
	 * 条件查询webgis服务关联的服务
	 * @param webgisServiceId webgis服务id
	 * @param webgisServiceSelectVo 查询条件
	 * @return
	 */
	List<WebgisServiceDo> selectAssociationServices(@Param("webgisServiceId") String webgisServiceId, @Param("vo") WebgisServiceSelectVo webgisServiceSelectVo);

	/**
	 * 条件查询webgis服务不关联的服务
	 * @param webgisServiceId webgis服务id
	 * @param webgisServiceSelectVo 查询条件
	 * @return
	 */
	List<WebgisServiceDo> selectUnrelatedServices(@Param("webgisServiceId") String webgisServiceId, @Param("vo") WebgisServiceSelectVo webgisServiceSelectVo);
}

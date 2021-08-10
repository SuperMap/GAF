/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.dao;

import com.supermap.gaf.webgis.entity.WebgisService;
import com.supermap.gaf.webgis.vo.WebgisServiceConditonVo;
import com.supermap.gaf.webgis.vo.WebgisServiceSelectVo;

import java.util.*;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * GIS服务数据访问类
 *
 * @author wangxiaolong
 * @date 2020-12-05
 */
@Mapper
@Component
public interface WebgisServiceMapper {
    /**
     * 根据主键 gisServiceId 查询
     */
    WebgisService select(@Param("gisServiceId") String gisServiceId);

    /**
     * 单字段模糊匹配且根据类型查询
     * 若查询条件为null则忽略此条件
     * 注意：单字段条件不能为类型
     *
     * @param webgisServiceSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
    List<WebgisService> selectList(WebgisServiceSelectVo webgisServiceSelectVo);

    /**
     * 新增
     */
    int insert(WebgisService webgisService);

    /**
     * 批量插入
     */
    int batchInsert(List<WebgisService> webgisServices);

    /**
     * 批量删除
     */
    int batchDelete(List<String> gisServiceIds);

    /**
     * 刪除
     */
    int delete(@Param("gisServiceId") String gisServiceId);

    /**
     * 更新
     **/
    int update(WebgisService webgisService);

    /**
     * 根据id集合批量查询webgis服务
     *
     * @param ids id集合
     * @return 若未查询到则返回空对象
     */
    List<WebgisService> selectByIds(@Param("ids") List<String> ids);


    /**
     * 单字段模糊匹配且根据类型 ，且不在集合里的查询
     * 若查询条件为null则忽略此条件
     * 注意：单字段条件不能为类型
     *
     * @param webgisServiceConditonVo 查询条件
     * @param serviceIdSet            webgisServiceId集合
     * @return 若未查询到则返回空集合
     */
    List<WebgisService> selectNotInSet(@Param("vo") WebgisServiceConditonVo webgisServiceConditonVo, @Param("serviceIdSet") Set<String> serviceIdSet);

    /**
     * 查询多种类型下的所有服务
     *
     * @param typeCodes
     * @return
     */
    List<WebgisService> selectByTypeCodes(@Param("typeCodes") List<String> typeCodes);
}

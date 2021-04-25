/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.api.scanner.dao;

import com.supermap.gaf.api.scanner.entity.SysCatalog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @date:2021/3/25
 * @author wangxiaolong
 */
@Mapper
@Component
public interface SwaggerSysCatalogMapper {

    /**
     * [新增]
     * @author wangxiaolong
     **/
    int insert(SysCatalog sysCatalog);

    /**
     * 根据id和状态条件查询
     * @param id 目录id
     * @param status true 表示有效 false表示无效
     * @return
     */
    SysCatalog selectByIdAndStatus(@Param("id") String id, @Param("status") Boolean status);

    /**
     * 组合条件等值查询
     * 注意： queryCatalog 至少有一个属性不为null
     * @param queryCatalog 查询参数
     * @return 查询结果
     */
    List<SysCatalog> selectByCombination(SysCatalog queryCatalog);

    /**
     * 通过component和type获取目录
     * @param componentId
     * @param type
     * @return
     */
    List<SysCatalog> getByComponentAndType(@Param("componentId") String componentId, @Param("type") String type);


}

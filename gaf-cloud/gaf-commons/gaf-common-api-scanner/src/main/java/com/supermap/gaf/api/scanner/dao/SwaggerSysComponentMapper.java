/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.api.scanner.dao;

import com.supermap.gaf.api.scanner.entity.SysComponent;
import com.supermap.gaf.api.scanner.entity.SysComponentSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhm
 * @date:2021/3/25
 */
@Mapper
@Component
public interface SwaggerSysComponentMapper {

    /**
     * [查询] 组件名称全词匹配查询
     *
     * @author zhm
     **/
    List<SysComponent> searchWholeMatchList(SysComponentSelectVo SelectVo);

    /**
     * [新增]
     *
     * @author zhm
     **/
    int insert(SysComponent sysComponent);

    /**
     * 单字段等值条件查询 若字段名为null则查询所有数据
     *
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @return
     */
    List<SysComponent> selectByOneField(@Param("fieldName") String fieldName, @Param("fieldValue") String fieldValue);

}

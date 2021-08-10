/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.api.scanner.service;


import com.supermap.gaf.api.scanner.entity.SysComponent;

import java.util.List;

/**
 * @author zhm
 * @date:2021/3/25 组件服务类
 */
public interface SwaggerSysComponentService {


    /**
     * 新增组件
     *
     * @return
     */
    SysComponent insertSysComponent(SysComponent sysComponent);


    /**
     * 单字段等值条件查询 若字段名为null则查询所有数据
     *
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @return
     */
    List<SysComponent> selectByOneField(String fieldName, String fieldValue);

}

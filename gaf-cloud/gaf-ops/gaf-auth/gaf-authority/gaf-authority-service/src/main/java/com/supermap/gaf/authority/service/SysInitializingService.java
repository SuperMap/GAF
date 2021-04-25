/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service;


import com.supermap.gaf.authority.commontype.SysComponent;
import io.swagger.models.Swagger;

/**
 * 系统初始化服务类
 * @author dqc
 * @date:2021/3/25
 *
 */
public interface SysInitializingService {
    /**
     * 初始化系统模块表数据
     * @return 组件
     */
    SysComponent initializeSysComponent();

    /**
     * 初始化系统目录表数据
     * @param sysComponent 组件
     * @param swagger swagger
     */
    void initializeSysCatalog(SysComponent sysComponent, Swagger swagger);

    /**
     * 初始化系统API表数据
     * @param sysComponent 组件
     * @param swagger swagger
     */
    void initializeSysResourceApi(SysComponent sysComponent, Swagger swagger);
}

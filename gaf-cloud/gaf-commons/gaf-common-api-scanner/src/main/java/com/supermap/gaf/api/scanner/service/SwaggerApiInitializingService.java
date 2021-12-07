/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.api.scanner.service;

import io.swagger.models.Swagger;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2021/2/1 9:28 AM
 */
public interface SwaggerApiInitializingService {


    /**
     * 初始化系统目录表数据
     */
    void initializeSysCatalog(Swagger swagger);

    /**
     * 初始化系统API表数据
     */
    void initializeSysResourceApi(Swagger swagger);
}

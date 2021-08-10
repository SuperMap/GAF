/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthModuleApi;

import java.util.List;

/**
 * @author dqc
 * @date:2021/3/25 模块API关联服务类
 */
public interface AuthModuleApiQueryService {


    /**
     * 根据模块id查询与之关联的API
     *
     * @return
     */
    List<AuthModuleApi> getByModuleId(String moduleId, Boolean status);


}

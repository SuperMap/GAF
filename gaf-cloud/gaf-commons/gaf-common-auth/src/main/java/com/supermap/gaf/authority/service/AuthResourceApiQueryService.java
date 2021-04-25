/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service;

import com.supermap.gaf.authority.commontype.AuthResourceApi;

import java.util.List;

/**
 * @date:2021/3/25
 * @author dqc
 */
public interface AuthResourceApiQueryService {
	



    /**
     * id查询api资源
     * @return
     */
    AuthResourceApi getById(String resourceApiId);




}

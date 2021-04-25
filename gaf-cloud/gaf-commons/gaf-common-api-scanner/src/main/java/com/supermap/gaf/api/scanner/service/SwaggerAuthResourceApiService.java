/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.api.scanner.service;


import com.supermap.gaf.api.scanner.entity.AuthResourceApi;

import java.util.List;

/**
 * @author zhm
 * @date:2021/3/25
 * api资源服务类
 */
public interface SwaggerAuthResourceApiService {

	
    /**
    * 新增api资源
    * @return
    */
    AuthResourceApi insertAuthResourceApi(AuthResourceApi authResourceApi);

    /**
     * 通过catalog列出api
     * @param catalogId
     * @return
     */
    List<AuthResourceApi> listByCatalogId(String catalogId);
    

}

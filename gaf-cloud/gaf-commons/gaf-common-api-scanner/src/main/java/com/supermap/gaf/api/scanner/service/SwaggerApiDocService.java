/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.api.scanner.service;


import com.supermap.gaf.api.scanner.entity.ApiDoc;

/**
 * @author dqc
 * @date:2021/3/25
 * api文档表
 */
public interface SwaggerApiDocService {



    /**
     * 通过name属性更新文档
     * @param apiDoc
     * @return
     */
    boolean syncApiDoc(ApiDoc apiDoc);

    /**
     * 通过name获取ApiDoc
     * @param name
     * @return
     */
    ApiDoc getApiDoc(String name);

}

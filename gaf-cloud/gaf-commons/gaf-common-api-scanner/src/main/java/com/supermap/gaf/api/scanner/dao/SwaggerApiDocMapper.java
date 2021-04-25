/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.api.scanner.dao;

import com.supermap.gaf.api.scanner.entity.ApiDoc;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


/**
 * @date:2021/3/25
 * @author dqc
 */
@Mapper
@Component
public interface SwaggerApiDocMapper {

    /**
     * 新增apidoc
     * @param apiDoc
     * @return
     */
    ApiDoc addApiDoc(ApiDoc apiDoc);

    /**
     * 通过name属性更新apidoc
     * @param apiDoc
     * @return
     */
    void updateApiDoc(ApiDoc apiDoc);

    /**
     * 通过name获取ApiDoc
     * @param name
     * @return
     */
    ApiDoc getApiDoc(String name);

}

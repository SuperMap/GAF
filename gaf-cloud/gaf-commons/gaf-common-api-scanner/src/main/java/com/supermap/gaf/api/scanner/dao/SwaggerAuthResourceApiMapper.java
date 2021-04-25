/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.api.scanner.dao;

import com.supermap.gaf.api.scanner.entity.AuthResourceApi;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @date:2021/3/25
 * @author zhm
 */
@Mapper
@Component
public interface SwaggerAuthResourceApiMapper {
    /**
     * [新增]
     * @author zhm
     **/
    int insert(AuthResourceApi authResourceApi);

    /**
     * 通过catalog列出api
     * @param catalogId
     * @return
     */
    List<AuthResourceApi> listByCatalogId(String catalogId);


}

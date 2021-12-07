/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.api.scanner.service.impl;

import com.supermap.gaf.api.scanner.dao.SwaggerApiDocMapper;
import com.supermap.gaf.api.scanner.entity.ApiDoc;
import com.supermap.gaf.api.scanner.service.SwaggerApiDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2021/2/1 9:33 AM
 */
@Service
public class SwaggerApiDocServiceImpl implements SwaggerApiDocService {
    @Autowired
    private SwaggerApiDocMapper swaggerApiDocMapper;

    @Override
    public boolean syncApiDoc(ApiDoc apiDoc) {
        //查询是否有对应name的apidoc，如果有则更新，没有则新增
        if (apiDoc != null && !StringUtils.isEmpty(apiDoc.getName())) {
            ApiDoc existApiDoc = swaggerApiDocMapper.getApiDoc(apiDoc.getName());
            if (null != existApiDoc) {
                swaggerApiDocMapper.updateApiDoc(apiDoc);
            } else {
                apiDoc.setId(UUID.randomUUID().toString());
                swaggerApiDocMapper.addApiDoc(apiDoc);
            }
            return true;
        }
        return false;
    }

    @Override
    public ApiDoc getApiDoc(String name) {
        return swaggerApiDocMapper.getApiDoc(name);
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.api.scanner.service.impl;

import com.supermap.gaf.api.scanner.dao.SwaggerAuthResourceApiMapper;
import com.supermap.gaf.api.scanner.entity.AuthResourceApi;
import com.supermap.gaf.api.scanner.service.SwaggerAuthResourceApiService;
import com.supermap.gaf.data.access.service.BatchSortAndCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author zhm
 * @date:2021/3/25 api资源服务类
 */
@Service
public class SwaggerAuthResourceApiServiceImpl implements SwaggerAuthResourceApiService {
    @Autowired
    private SwaggerAuthResourceApiMapper swaggerAuthResourceApiMapper;
    @Autowired
    private BatchSortAndCodeService batchSortAndCodeService;


    /**
     * 新增api资源
     *
     * @return
     */
    @Override
    @Transactional
    public AuthResourceApi insertAuthResourceApi(AuthResourceApi authResourceApi) {
        authResourceApi.setResourceApiId(UUID.randomUUID().toString());
        swaggerAuthResourceApiMapper.insert(authResourceApi);
        batchSortAndCodeService.revisionSortSnForInsertOrDelete(AuthResourceApi.class, Arrays.asList(authResourceApi.getApiCatalogId()));
        return authResourceApi;
    }

    @Override
    public List<AuthResourceApi> listByCatalogId(String catalogId) {
        return swaggerAuthResourceApiMapper.listByCatalogId(catalogId);
    }


}

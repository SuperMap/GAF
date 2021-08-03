/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.cache;


import com.supermap.gaf.webgis.domain.BatchRegistryServiceResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * The type Cache registry result cache.
 *
 * @author heykb
 * @date:2021/3/25
 */
@Component
@Primary
public class CacheRegistryResultCache extends AbstractRegistryResultCache implements InitializingBean {

    public static final String REGISTRY_RESULT_CACHE_PREFIX = "Gaf-Map:REGISTRY_RESULT:";

    @Autowired
    private CacheManager cacheManager;

    private Cache cache;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.cache = cacheManager.getCache("Gaf-Map");
    }

    @Override
    public BatchRegistryServiceResult get(String code) {
        return cache.get(REGISTRY_RESULT_CACHE_PREFIX + code, BatchRegistryServiceResult.class);
    }

    @Override
    public void put(String code, BatchRegistryServiceResult data) {
        cache.put(REGISTRY_RESULT_CACHE_PREFIX + code, data);
    }


}

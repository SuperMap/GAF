/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.cache;


import com.supermap.gaf.webgis.domain.BatchRegistryServiceResult;

/**
 * @author heykb
 * @date:2021/3/25
 */
public interface RegistryResultCacheI {
    String generateKey();

    BatchRegistryServiceResult get(String code);

    void put(String code, BatchRegistryServiceResult data);

    void success(String code);

    void fail(String code);

    void exist(String code);

    void done(String code);

    void error(String code);
}

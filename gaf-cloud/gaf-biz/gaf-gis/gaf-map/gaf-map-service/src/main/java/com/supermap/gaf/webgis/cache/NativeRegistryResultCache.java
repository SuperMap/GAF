/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.cache;


import com.supermap.gaf.webgis.domain.BatchRegistryServiceResult;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * @author heykb
 * @date:2021/3/25
 */
//@Component
public class NativeRegistryResultCache extends AbstractRegistryResultCache {

    private static final LinkedHashMap<String, BatchRegistryServiceResult> RESULT_MAP = new LinkedHashMap<>(100, 0.75f, false);

    @Override
    public BatchRegistryServiceResult get(String code) {
        return RESULT_MAP.get(code);
    }

    @Override
    public void put(String code, BatchRegistryServiceResult data) {
        RESULT_MAP.put(code, data);
    }
}

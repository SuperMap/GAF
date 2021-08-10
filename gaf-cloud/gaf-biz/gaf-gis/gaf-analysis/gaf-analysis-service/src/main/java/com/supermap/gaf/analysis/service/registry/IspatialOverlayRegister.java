/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.analysis.service.registry;

import com.supermap.gaf.analysis.spi.IspatialOverlay;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @author dqc
 * @date:2021/3/25
 * @since : 2020-12-29
 */
@Component
public class IspatialOverlayRegister implements InitializingBean {
    @Value("${spiOverlayPath:}")
    private String spiOverlayPath;

    /**
     * 所有空间分析服务缓存
     */
    private Map<String, IspatialOverlay> spatialOverlayProviders = new HashMap<>();

    /**
     * 获取空间分析服务
     */
    public IspatialOverlay getSpatialOverlayProvider(String type) {
        return spatialOverlayProviders.get(type);
    }

    /**
     * 初始化加载SPI接口实现类
     */
    @Override
    public void afterPropertiesSet() {
        File path = new File(spiOverlayPath);
        File[] files = path.listFiles((dir, name) -> name.endsWith(".jar"));
        if (files == null || files.length == 0) {
            return;
        }
        List<URL> urlList = new ArrayList<>();
        try {
            for (File file : files) {
                urlList.add(file.toURI().toURL());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URL[] urls = new URL[urlList.size()];
        urlList.toArray(urls);
        URLClassLoader ucl = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
        ServiceLoader<IspatialOverlay> spatialOverlays = ServiceLoader.load(IspatialOverlay.class, ucl);
        for (IspatialOverlay spatialOverlay : spatialOverlays) {
            this.spatialOverlayProviders.put(spatialOverlay.type(), spatialOverlay);
        }
    }

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.common.storage.handler;

import com.supermap.gaf.common.storage.entity.MinioConfig;
import com.supermap.gaf.common.storage.entity.VolumePathReturn;
import com.supermap.gaf.common.storage.web.SelectModeI;
import org.apache.commons.lang3.NotImplementedException;

/**
 * @date:2021/3/25
 * @author heykb
 */
public interface MinioConfigHandlerI {
    MinioConfig getConfig(String name, SelectModeI selectMode);
    String encodeKeyName(MinioConfig minioConfig, String keyName);
    String decodeKeyName(MinioConfig minioConfig,String encodedKeyName);
    /**
     *
     * @param isWin
     * @return
     */
    default String getVolumeConfigIni(boolean isWin){
        throw new NotImplementedException("getVolumeConfigIni()");
    }

    VolumePathReturn getVolumePath(String name, String path, boolean returnUrl, SelectModeI selectMode);
}

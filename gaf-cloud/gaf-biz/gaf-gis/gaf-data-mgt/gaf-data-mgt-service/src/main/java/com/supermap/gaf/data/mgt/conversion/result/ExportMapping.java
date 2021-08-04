/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.conversion.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author wxl
 * @since 2021/8/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportMapping {
    String sourceDataName;
    String databaseServerAddr;
    String datasourceAlias;

    String targetFilePath;
    String downloadSignUrl;
    String targetFileType;
    String targetFileCharset;
    boolean overwrite;
}

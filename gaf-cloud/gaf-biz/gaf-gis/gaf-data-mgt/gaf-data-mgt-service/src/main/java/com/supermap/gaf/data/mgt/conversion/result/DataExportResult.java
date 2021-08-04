/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.conversion.result;

import lombok.Data;

import java.util.List;

/**
 * 数据导出结果
 * @author wxl
 * @since 2021/8/4
 */
@Data
public class DataExportResult {
    List<ExportMapping> successed;
    List<ExportMapping> failed;
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.conversion.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 *
 * @author wxl
 * @since 2021/8/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("导入映射.即源文件到目标数据集集合的映射")
public class ImportMapping {
    @ApiModelProperty("源文件路径.该路径是gaf文件存储系统中的路径")
    String sourceFilePath;
    @ApiModelProperty("目标数据源的别名")
    String targetDatasourceAlias;
    @ApiModelProperty("目标数据集名集合")
    List<String> datasetNames;
    @ApiModelProperty("目标地图名集合")
    List<String> mapNames;
}

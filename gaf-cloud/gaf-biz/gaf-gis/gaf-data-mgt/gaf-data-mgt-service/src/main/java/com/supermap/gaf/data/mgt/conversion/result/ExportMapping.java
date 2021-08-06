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

/**
 *
 *
 * @author wxl
 * @since 2021/8/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("导出映射.即源数据源下的源数据集到目标导出文件的映射")
public class ExportMapping {
    @ApiModelProperty("源数据集名")
    String sourceDataName;
    @ApiModelProperty("数据库型数据源的数据库地址,格式是ip:端口_数据库名,")
    String databaseServerAddr;
    @ApiModelProperty("数据源别名")
    String datasourceAlias;
    @ApiModelProperty("目标文件路径.该路径为gaf文件存储系统里的路径")
    String targetFilePath;
    @ApiModelProperty("导出的目标文件下载url,该url有时间限制")
    String downloadSignUrl;
    @ApiModelProperty("目标文件类型")
    String targetFileType;
    @ApiModelProperty("目标文件字符集")
    String targetFileCharset;
    @ApiModelProperty("返回导出目录中存在同名文件时，是否强制覆盖")
    boolean overwrite;
}

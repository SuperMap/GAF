/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author:yd
 * @date:2021/3/25
 * @Date 2021-3-03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("构建s3m参数")
public class BuildS3MParameter implements Serializable {
    @ApiModelProperty(value = "数据源信息", example = "{}")
    private DataSourceInfo dataSourceInfo;

    @ApiModelProperty(value = "数据集名称列表",example = "[test1,test2]")
    private List<String> datasetNames;

    @ApiModelProperty(value = "是否覆写",example = "false")
    private boolean isOverWrite;

    @ApiModelProperty(value = "缓存存放路径",example = "/xx/xx")
    private String outputFolder;
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.vo;

import com.supermap.gaf.webgis.entity.WebgisDataServiceField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @author:yj
 * @date:2021/3/25
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("数据服务数据集字段及已选择的字段名视图层对象")
public class DataServiceFieldsVo {
    /**
     * 数据集字段列表
     */
    @ApiModelProperty("数据集字段列表")
    List<WebgisDataServiceField> fields;
    /**
     * 已选择的字段名列表
     */
    @ApiModelProperty("已选择的字段名列表")
    List<String>  fieldNames;
}

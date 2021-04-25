/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.api.scanner.entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 组件 分页条件查询实体
 * @date:2021/3/25
* @author zhm
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("组件 分页条件查询实体")
public class SysComponentSelectVo {
	@ApiModelProperty("查询字段名")
    private String searchFieldName;
	@ApiModelProperty("查询字段值")
    private String searchFieldValue;
	@ApiModelProperty("排序字段名")
    private String orderFieldName;
	@ApiModelProperty("排序方法")
    private String orderMethod;
    @ApiModelProperty("偏移量")
    private Integer offset;
    @ApiModelProperty("页大小")
    private Integer pageSize;
}

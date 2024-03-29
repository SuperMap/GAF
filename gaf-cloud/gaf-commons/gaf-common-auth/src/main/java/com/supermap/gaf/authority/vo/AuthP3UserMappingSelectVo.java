/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.vo;

import com.supermap.gaf.authority.commontype.AuthP3UserMapping;
import com.supermap.gaf.validator.StringRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 第三方用户映射 分页条件查询实体
 *
 * @author yangdong
 * @date:2021/3/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("第三方用户映射 分页条件查询实体")
public class AuthP3UserMappingSelectVo {
    @ApiModelProperty("查询字段名")
    @StringRange(entityClass = AuthP3UserMapping.class, message = "不在指定的字段名范围内")
    private String searchFieldName;
    @ApiModelProperty("查询字段值")
    private String searchFieldValue;
    @ApiModelProperty("排序字段名")
    @StringRange(entityClass = AuthP3UserMapping.class, message = "不在指定的字段名范围内")
    private String orderFieldName;
    @ApiModelProperty("排序方法")
    @StringRange(value = {"asc", "desc"}, message = "不在指定的范围[asc,desc]内")
    private String orderMethod;
    @ApiModelProperty("偏移量")
    private Integer offset;
    @ApiModelProperty("页大小")
    private Integer pageSize;
}

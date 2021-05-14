/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.commontype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther : yd
 * @author:yj
 * @date:2021/3/25
 * @since : 2020-12-02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("pull同步第三方资源结果")
public class P3SyncResult {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("名称")
    private String name;
}

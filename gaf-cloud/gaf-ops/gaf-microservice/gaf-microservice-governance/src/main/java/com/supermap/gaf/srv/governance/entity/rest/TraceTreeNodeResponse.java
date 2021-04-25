/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.srv.governance.entity.rest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/12/4 8:54 AM
 */
@Data
@NoArgsConstructor
@ApiModel("链路追踪查询对象")
public class TraceTreeNodeResponse implements Serializable {
    private static final long serialVersionUID = -8190485641011222108L;

    @ApiModelProperty("链路追踪查询记录列表")
    private List<TraceTreeNode> data;
    @ApiModelProperty("链路追踪记录查询总数")
    private Long totalHits;
}

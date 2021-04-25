/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * @author:yj
 * @date:2021/3/25
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("树和复选框选中的节点key 视图层对象")
public class TreeVo implements Serializable {
    /**
     * 树
     */
    @ApiModelProperty("树根节点集合")
    List<TreeNode> rootTreeNodes;

    /**
     * 树节点 复选框选中的keys
     */
    @ApiModelProperty("复选框选中的节点key")
    List<String> checkedKeys;
}

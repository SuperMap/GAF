/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.supermap.gaf.commontypes.tree.ITreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 字典数据树节点
 * @author wangxiaolong
 * @date 2021-01-08
 */
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("字典数据树节点")
@EqualsAndHashCode(callSuper=true)
public class DictDataNode extends DictData implements Serializable , ITreeNode<String, DictDataNode> {

    @ApiModelProperty("父级id")
    private String pid;
    @ApiModelProperty("子字典")
    private List<DictDataNode> children;
    @JsonProperty("isLeaf")
    @ApiModelProperty("是否是叶子节点")
    private boolean leaf;

    @ApiModelProperty("层级")
    private Integer level;

    @Override
    public String getKey() {
        return super.getKey();
    }

    @Override
    public void setKey(String key) {
        super.setKey(key);
    }

    @ApiModelProperty("父级id")
    @Override
    public String getParentId() {
        return pid;
    }

    @Override
    public void setParentId(String parentId) {
        this.pid = parentId;
    }

    @Override
    public List<DictDataNode> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<DictDataNode> children) {
        this.children = children;
    }

    @Override
    public boolean isLeaf() {
        return leaf;
    }

    @Override
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}


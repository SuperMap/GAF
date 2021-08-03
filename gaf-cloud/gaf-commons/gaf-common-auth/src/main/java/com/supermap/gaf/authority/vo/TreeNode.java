/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supermap.gaf.authority.enums.NodeTypeEnum;
import com.supermap.gaf.commontypes.tree.ITreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author:yj
 * @date:2021/3/25 树节点
 */
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("树节点")
public class TreeNode implements Serializable, ITreeNode<String, TreeNode> {
    @ApiModelProperty("树节点key,唯一")
    private String key;
    @ApiModelProperty("树节点名")
    private String title;
    @JsonIgnore
    @ApiModelProperty("是否为叶子节点")
    private boolean leaf;
    @ApiModelProperty("子节点")
    private List<TreeNode> children;
    @ApiModelProperty("节点类型")
    private Integer type = NodeTypeEnum.TENANT.getValue();
    /**
     * 树节点在兄弟节点之间的 顺序从1开始 如1 2 3 ...
     */
    @ApiModelProperty(value = "节点在同一父节点下的兄弟节点的排序", allowableValues = "注意排序从1开始，例如1 2 3 ... 最大值最好小于等于同一父节点下所有子节点数量。当插该节点相关联的数据，它的父节点下的所有子节点数据排序必须为1 2 3 4 ...")
    private Integer sortSn;
    /**
     * 树节点的父Id
     */
    @ApiModelProperty(value = "节点父id ", allowableValues = "注意原始节点的若为根节点，则它的parentId(父id)必须为\"0\"")
    private String parentId;


    @ApiModelProperty("节点类型")
    private ScopedSlot scopedSlots = new ScopedSlot();//


    @Override
    public List<TreeNode> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<TreeNode> children) {
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

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSortSn() {
        return sortSn;
    }

    public void setSortSn(Integer sortSn) {
        this.sortSn = sortSn;
    }

    public ScopedSlot getScopedSlots() {
        return scopedSlots;
    }

    public void setScopedSlots(ScopedSlot scopedSlots) {
        this.scopedSlots = scopedSlots;
    }
}

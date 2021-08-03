/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.commontypes.tree;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 默认树节点
 *
 * @author:yj
 * @date:2021/3/25
 */
@ApiModel("默认树节点")
public class DefaultTreeNode implements ITreeNode<String, DefaultTreeNode> {
    /**
     * 节点的唯一标识，即id
     */
    @ApiModelProperty("点的唯一标识，即id")
    protected String key;
    /**
     * 节点名
     */
    @ApiModelProperty("节点名")
    protected String title;
    /**
     * 子节点
     */
    @ApiModelProperty("子节点")
    protected List<DefaultTreeNode> children;
    /**
     * 父节点id
     */
    @ApiModelProperty("父节点id")
    protected String parentId;
    /**
     * 是否是叶子节点
     */
    @ApiModelProperty("是否是叶子节点")
    protected boolean leaf;
    /**
     * 同级排序
     */
    @ApiModelProperty("同级排序")
    protected Integer sortSn;
    /**
     * 用户自定义数据
     */
    @ApiModelProperty("用户自定义数据")
    protected Object userObject;

    public DefaultTreeNode() {
    }

    public DefaultTreeNode(String key, String title, List<DefaultTreeNode> children, String parentId, boolean leaf, Integer sortSn, Object userObject) {
        this.key = key;
        this.title = title;
        this.children = children;
        this.parentId = parentId;
        this.leaf = leaf;
        this.sortSn = sortSn;
        this.userObject = userObject;
    }

    @Override
    public List<DefaultTreeNode> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<DefaultTreeNode> children) {
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

    public Integer getSortSn() {
        return sortSn;
    }

    public void setSortSn(Integer sortSn) {
        this.sortSn = sortSn;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }
}

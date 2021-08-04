/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.commontypes.tree;


import java.util.List;

/**
 * 树节点接口
 *
 * @param <N> 树节点类型
 * @param <K> 树节点唯一标识类型
 * @author wangxiaolong
 * @date:2021/3/25
 */
public interface ITreeNode<K, N extends ITreeNode> extends INode<K> {

    /**
     * 获取子节点
     *
     * @return
     */
    List<N> getChildren();

    /**
     * 设置子节点
     *
     * @return
     */
    void setChildren(List<N> children);

    /**
     * 是否是叶子节点
     *
     * @return
     */
    boolean isLeaf();

    /**
     * 设置叶子节点
     */
    void setLeaf(boolean leaf);
}

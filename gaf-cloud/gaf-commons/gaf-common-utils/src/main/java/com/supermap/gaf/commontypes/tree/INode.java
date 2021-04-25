package com.supermap.gaf.commontypes.tree;

/**
 *
 * 使用id和parentId存储树形数据的接口
 * @param <K> 表示节点唯一标识的数据类型
 * @author wangxiaolong
 */
public interface INode<K> {

    /**
     * 获取节点的唯一标识，即id
     * @return
     */
    K getKey();

    /**
     * 设置节点的唯一标识
     * @return
     */
    void setKey(K key);

    /**
     * 获取父节点的唯一标识，父id
     * @return
     */
    K getParentId();

    /**
     * 设置父节点的唯一标识，父id
     * @return
     */
    void setParentId(K parentId);

}

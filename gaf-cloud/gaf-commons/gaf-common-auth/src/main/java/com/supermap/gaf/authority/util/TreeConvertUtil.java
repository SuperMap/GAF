/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.util;

import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.utils.TreeUtil;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 将树节点原始数据转换为树
 * @date:2021/3/25
 * @author wxl
 */
public class TreeConvertUtil {

    /**
     * 表示所有目录构成的目录树的根的parentId
     */
    public static final String ROOT_PARENT_ID = "0";

    /**
     * 将原始树节点转换为树 注意原始节点的若为根节点，则它的parentId(父id)必须为"0"
     * @param nodeList  原始树节点集合
     * @return 树节点集合
     */
    public static List<TreeNode> convertToTree(List<TreeNode> nodeList) {
        return convertToTree(nodeList, Comparator.comparingInt(TreeNode::getSortSn));
    }

    /**
     * 将原始树节点转换为树 注意原始节点的若为根节点，则它的parentId(父id)必须为"0"
     * @param nodeList  原始树节点集合
     * @param comparator  自定义排序比较器
     * @return 树节点集合
     */
    public static List<TreeNode> convertToTree(List<TreeNode> nodeList, Comparator<? super TreeNode> comparator ) {
        if (nodeList == null || nodeList.size() <= 0) {
            return new LinkedList<>();
        }
        TreeNode rootParent = new TreeNode();
        rootParent.setKey(ROOT_PARENT_ID);
        return TreeUtil.getChildren(rootParent, nodeList, comparator);
    }

}

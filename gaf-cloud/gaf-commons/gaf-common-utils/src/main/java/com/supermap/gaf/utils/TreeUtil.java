/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.utils;

import com.supermap.gaf.commontypes.tree.ITreeNode;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 将树通用工具类
 *
 * @author wangxiaolong
 * @date:2021/3/25
 */
public class TreeUtil {

    /**
     * 将非树形结构的树节点组织为树形结构
     * 注意：树形结构的叶子节点的children为null
     *
     * @param parentNode 父节点
     * @param all        所有树节点
     * @param comparator 比较器
     * @param <K>        树节点唯一标识类型
     * @param <N>        树节点类型
     * @return 若所有树节点为空或者未找到parentNode的子节点，则返回null
     */
    public static <K, N extends ITreeNode<K, N>> List<N> getChildren(N parentNode, List<N> all, Comparator<? super N> comparator) {
        List<N> collect = all.stream().filter(treeNode -> treeNode.getParentId().equals(parentNode.getKey()))
                .map(treeNode -> {
                    treeNode.setChildren(getChildren(treeNode, all, comparator));
                    return treeNode;
                })
                .sorted(comparator)
                .collect(Collectors.toList());
        parentNode.setLeaf(collect.size() <= 0);
        return collect.size() <= 0 ? null : collect;
    }

    /**
     * 从树中获取某节点
     *
     * @param treeList  树
     * @param predicate 获取某节点的判断条件
     * @param <K>       树节点唯一标识类型
     * @param <N>       树节点类型
     * @return 若为查询到节点则返回null
     */
    public static <K, N extends ITreeNode<K, N>> N getTreeNode(List<N> treeList, Predicate<N> predicate) {
        if (treeList != null && treeList.size() > 0) {
            for (N node : treeList) {
                if (predicate.test(node)) {
                    return node;
                }
                if (node.getChildren() != null && node.getChildren().size() > 0) {
                    N hit = getTreeNode(node.getChildren(), predicate);
                    if (hit != null) {
                        return hit;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 浏览树节点后的结果或者浏览树后的结果
     */
    public enum VisitResult {
        /**
         * 控制对树每个节点进行遍历时，继续遍历
         */
        CONTINUE,
        /**
         * 遍历树时，终结遍历
         */
        TERMINATE,
        /**
         * 遍历树时，跳过当前节点的所有子节点
         */
        SKIP_SUBTREE,
    }

    /**
     * 深度优先遍历树
     *
     * @param treeList 树
     * @param func     回调函数 传入参数：当前节点、层级(从level开始) 返回：浏览当前节点的结果枚举
     * @param level    初始层级
     * @param <K>      树节点唯一标识类型
     * @param <N>      树节点类型
     * @return 遍历树的结果枚举
     */
    public static <K, N extends ITreeNode<K, N>> VisitResult deepFirstTraverseTree(List<N> treeList, BiFunction<N, Integer, VisitResult> func, int level) {
        if (treeList != null && treeList.size() > 0) {
            int currentLevel = level + 1;
            for (N node : treeList) {
                VisitResult signal = func.apply(node, currentLevel);
                if (signal == VisitResult.SKIP_SUBTREE) {
                    continue;
                } else if (signal == VisitResult.TERMINATE) {
                    return VisitResult.TERMINATE;
                }
                if (node.getChildren() != null && node.getChildren().size() > 0) {
                    VisitResult visitResult = deepFirstTraverseTree(node.getChildren(), func, currentLevel);
                    if (visitResult == VisitResult.TERMINATE) {
                        return VisitResult.TERMINATE;
                    }
                }
            }
        }
        return VisitResult.CONTINUE;
    }

    /**
     * 深度优先遍历树
     *
     * @param treeList 树
     * @param func     回调函数 传入参数：当前节点、当前节点的迭代器 返回：浏览当前节点的结果枚举
     * @param <K>      树节点唯一标识类型
     * @param <N>      树节点类型
     * @return 遍历树的结果枚举
     */
    public static <K, N extends ITreeNode<K, N>> VisitResult deepFirstTraverseTree(List<N> treeList, BiFunction<N, Iterator<N>, VisitResult> func) {
        if (treeList != null && treeList.size() > 0) {
            Iterator<N> iterator = treeList.iterator();
            while (iterator.hasNext()) {
                N node = iterator.next();
                VisitResult signal = func.apply(node, iterator);
                if (signal == VisitResult.SKIP_SUBTREE) {
                    continue;
                } else if (signal == VisitResult.TERMINATE) {
                    return VisitResult.TERMINATE;
                }
                if (node.getChildren() != null && node.getChildren().size() > 0) {
                    VisitResult visitResult = deepFirstTraverseTree(node.getChildren(), func);
                    if (visitResult == VisitResult.TERMINATE) {
                        return VisitResult.TERMINATE;
                    }
                }
            }
        }
        return VisitResult.CONTINUE;
    }

    /**
     * 根据条件获取路径
     *
     * @param treeList  树
     * @param predicate 谓词函数 传入参数：当前节点 返回：布尔值
     * @param path      路径（结果）
     * @param <K>       树节点唯一标识类型
     * @param <N>       树节点类型
     * @return 路径的最后一个节点
     */
    public static <K, N extends ITreeNode<K, N>> N getPath(List<N> treeList, Predicate<N> predicate, List<N> path) {
        if (treeList != null && treeList.size() > 0) {
            for (N node : treeList) {
                path.add(node);
                if (predicate.test(node)) {
                    return node;
                }
                if (node.getChildren() != null && node.getChildren().size() > 0) {
                    N hit = getPath(node.getChildren(), predicate, path);
                    if (hit != null) {
                        return hit;
                    }
                }
                path.remove(node);
            }
        }
        return null;
    }

}

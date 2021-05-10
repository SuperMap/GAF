package com.supermap.gaf.data.graph.service;


import com.supermap.gaf.data.graph.entity.vo.NodeQueryParam;
import com.supermap.gaf.data.graph.entity.vo.NodeQueryResponse;


/**
 * @author : duke
 * @since 2021/5/10 2:36 PM
 */
public interface DataGraphQueryService {
    /**
     * 查询所在节点层级以内的所有关系图
     * @param nodeQueryParam
     * @return
     */
    NodeQueryResponse levelQuery(NodeQueryParam nodeQueryParam);
}

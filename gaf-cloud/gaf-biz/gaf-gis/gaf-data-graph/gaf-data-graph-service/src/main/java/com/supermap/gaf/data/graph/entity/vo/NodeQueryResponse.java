package com.supermap.gaf.data.graph.entity.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author : duke
 * @since 2021/5/10 2:57 PM
 */
@Data
public class NodeQueryResponse {
    private List<Map<String,Object>> nodes;
    private List<Map<String,Object>> links;
}

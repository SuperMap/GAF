package com.supermap.gaf.data.graph.service.impl;

import com.google.common.collect.Iterables;
import com.supermap.gaf.data.graph.config.Neo4jProperties;
import com.supermap.gaf.data.graph.entity.vo.NodeQueryParam;
import com.supermap.gaf.data.graph.entity.vo.NodeQueryResponse;
import com.supermap.gaf.data.graph.service.DataGraphQueryService;
import com.supermap.gaf.data.graph.utils.Neo4jAutoCloseable;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.InternalRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : duke
 * @since 2021/5/10 3:04 PM
 */
@Service
@EnableConfigurationProperties(Neo4jProperties.class)
public class DataGraphQueryServiceImpl implements DataGraphQueryService {
    private static final String QUERY_START_FIELD="A";
    private static final String QUERY_END_FIELD="B";
    private static final String QUERY_REL_FIELD="C";
    private static final String LABEL="label";
    private static final String TYPE="type";
    private static final String IDENTITY="identity";
    private static final String SOURCE="source";
    private static final String TARGET="target";
    private static final String NAME="name";
    @Autowired
    private Neo4jProperties neo4jProperties;

    @Override
    public NodeQueryResponse levelQuery(NodeQueryParam nodeQueryParam) {
        try (Neo4jAutoCloseable neo4j = new Neo4jAutoCloseable(neo4jProperties)){
            String label = nodeQueryParam.getLabel();
            String cql = String.format("match (n1:%s %s)-[r]-(n2) return n1 as %s,n2 as %s,r as %s",
                    label,
                    nodeQueryParam.getJsonFormatAttribute(),
                    QUERY_START_FIELD,QUERY_END_FIELD,QUERY_REL_FIELD);
            List<Map<String, Object>> levelQueryResponse = neo4j.read(cql);
            return levelQueryResponseConvert(levelQueryResponse);
        }
    }

    /**
     * neo4j数据转为d3.js数据格式
     * @param levelQueryResponse
     * @return
     */
    private NodeQueryResponse levelQueryResponseConvert(List<Map<String, Object>> levelQueryResponse){
        NodeQueryResponse response = new NodeQueryResponse();
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> links = new ArrayList<>();
        response.setNodes(nodes);
        response.setLinks(links);
        for (Map<String, Object> row : levelQueryResponse){
            Map<String, Object> startNode = convertNode(row,QUERY_START_FIELD);
            Map<String, Object> endNode = convertNode(row,QUERY_END_FIELD);
            Map<String, Object> relationship = convertRel(row,startNode,endNode);
            nodes.add(startNode);
            nodes.add(endNode);
            links.add(relationship);
        }
        response.setNodes(checkRepeatIdentity(response.getNodes()));
        return response;
    }

    /**
     * neo4j数据转为d3.js数据格式
     * @param row
     * @param key
     * @return
     */
    private Map<String, Object> convertNode(Map<String, Object> row,String key){
        InternalNode node = (InternalNode)row.get(key);
        Map<String, Object> nodeMap = new HashMap<>(node.asMap());
        nodeMap.put(IDENTITY, node.id());
        nodeMap.put(LABEL, Iterables.get(node.labels(),0));
        return nodeMap;
    }

    /**
     * neo4j数据转为d3.js数据格式
     * @param row
     * @param startNode
     * @param endNode
     * @return
     */
    private Map<String, Object> convertRel(Map<String, Object> row,Map<String, Object> startNode,Map<String, Object> endNode){
        InternalRelationship relationship = (InternalRelationship)row.get(QUERY_REL_FIELD);
        Map<String, Object> relationshipMap = new HashMap<>(relationship.asMap());
        relationshipMap.put(SOURCE, startNode.get(NAME));
        relationshipMap.put(TARGET, endNode.get(NAME));
        relationshipMap.put(TYPE,  relationship.type());
        relationshipMap.put(IDENTITY, relationship.id());
        return relationshipMap;
    }

    /**
     * node去重
     * @param nodes
     * @return
     */
    private List<Map<String, Object>> checkRepeatIdentity(List<Map<String, Object>> nodes){
        return nodes.stream().distinct().collect(Collectors.toList());
    }
}

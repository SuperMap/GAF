/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.srv.governance.entity.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.SearchHit;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.supermap.gaf.srv.governance.constant.SrvConstant.*;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/12/4 8:54 AM
 */
@Data
@NoArgsConstructor
@ApiModel("链路追踪查询节点对象")
public class TraceTreeNode implements Serializable {
    private static final long serialVersionUID = 7454672314997411587L;

    @ApiModelProperty("链路记录id")
    private String id;
    @ApiModelProperty("traceId")
    private String traceId;
    @ApiModelProperty("父节点id")
    private String parentId;
    @ApiModelProperty("类型")
    private String kind;
    @ApiModelProperty("记录时间戳")
    private String timestamp;
    @ApiModelProperty("时间戳格式化")
    private String timestampFormat;
    @ApiModelProperty("耗时时长")
    private String duration;
    @ApiModelProperty("耗时时长格式化")
    private String durationFormat;
    @ApiModelProperty("服务名")
    private String serviceName;
    @ApiModelProperty("ip地址")
    private String ipv4;
    @ApiModelProperty("请求方法")
    private String httpMethod;
    @ApiModelProperty("请求路径")
    private String httpPath;
    @ApiModelProperty("错误描述")
    private String error;
    @ApiModelProperty("用户名")
    private String username;

    private String rectWidth;
    private String rectLeftOffset;
    private List<TraceTreeNode> children;


    public static TraceTreeNode buildTraceTreeNode(SearchHit searchHit) {
        if (searchHit == null) {
            return null;
        }
        TraceTreeNode result = new TraceTreeNode();
        Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();

        String id = (String) sourceAsMap.get(TRACE_ID_FIELD_NAME);
        result.setId(id);
        String traceId = (String) sourceAsMap.get("traceId");
        result.setTraceId(traceId);

        if (sourceAsMap.containsKey(TRACE_PARENTID_FIELD_NAME)) {
            String parentId = (String) sourceAsMap.get(TRACE_PARENTID_FIELD_NAME);
            result.setParentId(parentId);
        }
        if (sourceAsMap.containsKey(TRACE_KIND_FIELD_NAME)) {
            String kind = (String) sourceAsMap.get(TRACE_KIND_FIELD_NAME);
            result.setKind(kind);
        }
        if (sourceAsMap.containsKey(TRACE_TIMESTAMP_MILLIS_FIELD_NAME)) {
            Long timestamp = (Long) sourceAsMap.get(TRACE_TIMESTAMP_MILLIS_FIELD_NAME);
            result.setTimestamp(Long.toString(timestamp));
            String timestampFormat = formatTraceTimestamp(timestamp);
            result.setTimestampFormat(timestampFormat);
        }
        if (sourceAsMap.containsKey(TRACE_DURATION_FIELD_NAME)) {
            Integer duration = (Integer) sourceAsMap.get(TRACE_DURATION_FIELD_NAME);
            result.setDuration(duration.toString());
            String durationFormat = Integer.valueOf(duration / 1000).toString() + "ms";
            result.setDurationFormat(durationFormat);
        }
        if (sourceAsMap.containsKey(TRACE_LOCALENDPOINT_FIELD_NAME)) {
            HashMap localEndpoint = (HashMap) sourceAsMap.get(TRACE_LOCALENDPOINT_FIELD_NAME);
            if (localEndpoint.containsKey(SERVICENAME)) {
                String serviceName = (String) localEndpoint.get(SERVICENAME);
                result.setServiceName(serviceName);
            }
            if (localEndpoint.containsKey(IPV4)) {
                String ipv4 = (String) localEndpoint.get(IPV4);
                result.setIpv4(ipv4);
            }
        }
        if (sourceAsMap.containsKey(TAGS)) {
            HashMap tags = (HashMap) sourceAsMap.get(TAGS);
            if (tags.containsKey(TRACE_HTTP_METHOD_FIELD_NAME)) {
                String httpMethod = (String) tags.get(TRACE_HTTP_METHOD_FIELD_NAME);
                result.setHttpMethod(httpMethod);
            }
            if (tags.containsKey(TRACE_HTTP_PATH_FIELD_NAME)) {
                String httpPath = (String) tags.get(TRACE_HTTP_PATH_FIELD_NAME);
                result.setHttpPath(httpPath);
            }
            if (tags.containsKey(ERROR)) {
                String error = (String) tags.get(ERROR);
                result.setError(error);
            }
            if (tags.containsKey(USERNAME)) {
                String username = (String) tags.get(USERNAME);
                result.setUsername(username);
            }
        }
        return result;
    }

    /**
     * 属于同一trace的多个span合并为树结构
     *
     * @param nodes
     * @return
     */
    public static TraceTreeNode buildTraceTreeNodeToOne(List<TraceTreeNode> nodes) {
        //step 1: 先找到根节点
        TraceTreeNode result = null;
        for (TraceTreeNode node : nodes) {
            if (StringUtils.isEmpty(node.getParentId())) {
                result = node;
            }
        }
        //step 2: 计算时间偏移属性
        for (TraceTreeNode node : nodes) {
            calculateRect(node, result);
        }
        //step 3: 根节点往下构建
        recursionBuildTree(result, nodes);
        return result;
    }

    private static void recursionBuildTree(TraceTreeNode node, List<TraceTreeNode> nodes) {
        if (node == null) {
            return;
        }
        String id = node.getId();
        if (CollectionUtils.isEmpty(node.getChildren())) {
            node.setChildren(new ArrayList<>());
        }
        List<TraceTreeNode> children = node.getChildren();
        //找到父id为id的子节点
        for (TraceTreeNode n : nodes) {
            if (!StringUtils.isEmpty(n.getParentId()) && id.equals(n.getParentId())) {
                children.add(n);
            }
        }

        if (!CollectionUtils.isEmpty(children)) {
            //排序
            children = children.stream().sorted(Comparator.comparingLong(item -> Long.valueOf(item.getTimestamp()))).collect(Collectors.toList());
            node.setChildren(children);
            //层序
            for (TraceTreeNode n : children) {
                recursionBuildTree(n, nodes);
            }
        } else {
            node.setChildren(null);
        }
    }

    private static void calculateRect(TraceTreeNode node, TraceTreeNode root) {
        int width = (Integer.valueOf(node.getDuration()) * 100) / Integer.valueOf(root.getDuration());
        long leftOffset = ((Long.valueOf(node.getTimestamp()) - Long.valueOf(root.getTimestamp())) * 100000) / Long.valueOf(root.getDuration());
        node.setRectWidth(String.valueOf(width));
        node.setRectLeftOffset(String.valueOf(leftOffset));
    }

    private static String formatTraceTimestamp(Long traceTimestamp) {
        Date date = new Date(traceTimestamp);
        SimpleDateFormat format = new SimpleDateFormat(MILLIS_FORMAT);
        return format.format(date);
    }

}

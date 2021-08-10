/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.analysis.entity;

import com.supermap.services.components.commontypes.DataReturnOption;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.services.components.commontypes.QueryParameter;
import com.supermap.services.rest.PostResultType;
import com.supermap.services.rest.commontypes.DatasetOverlayPostParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.core.UriBuilder;
import java.io.Serializable;

/**
 * @author dqc
 * @date:2021/3/25
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel("数据集叠加分析参数实体类")
public class DatasetOverlayParamVO implements Serializable {

    private static final long serialVersionUID = 2959086979634330277L;

    /**
     * iserver分析服务地址
     */
    @ApiModelProperty("iserver数据集叠加分析服务地址,包含第一数据集信息")
    private String spatialAnalystDatasetOverlayUrl;
    /**
     * iserver数据集叠加需要的query参数
     */
    @ApiModelProperty("是否采用异步操作。如果为 true，则在客户端提交请求后会立即返回新资源的 URI（即 returnContent 的设置不起作用）； 为 false，则服务端在分析完成后根据 returnContent 的设置返回新资源的表述或者新资源的 URI。默认为 false。")
    private Boolean asynchronousReturn;
    @ApiModelProperty("是否立即返回新创建资源的表述还是返回新资源的 URI。如果为 true，则直接返回新创建资源，即分析结果的表述。为 false，则返回的是分析结果资源的 URI。默认为 false。")
    private Boolean returnContent;
    /**
     * iserver数据集叠加需要的请求体参数
     */
    @ApiModelProperty("源数据集中要复制的字段。")
    private QueryParameter sourceDatasetFilter;
    @ApiModelProperty("源数据集中要复制的字段。")
    private String[] sourceDatasetFields;
    @ApiModelProperty("操作的第二数据集标识，表示与该数据集进行叠加分析。与 operateRegions 参数互斥，冲突时以本参数为准。")
    private String operateDataset;
    @ApiModelProperty("用于对操作数据集进行过滤的过滤条件。")
    private QueryParameter operateDatasetFilter;
    @ApiModelProperty("操作数据集中要复制的字段。")
    private String[] operateDatasetFields;
    @ApiModelProperty("操作面对象集合，表示与这些面对象进行叠加分析。与 operateDataset 参数互斥，冲突时以 operateDataset 为准。")
    private Geometry[] operateRegions;
    @ApiModelProperty("叠加操作，可选值为 clip（裁剪）、erase（擦除）、identity（同一）、intersect（相交）、union（合并）、update（更新）、XOR（对称差）。 \n" +
            "其中，对于 clip（裁剪）、erase（擦除）、identity（同一）和 intersect（相交）四种操作，操作数据集支持的数据类型为面数据集，或者面状几何对象数据，被操作数据集的数据类型为点、线、面数据集。 \n" +
            "而对于 union（合并）、update（更新）、XOR（对称差）三种操作，操作数据集支持的数据类型为面数据集，或者面状几何对象数据，被操作数据集的数据类型只能是面数据集")
    private String operation;
    @ApiModelProperty("返回的提取结果设置。")
    private DataReturnOption dataReturnOption;
    @ApiModelProperty("叠加分析的容限值。叠加分析后，若两个节点之间的距离小于此值，则将这两个节点合并。")
    private Double tolerance;

    /**
     * 组装请求体参数
     *
     * @return
     */
    public DatasetOverlayPostParameter buildDatasetOverlayPostParameter() {
        DatasetOverlayPostParameter postParameter = new DatasetOverlayPostParameter();
        postParameter.sourceDatasetFilter = sourceDatasetFilter;
        postParameter.sourceDatasetFields = sourceDatasetFields;
        postParameter.operateDataset = operateDataset;
        postParameter.operateDatasetFilter = operateDatasetFilter;
        postParameter.operateDatasetFields = operateDatasetFields;
        postParameter.operateRegions = operateRegions;
        postParameter.operation = operation;
        postParameter.dataReturnOption = dataReturnOption;
        postParameter.tolerance = tolerance;
        return postParameter;
    }

    /**
     * 组装请求地址
     *
     * @return
     */
    public String buildDatasetOverlayPostUrl() {
        UriBuilder uriBuilder = UriBuilder.fromUri(spatialAnalystDatasetOverlayUrl);
        if (asynchronousReturn != null) {
            uriBuilder.queryParam("asynchronousReturn", asynchronousReturn);
        }
        if (returnContent != null) {
            uriBuilder.queryParam("returnContent", returnContent);
        }
        return uriBuilder.build().toString();
    }
}

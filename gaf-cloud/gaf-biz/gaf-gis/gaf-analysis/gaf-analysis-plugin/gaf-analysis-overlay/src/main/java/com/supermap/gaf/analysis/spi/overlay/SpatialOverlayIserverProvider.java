/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.analysis.spi.overlay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.gaf.analysis.entity.OverlayParamVO;
import com.supermap.gaf.analysis.entity.RecordSetVO;
import com.supermap.gaf.analysis.entity.SpatialAnalysisDataConnInfo;
import com.supermap.gaf.analysis.entity.SpatialAnalysisResult;
import com.supermap.gaf.analysis.spi.IspatialOverlay;
import com.supermap.gaf.analysis.utils.GeometryUtil;
import com.supermap.gaf.analysis.utils.HttpClientUtil;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.services.components.commontypes.*;
import com.supermap.services.rest.commontypes.DatasetOverlayPostParameter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.supermap.gaf.commontypes.GafCommonConstant.TRUE;


/**
 * @author dqc
 * @date:2021/3/25
 * @since : 2020-12-29
 */
public class SpatialOverlayIserverProvider implements IspatialOverlay {
    private static final String OVERLAY_SUFFIX = "/overlay.json";
    private static final String JSON_SUFFIX = ".json";

    @Override
    public String type() {
        return "OVERLAY-ISERVER";
    }

    @Override
    public MessageResult<String> verifyParam(OverlayParamVO overlayParamVO) {
        List<Geometry> sourceGeometries = overlayParamVO.getSourceGeometries();
        SpatialAnalysisDataConnInfo targetDataConnInfo = overlayParamVO.getTargetDataConnInfo();

        //1. sourceGeometries不能为空
        if (CollectionUtils.isEmpty(sourceGeometries)) {
            return MessageResult.<String>failed(String.class).message("源数据信息不能为空").build();
        }
        //2. targetDataConnInfo不能为空
        if (null == targetDataConnInfo) {
            return MessageResult.<String>failed(String.class).message("目标数据信息不能为空").build();
        }
        return MessageResult.successe(String.class).build();
    }

    @Override
    public MessageResult<SpatialAnalysisResult> overlay(String resultId, OverlayParamVO overlayParamVO) {
        List<Geometry> sourceGeometries = overlayParamVO.getSourceGeometries();
        String resourceUrl = overlayParamVO.getTargetDataConnInfo().getUrl();
        String operation = overlayParamVO.getOperator().name();
        String sourceDatasetFilter = overlayParamVO.getSourceDatasetFilter();
        String targetDatasetFilter = overlayParamVO.getTargetDatasetFilter();
        //坐标转换
        String response = HttpClientUtil.get(resourceUrl + JSON_SUFFIX);
        int targetCode = JSON.parseObject(response).getJSONObject("datasetInfo").getJSONObject("prjCoordSys").getIntValue("epsgCode");
        int sourceCode = 4490;
        sourceGeometries = GeometryUtil.transGeometries(sourceGeometries, sourceCode, targetCode);
        //组装参数
        DatasetOverlayPostParameter datasetOverlayPostParameter = new DatasetOverlayPostParameter();
        datasetOverlayPostParameter.tolerance = 0;
        datasetOverlayPostParameter.operateRegions = sourceGeometries.toArray(new Geometry[sourceGeometries.size()]);
        DataReturnOption dataReturnOption = new DataReturnOption();
        dataReturnOption.deleteExistResultDataset = Boolean.FALSE;
        dataReturnOption.expectCount = 10;
        dataReturnOption.dataReturnMode = DataReturnMode.RECORDSET_ONLY;
        datasetOverlayPostParameter.dataReturnOption = dataReturnOption;
        datasetOverlayPostParameter.operation = getOperatorMethodName(operation);
        if (!StringUtils.isEmpty(sourceDatasetFilter)) {
            QueryParameter queryParameter = new QueryParameter();
            queryParameter.attributeFilter = sourceDatasetFilter;
            datasetOverlayPostParameter.sourceDatasetFilter = queryParameter;
        }
        if (!StringUtils.isEmpty(targetDatasetFilter)) {
            QueryParameter queryParameter = new QueryParameter();
            queryParameter.attributeFilter = targetDatasetFilter;
            datasetOverlayPostParameter.operateDatasetFilter = queryParameter;
        }
        //发起iserver执行分析请求
        String overlayUrl = resourceUrl + OVERLAY_SUFFIX;
        Object overlayResult = HttpClientUtil.execute(overlayUrl, JSON.toJSONString(datasetOverlayPostParameter));
        JSONObject jsonObject = JSON.parseObject(overlayResult.toString());
        String succeed = jsonObject.get(MessageResult.SUCCEED).toString();
        if (TRUE.equalsIgnoreCase(succeed)) {
            String resourceId = jsonObject.getString("newResourceLocation") + JSON_SUFFIX;
            SpatialAnalysisResult spatialAnalysisResult = SpatialAnalysisResult.builder()
                    .resultId(resultId)
                    .resultConnInfo(resourceId)
                    .build();
            return MessageResult.<SpatialAnalysisResult>successe(SpatialAnalysisResult.class).data(spatialAnalysisResult).build();
        } else {
            return MessageResult.<SpatialAnalysisResult>failed(SpatialAnalysisResult.class).build();
        }
    }

    @Override
    public MessageResult<RecordSetVO> getResultRecordSet(SpatialAnalysisResult spatialAnalysisResult, Integer pageIndex, Integer pageSize) {
        Recordset recordset = null;
        String resourceLocation = spatialAnalysisResult.getResultConnInfo();
        if (!StringUtils.isEmpty(resourceLocation)) {
            String mapStr = HttpClientUtil.get(resourceLocation);
            JSONObject json = JSON.parseObject(mapStr);
            if (TRUE.equalsIgnoreCase(json.get(MessageResult.SUCCEED).toString())) {
                recordset = JSON.parseObject(json.get("recordset").toString(), Recordset.class);
            }
        }
        if (null != recordset) {
            RecordSetVO recordSetVO = transResultDataByIserverRecordSet(recordset, pageIndex, pageSize);
            return MessageResult.<RecordSetVO>successe(RecordSetVO.class).data(recordSetVO).build();
        }
        return MessageResult.<RecordSetVO>failed(RecordSetVO.class).build();
    }

    private RecordSetVO transResultDataByIserverRecordSet(Recordset recordSet, Integer pageIndex, Integer pageSize) {
        Feature[] features = recordSet.features;
        int count = features.length;
        String[] fields = recordSet.fields;
        String[] captions = recordSet.fieldCaptions;
        //字段统一转为小写
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].toLowerCase();
        }
        String[][] values = new String[count][fields.length];
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < fields.length; j++) {
                values[i][j] = features[i].fieldValues[j];
            }
        }
        List<com.supermap.services.components.commontypes.Geometry> geometryList = Arrays.stream(features).map(feature -> feature.geometry).collect(Collectors.toList());
        return getResultVo(fields, captions, values, geometryList, count, pageIndex, pageSize);
    }

    private RecordSetVO getResultVo(String[] fields, String[] captions, String[][] values, List<com.supermap.services.components.commontypes.Geometry> geometryList, int count, Integer pageIndex, Integer pageSize) {
        //resultData的value进行截取分页
        int start = pageIndex * pageSize;
        int end = (pageIndex + 1) * pageSize - 1;
        int total = count;
        //判断越界
        if (end >= start && total != 0) {
            start = start < 0 ? 0 : start;
            end = end <= total - 1 ? end : total - 1;
        } else {
            return null;
        }
        int offset = end - start + 1;
        //组装结果集
        List<com.supermap.services.components.commontypes.Geometry> paginationGeometries = new LinkedList<>();
        String[][] results = new String[offset][fields.length];
        for (int i = start; i < end + 1; i++) {
            paginationGeometries.add(geometryList.get(i - start));
            for (int j = 0; j < fields.length; j++) {
                results[i - start][j] = values[i][j];
            }
        }
        //组装dataVo
        RecordSetVO dataVo = new RecordSetVO();
        dataVo.setValues(results);
        dataVo.setFields(fields);
        dataVo.setCaptions(captions);
        dataVo.setTotal(total);
        return dataVo;
    }

    private String getOperatorMethodName(String operator) {
        return operator.toUpperCase();
    }
}

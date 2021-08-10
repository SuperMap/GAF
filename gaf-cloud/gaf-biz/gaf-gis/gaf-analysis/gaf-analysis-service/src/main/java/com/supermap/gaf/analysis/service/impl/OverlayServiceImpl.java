/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.analysis.service.impl;

import com.supermap.gaf.analysis.dao.SpatialAnalysisResultDao;
import com.supermap.gaf.analysis.entity.RecordSetVO;
import com.supermap.gaf.analysis.entity.OverlayParamVO;
import com.supermap.gaf.analysis.entity.SpatialAnalysisResult;
import com.supermap.gaf.analysis.service.OverlayService;
import com.supermap.gaf.analysis.service.registry.IspatialOverlayRegister;
import com.supermap.gaf.analysis.spi.IspatialOverlay;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Executor;

/**
 * @author dqc
 * @date:2021/3/25
 * @since : 2020-12-29
 */
@Service
public class OverlayServiceImpl implements OverlayService {

    private static Logger logger = LogUtil.getLocLogger(OverlayServiceImpl.class);

    @Autowired
    private IspatialOverlayRegister ispatialOverlayRegister;
    @Autowired
    private SpatialAnalysisResultDao spatialAnalysisResultDao;

    @Qualifier("taskExecutor")
    @Autowired
    private Executor executor;

    @Override
    public MessageResult<String> overlay(OverlayParamVO overlayParamVO) {
        if (null == overlayParamVO) {
            return MessageResult.failed(String.class).message("参数不能为空").build();
        }
        //1、SPI读取 获取对应的空间分析实现
        ispatialOverlayRegister.afterPropertiesSet();
        IspatialOverlay ispatialOverlay = ispatialOverlayRegister.getSpatialOverlayProvider(overlayParamVO.getType());
        if (null == ispatialOverlay) {
            return MessageResult.failed(String.class).message("不支持的空间分析类型").build();
        }
        //2、参数合法性校验
        MessageResult<String> verifyResult = ispatialOverlay.verifyParam(overlayParamVO);
        if (!verifyResult.isSuccessed()) {
            return verifyResult;
        }
        //3、生成结果记录ID
        String resultId = UUID.randomUUID().toString();
        //4、异步执行空间分析
        executor.execute(() -> {
            try {
                MessageResult<SpatialAnalysisResult> result = ispatialOverlay.overlay(resultId, overlayParamVO);
                if (result.isSuccessed()) {
                    SpatialAnalysisResult spatialAnalysisResult = result.getData();
                    spatialAnalysisResult.setResultId(resultId);
                    spatialAnalysisResult.setAnalysisName(overlayParamVO.getAnalysisName());
                    spatialAnalysisResult.setCreatedTime(new Date());
                    spatialAnalysisResult.setAnalysisType(overlayParamVO.getType());
                    spatialAnalysisResultDao.addSpatialAnalysisResult(spatialAnalysisResult);
                } else {
                    String error = String.format("专题分析异常，%s", result.getMessage());
                    logger.error(error);
                }
            } catch (Exception e) {
                logger.error("专题分析异常", e);
            }
        });
        return MessageResult.successe(String.class).data(resultId).build();
    }

    @Override
    public MessageResult<RecordSetVO> getResultRecordSet(String resultId, Integer pageIndex, Integer pageSize) {
        try {
            //1、查询数据库中分析结果信息
            SpatialAnalysisResult spatialAnalysisResult = spatialAnalysisResultDao.getById(resultId);
            if (null == spatialAnalysisResult) {
                return MessageResult.failed(RecordSetVO.class).message("不存在的分析结果").build();
            }
            //2、获取对应的空间分析实现
            String spatialAnalysisType = spatialAnalysisResult.getAnalysisType();
            IspatialOverlay ispatialOverlay = ispatialOverlayRegister.getSpatialOverlayProvider(spatialAnalysisType);
            if (null == ispatialOverlay) {
                return MessageResult.failed(RecordSetVO.class).message("不支持的空间分析类型").build();
            }
            //3、根据不同分析类型获取分析结果
            return ispatialOverlay.getResultRecordSet(spatialAnalysisResult, pageIndex, pageSize);
        } catch (Exception e) {
            return MessageResult.failed(RecordSetVO.class).message(e.getMessage()).build();
        }
    }

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.analysis.service.impl;

import com.supermap.analyst.spatialanalyst.OverlayAnalyst;
import com.supermap.analyst.spatialanalyst.OverlayAnalystParameter;
import com.supermap.data.DatasetVector;
import com.supermap.data.Geometry;
import com.supermap.data.Workspace;
import com.supermap.gaf.analysis.entity.OverlayAnalystParamVO;
import com.supermap.gaf.analysis.service.OverlayIobjectService;
import com.supermap.gaf.analysis.util.AnalystUtils;
import com.supermap.services.components.commontypes.Recordset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2021/3/25 4:45 PM
 */
@Slf4j
@Service
public class OverlayIobjectServiceImpl implements OverlayIobjectService {

    /**
     * TODO 先完善ISERVER分析接口
     * @param overlayAnalystParamVO
     * @return
     */
    @Override
    public Recordset overlayViaIobject(OverlayAnalystParamVO overlayAnalystParamVO) {
        OverlayAnalystParameter overlayAnalystParameter = AnalystUtils.buildOverlayAnalystParameter(overlayAnalystParamVO);
        Workspace workspace = new Workspace();
        //获取第一数据集的DatasetVector
        DatasetVector sourceDataset = AnalystUtils.getDatasetVector(workspace,
                    overlayAnalystParamVO.getSourceDatasourceConnectionInfo(),
                    overlayAnalystParamVO.getSourceDatasetName());
        //创建本地临时结果结果数据集的DatasetVector
        DatasetVector resultDataset = AnalystUtils.createTempDatasetVector(workspace);
        //通过第二数据集类型和operation判断并使用OverlayAnalyst的方法
        Class<OverlayAnalyst> overlayAnalystClass = OverlayAnalyst.class;
        boolean flag = false;
        //执行分析
        try {
            if (overlayAnalystParamVO.getOperationDatasourceConnectionInfo() != null && !StringUtils.isEmpty(overlayAnalystParamVO.getOperationDatasetName())){
                DatasetVector operationDataset = AnalystUtils.getDatasetVector(workspace,
                        overlayAnalystParamVO.getOperationDatasourceConnectionInfo(),
                        overlayAnalystParamVO.getOperationDatasetName());
                Method method = overlayAnalystClass.getMethod(overlayAnalystParamVO.getOperation(), DatasetVector.class, DatasetVector.class, DatasetVector.class, OverlayAnalystParameter.class);
                flag = (boolean) method.invoke(null, sourceDataset, operationDataset, resultDataset, overlayAnalystParameter);
            }else if (null != overlayAnalystParamVO.getOperationGeometries() && overlayAnalystParamVO.getOperationGeometries().length != 0){
                Geometry[] geometries = AnalystUtils.getUgoGeometries(overlayAnalystParamVO.getOperationGeometries());
                Method method = overlayAnalystClass.getMethod(overlayAnalystParamVO.getOperation(), DatasetVector.class, Geometry[].class, DatasetVector.class, OverlayAnalystParameter.class);
                flag = (boolean) method.invoke(null, sourceDataset,geometries, resultDataset, overlayAnalystParameter);
            }else {
                log.error("未传入第二数据集参数");
            }
        }catch (Exception e){
            log.error("执行分析时异常");
        }
        //判断是否有结果
        if (flag){
            log.info("分析成功");
        }else {
            log.info("未分析出结果");
            return null;
        }
        return null;
    }
}

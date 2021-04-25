/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.analysis.service.impl;

import com.supermap.data.*;
import com.supermap.gaf.analysis.service.AreaService;
import com.supermap.gaf.analysis.utils.WorkspaceParser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/9/2 8:56 AM
 */
@Service
public class AreaServiceImpl implements AreaService {

    private static final String SMAREA = "SmArea";
    private static final int DEFAULT_EPSG_CODE = 4490;
    private static final int AREA_SCALE = 4;

    @Override
    public com.supermap.services.components.commontypes.Geometry getGeometryById(DatasourceConnectionInfo connectionInfo, String dataSetName, String id) {
        return null;
    }

    @Override
    public String calculateDataSetArea(DatasourceConnectionInfo connectionInfo, String dataSetName) {
        List<String> areaStrs = new ArrayList<>();
        //新建工作空间
        Workspace workspace = new Workspace();
        //获取dataset
        DatasetVector dataset = (DatasetVector) WorkspaceParser.getDataset(workspace, connectionInfo, dataSetName);
        Recordset recordset = dataset.getRecordset(false, CursorType.STATIC);
        for (recordset.moveFirst(); !recordset.isEOF(); recordset.moveNext()) {
            areaStrs.add(recordset.getString(SMAREA));
        }
        //面积相加
        return sumArea(areaStrs);
    }

    @Override
    public String getGeometryArea(List<Geometry> ugoGeometries) {
        BigDecimal sum = new BigDecimal(0);
        for (Geometry geo : ugoGeometries) {
            GeoRegion region = (GeoRegion) geo;
            sum = sum.add(BigDecimal.valueOf(region.getPreciseArea(PrjCoordSys.fromEPSG(DEFAULT_EPSG_CODE))));
        }
        sum = sum.setScale(AREA_SCALE, BigDecimal.ROUND_HALF_UP);
        return sum.toString();
    }

    private String sumArea(List<String> areaStrs) {
        BigDecimal sum = new BigDecimal(0);
        for (String areaStr : areaStrs) {
            sum = sum.add(new BigDecimal(areaStr));
        }
        sum = sum.setScale(AREA_SCALE, BigDecimal.ROUND_HALF_UP);
        return sum.toString();
    }

}

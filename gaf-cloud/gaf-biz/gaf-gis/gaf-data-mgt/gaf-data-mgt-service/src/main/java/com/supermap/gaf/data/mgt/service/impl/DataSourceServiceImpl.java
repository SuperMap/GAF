/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.service.impl;

import com.supermap.data.Dataset;
import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.gaf.data.mgt.entity.DataSourceInfo;
import com.supermap.gaf.data.mgt.entity.GDataset;
import com.supermap.gaf.data.mgt.service.DataSourceService;
import com.supermap.gaf.data.mgt.util.DatasourceParser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:yd
 * @date:2021/3/25
 * @Date 2021-3-01
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Override
    public List<GDataset> listDataset(DataSourceInfo dataSourceInfo) {
        List<GDataset> data = new ArrayList<>(8);
        if (null == dataSourceInfo) {
            return data;
        }
        Datasource datasource = DatasourceParser.parserDatasource(dataSourceInfo);
        Datasets datasets = datasource.getDatasets();
        int datasetCount = datasets.getCount();
        for (int i = 0; i < datasetCount; i++) {
            Dataset dataset = datasets.get(i);
            GDataset gDataset = new GDataset();
            gDataset.setDatasetName(dataset.getName());
            gDataset.setCaption(dataset.getTableName());
            gDataset.setDatasetType(dataset.getType().name());
            data.add(gDataset);
        }
        return data;
    }
}

package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.data.mgt.entity.MmField;

import java.util.List;

public interface FieldsService {
    void addField(String datasourceId, String datasetName, MmField mmField);

    void modifyField(String datasourceId, String datasetName, String fieldName, MmField mmField);

    void removeField(String datasourceId, String datasetName, String fieldName);

    List<MmField> fields(String datasourceId, String datasetName);
}

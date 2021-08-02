package com.supermap.gaf.data.mgt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supermap.data.*;
import com.supermap.data.conversion.DataImport;
import com.supermap.data.conversion.ImportResult;
import com.supermap.data.conversion.ImportSetting;
import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.data.mgt.enums.ImportSettingTypeEnum;
import com.supermap.gaf.data.mgt.service.SpaceDatasourceService;
import com.supermap.gaf.data.mgt.service.SysResourceDatasourceService;
import com.supermap.gaf.data.mgt.support.ConvertHelper;
import com.supermap.gaf.exception.GafException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wxl
 * @since 2021/7/27
 */
@Service
public class SpaceDatasourceServiceImpl implements SpaceDatasourceService {

    @Autowired
    private SysResourceDatasourceService sysResourceDatasourceService;
    @Autowired
    private ConvertHelper convertHelper;

    @Override
    public boolean checkNameRepeat(String dsName) {
        List<SysResourceDatasource> datasources = sysResourceDatasourceService.getByName(dsName, true, false);
        return datasources.size() != 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysResourceDatasource createEmptySpaceDatasource(SysResourceDatasource sysResourceDatasource) {
        SysResourceDatasource result = sysResourceDatasourceService.insertSysResourceDatasource(sysResourceDatasource);
        DatasourceConnectionInfo datasourceConnectionInfo = convertHelper.conver2DatasourceConnectionInfo(sysResourceDatasource);
        Datasource datasource = null;
        try {
            datasource = new Datasource(datasourceConnectionInfo.getEngineType());
            boolean createSuccess = datasource.create(datasourceConnectionInfo);
            if (!createSuccess) {
                throw new GafException("创建空的空间数据源失败");
            }
        } finally {
            if (datasource != null) {
                datasource.close();
            }
        }

        return result;
    }

    // 若数据集重名，暂时直接获取新的数据集名创建，不覆盖
    @Override
    public void createSpaceDatasourceByTemplate(String templateId, String targetId) {
        SysResourceDatasource template = sysResourceDatasourceService.getById(templateId);
        SysResourceDatasource target = sysResourceDatasourceService.getById(targetId);
        DatasourceConnectionInfo templateConnInfo = convertHelper.conver2DatasourceConnectionInfo(template);
        DatasourceConnectionInfo targetConnInfo = convertHelper.conver2DatasourceConnectionInfo(target);
        Workspace templateWs = null;
        Datasource templateDs = null;
        Workspace targetWs = null;
        Datasource targetDs = null;
        try {
            templateWs = new Workspace();
            templateConnInfo.setReadOnly(true);
            templateDs = templateWs.getDatasources().open(templateConnInfo);
            Datasets templateDatasets = templateDs.getDatasets();
            int count = templateDatasets.getCount();

            targetWs = new Workspace();
            targetDs = targetWs.getDatasources().open(targetConnInfo);
            Datasets targetDatasets = targetDs.getDatasets();
            for (int i = 0; i < count; i++) {
                Dataset dataset = templateDatasets.get(i);
                String availableDatasetName = targetDatasets.getAvailableDatasetName(dataset.getName());
                targetDatasets.createFromTemplate(availableDatasetName, dataset);
            }
        } finally {
            closeDatasource(templateDs);
            closeDatasource(targetDs);
            closeWorkspace(templateWs);
            closeWorkspace(targetWs);
        }


    }

    @Override
    public ImportResult importData(String importSettingJsonStr) {
        JSONObject importSettingJO = JSON.parseObject(importSettingJsonStr);
        String basePart = importSettingJO.getString("basePart");
        JSONObject basePartJO = JSON.parseObject(basePart);
        String importSettingType = basePartJO.getString("importSettingType");
        String sourceFilePath = basePartJO.getString("sourceFilePath");
        String targetDatasourceId = basePartJO.getString("targetDatasourceId");
        DatasourceConnectionInfo targetDatasourceConnectionInfo = basePartJO.getObject("targetDatasourceConnectionInfo", DatasourceConnectionInfo.class);
        SysResourceDatasource targetSysResourceDatasource = basePartJO.getObject("targetSysResourceDatasource", SysResourceDatasource.class);

        ImportSetting importSetting = ImportSettingTypeEnum.valueOf(importSettingType).parseImportSetting(importSettingJsonStr);
        // todo:还需处理特别情况
        String realSourceFilePath = sourceFilePath;
        importSetting.setSourceFilePath(realSourceFilePath);
        if (targetDatasourceId != null && !targetDatasourceId.isEmpty()) {
            SysResourceDatasource sysResourceDatasource = sysResourceDatasourceService.getById(targetDatasourceId);
            DatasourceConnectionInfo datasourceConnectionInfo = convertHelper.conver2DatasourceConnectionInfo(sysResourceDatasource);
            importSetting.setTargetDatasourceConnectionInfo(datasourceConnectionInfo);
        } else if (targetDatasourceConnectionInfo != null) {
            importSetting.setTargetDatasourceConnectionInfo(targetDatasourceConnectionInfo);
        } else if (targetSysResourceDatasource != null) {
            DatasourceConnectionInfo datasourceConnectionInfo = convertHelper.conver2DatasourceConnectionInfo(targetSysResourceDatasource);
            importSetting.setTargetDatasourceConnectionInfo(datasourceConnectionInfo);
        } else {
            throw new IllegalArgumentException("无目标数据源信息");
        }
        DataImport dataImport = new DataImport();
        dataImport.getImportSettings().add(importSetting);
        ImportResult importResult = dataImport.run();

        importSetting.dispose();
        dataImport.dispose();
        return importResult;
    }

    private void closeDatasource(Datasource datasource) {
        if (datasource != null) {
            datasource.close();
        }
    }

    private void closeWorkspace (Workspace workspace) {
        if (workspace != null) {
            workspace.close();
            workspace.dispose();
        }
    }
}

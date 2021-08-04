package com.supermap.gaf.data.mgt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.data.*;
import com.supermap.data.conversion.*;
import com.supermap.gaf.common.storage.client.StorageClient;
import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.data.mgt.conversion.ConversionConfig;
import com.supermap.gaf.data.mgt.conversion.result.DataExportResult;
import com.supermap.gaf.data.mgt.conversion.result.DataImportResult;
import com.supermap.gaf.data.mgt.conversion.result.ExportMapping;
import com.supermap.gaf.data.mgt.conversion.result.ImportMapping;
import com.supermap.gaf.data.mgt.entity.DataSourceInfo;
import com.supermap.gaf.data.mgt.entity.GDataset;
import com.supermap.gaf.data.mgt.enums.ExportSettingTypeEnum;
import com.supermap.gaf.data.mgt.enums.ImportSettingTypeEnum;
import com.supermap.gaf.data.mgt.service.SpaceDatasourceService;
import com.supermap.gaf.data.mgt.service.SysResourceDatasourceService;
import com.supermap.gaf.data.mgt.support.ConvertHelper;
import com.supermap.gaf.data.mgt.support.DatasourceParser;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

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

    @Autowired
    @Qualifier("DatamgtStorageClient")
    private StorageClient storageClient;

    @Autowired
    private DatasourceParser datasourceParser;

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
    public DataImportResult importData(String importSettingJsonArray) {
        JSONArray array = JSON.parseArray(importSettingJsonArray);
        Assert.notEmpty(array,"导入设置json数组不能为空");
        List<ImportSetting> needCloseImportSettings = new LinkedList<>();
        DataImport dataImport = new DataImport();
        try {
            ImportSettings importSettings = dataImport.getImportSettings();
            Map<String,String> pathMap = new HashMap<>();
            for (int i = 0; i < array.size(); i++) {
                JSONObject importSettingJO = array.getJSONObject(i);

                JSONObject commonPartJO = importSettingJO.getJSONObject("commonPart");
                String importSettingType = commonPartJO.getString("importSettingType");
                Assert.notNull(importSettingType,"数据导入类型不能为null");
                String sourceFilePath = commonPartJO.getString("sourceFilePath");
                Assert.notNull(sourceFilePath,"源数据文件路径不能为null");
                String realSourceFilePath =  convertHelper.resolve(sourceFilePath);// 测试时暂时注释
                pathMap.put(realSourceFilePath,sourceFilePath);
                //pathMap.put(sourceFilePath,sourceFilePath);
                JSONObject basePartJO = importSettingJO.getJSONObject("basePart");
                String worldFilePath = basePartJO.getString("worldFilePath");
                if (worldFilePath != null && !worldFilePath.isEmpty()) {
                    basePartJO.put("worldFilePath", convertHelper.resolve(worldFilePath));
                }

                String targetDatasourceId = commonPartJO.getString("targetDatasourceId");
                DatasourceConnectionInfo targetDatasourceConnectionInfoTemp = null;
                JSONObject targetDsConJO = commonPartJO.getJSONObject("targetDatasourceConnectionInfo");
                if (targetDsConJO != null) {
                    targetDatasourceConnectionInfoTemp = JSON.parseObject(targetDsConJO.toJSONString(),DatasourceConnectionInfo.class, ConversionConfig.getParseConfig());
                }
                DatasourceConnectionInfo targetDatasourceConnectionInfo = targetDatasourceConnectionInfoTemp;

                SysResourceDatasource targetSysResourceDatasource = commonPartJO.getObject("targetSysResourceDatasource", SysResourceDatasource.class);

                ImportSettingTypeEnum importSettingTypeEnum = ImportSettingTypeEnum.valueOf(importSettingType);
                ImportSetting importSetting2 = importSettingTypeEnum.parseImportSetting(importSettingJO, importSetting -> {
                    importSetting.setSourceFilePath(realSourceFilePath);
                    //importSetting.setSourceFilePath(sourceFilePath);
                    if (targetDatasourceId != null && !targetDatasourceId.isEmpty()) {
                        SysResourceDatasource sysResourceDatasource = sysResourceDatasourceService.getById(targetDatasourceId);
                        DatasourceConnectionInfo datasourceConnectionInfo = convertHelper.conver2DatasourceConnectionInfo(sysResourceDatasource);
                        importSetting.setTargetDatasourceConnectionInfo(datasourceConnectionInfo);
                    } else if (targetDatasourceConnectionInfo != null) {
                        if (convertHelper.isFileType(targetDatasourceConnectionInfo.getEngineType())) {
                            String server = targetDatasourceConnectionInfo.getServer();
                            targetDatasourceConnectionInfo.setServer(convertHelper.resolve(server));
                        }
                        if (EngineType.SQLPLUS.equals(targetDatasourceConnectionInfo.getEngineType())) {
                            targetDatasourceConnectionInfo.setDriver("SQL SERVER");
                        }
                        importSetting.setTargetDatasourceConnectionInfo(targetDatasourceConnectionInfo);
                    } else if (targetSysResourceDatasource != null) {
                        DatasourceConnectionInfo datasourceConnectionInfo = convertHelper.conver2DatasourceConnectionInfo(targetSysResourceDatasource);
                        importSetting.setTargetDatasourceConnectionInfo(datasourceConnectionInfo);
                    } else {
                        throw new IllegalArgumentException("无目标数据源信息");
                    }
                });
                importSettings.add(importSetting2);
                needCloseImportSettings.add(importSetting2);
            }
            dataImport.setImportSettings(importSettings);
            ImportResult importResult = dataImport.run();
            return convert(importResult,pathMap);
        } finally {
            needCloseImportSettings.forEach(IDisposable::dispose);
            dataImport.dispose();
        }
    }


    private DataImportResult convert(ImportResult importResult,Map<String,String> pathMap) {
        DataImportResult dataImportResult = new DataImportResult();
        List<ImportMapping> succeedMappings = new LinkedList<>();
        ImportSetting[] succeedSettings = importResult.getSucceedSettings();
        if (succeedSettings != null) {
            for (ImportSetting succeedSetting : succeedSettings) {
                String sourceFilePath = succeedSetting.getSourceFilePath();
                String alias = succeedSetting.getTargetDatasourceConnectionInfo().getAlias();
                String[] succeedDatasetNames = importResult.getSucceedDatasetNames(succeedSetting);
                String[] succeedMapNames = importResult.getSucceedMapNames(succeedSetting);
                ImportMapping importMapping = new ImportMapping();
                importMapping.setSourceFilePath(pathMap.get(sourceFilePath));
                importMapping.setTargetDatasourceAlias(alias);
                if (succeedDatasetNames != null) {
                    importMapping.setDatasetNames(Arrays.asList(succeedDatasetNames));
                }
                if (succeedMapNames != null) {
                    importMapping.setMapNames(Arrays.asList(succeedMapNames));
                }
                succeedMappings.add(importMapping);
            }
        }
        dataImportResult.setSuccessed(succeedMappings);
        List<ImportMapping> failedMappings = new LinkedList<>();
        ImportSetting[] failedSettings = importResult.getFailedSettings();
        if (failedSettings != null) {
            for (ImportSetting failedSetting : failedSettings) {
                ImportMapping importMapping = new ImportMapping();
                String sourceFilePath = failedSetting.getSourceFilePath();
                String alias = failedSetting.getTargetDatasourceConnectionInfo().getAlias();
                importMapping.setSourceFilePath(pathMap.get(sourceFilePath));
                importMapping.setTargetDatasourceAlias(alias);
                failedMappings.add(importMapping);
            }
        }
        dataImportResult.setFailed(failedMappings);
        return dataImportResult;
    }

    @Override
    public DataExportResult exportData(String exportSettingJsonArray) {
        JSONArray array = JSON.parseArray(exportSettingJsonArray);
        Assert.notEmpty(array,"导出设置json数组不能为空");
        List<Workspace> needCloseWorkspaces = new LinkedList<>();
        List<Datasource> needCloseDatasources = new LinkedList<>();
        List<Dataset> needCloseDatasets = new LinkedList<>();
        Map<String,String> pathMap = new HashMap<>();
        DataExport dataExport = new DataExport();
        try {
            ExportSettings exportSettings = dataExport.getExportSettings();
            for (int i = 0; i < array.size(); i++) {
                JSONObject exportSettingJO = array.getJSONObject(i);
                JSONObject commonPartJO = exportSettingJO.getJSONObject("commonPart");
                String exportSettingType = commonPartJO.getString("exportSettingType");
                Assert.notNull(exportSettingType,"导出类型不能为null");
                String sourceDataset = commonPartJO.getString("sourceDataset");
                Assert.notNull(exportSettingType,"需要导出的数据集不能为null");
                String sourceDatasourceId = commonPartJO.getString("sourceDatasourceId");
                DatasourceConnectionInfo datasourceConnectionInfo = commonPartJO.getObject("sourceDatasourceConnectionInfo", DatasourceConnectionInfo.class);
                SysResourceDatasource sysResourceDatasource = commonPartJO.getObject("sourceSysResourceDatasource", SysResourceDatasource.class);

                DatasourceConnectionInfo connectionInfo;
                if (!StringUtils.isEmpty(sourceDatasourceId)) {
                    SysResourceDatasource ds = sysResourceDatasourceService.getById(sourceDatasourceId);
                    connectionInfo = convertHelper.conver2DatasourceConnectionInfo(ds);
                } else if(datasourceConnectionInfo != null) {
                    if (convertHelper.isFileType(datasourceConnectionInfo.getEngineType())) {
                        String server = datasourceConnectionInfo.getServer();
                        datasourceConnectionInfo.setServer(convertHelper.resolve(server));
                    }
                    connectionInfo = datasourceConnectionInfo;
                } else if(sysResourceDatasource != null) {
                    connectionInfo = convertHelper.conver2DatasourceConnectionInfo(sysResourceDatasource);
                } else {
                    throw new IllegalArgumentException("设置需要导出的源数据源信息为空");
                }

                JSONObject basePartJO = exportSettingJO.getJSONObject("basePart");
                Assert.notNull(basePartJO,"导出设置的基本部分basePart不能为null");
                // 提前处理worldFilePath路径
                String worldFilePath = basePartJO.getString("worldFilePath");
                if (worldFilePath != null && !worldFilePath.isEmpty()) {
                    basePartJO.put("worldFilePath", convertHelper.resolve(worldFilePath));
                }
                // 提前处理styleMappingTableFile路径
                String styleMappingTableFile = basePartJO.getString("styleMappingTableFile");
                if (styleMappingTableFile != null && !styleMappingTableFile.isEmpty()) {
                    basePartJO.put("styleMappingTableFile", convertHelper.resolve(styleMappingTableFile));
                }
                // 提前处理 TargetFilePath 路径
                String targetFilePath = basePartJO.getString("targetFilePath");
                Assert.notNull(targetFilePath,"导出目标文件的路径不能为null");
                String realPath = convertHelper.resolve(targetFilePath);
                pathMap.put(realPath,targetFilePath);
                basePartJO.put("targetFilePath",realPath);
                //pathMap.put(targetFilePath,targetFilePath);

                ExportSetting exportSetting = ExportSettingTypeEnum.valueOf(exportSettingType).parseExportSetting(exportSettingJO);
                // 设置需要导出的源数据集
                Workspace workspace = new Workspace();
                connectionInfo.setReadOnly(true);
                Datasource datasource = workspace.getDatasources().open(connectionInfo);
                Dataset dataset = datasource.getDatasets().get(sourceDataset);
                Assert.notNull(dataset,"在数据源中找不到对应的数据集 " + sourceDataset);
                needCloseWorkspaces.add(workspace);
                needCloseDatasources.add(datasource);
                needCloseDatasets.add(dataset);
                exportSetting.setSourceData(dataset);
                exportSettings.add(exportSetting);
            }
            dataExport.setExportSettings(exportSettings);
            ExportResult exportResult = dataExport.run();
            return convert(exportResult,pathMap);
        } finally {
            needCloseDatasets.forEach(Dataset::close);
            needCloseDatasources.forEach(Datasource::close);
            needCloseWorkspaces.forEach(Workspace::close);
            dataExport.dispose();
        }
    }

    private DataExportResult convert(ExportResult exportResult,Map<String,String> pathMap) {
        DataExportResult dataExportResult = new DataExportResult();
        List<ExportMapping> succeedMappings = new LinkedList<>();
        ExportSetting[] succeedSettings = exportResult.getSucceedSettings();
        if (succeedSettings != null) {
            for (ExportSetting succeedSetting : succeedSettings) {
                ExportMapping exportMapping = new ExportMapping();
                Object sourceData = succeedSetting.getSourceData();
                Dataset dataset = (Dataset) sourceData;
                String datasetName = dataset.getName();
                DatasourceConnectionInfo connectionInfo = dataset.getDatasource().getConnectionInfo();
                String alias = connectionInfo.getAlias();
                EngineType engineType = connectionInfo.getEngineType();
                if (!EngineType.UDBX.equals(engineType) && !EngineType.UDB.equals(engineType)) {
                    String server = connectionInfo.getServer();
                    String database = connectionInfo.getDatabase();
                    exportMapping.setDatabaseServerAddr(server + "_" + database);
                }
                boolean overwrite = succeedSetting.isOverwrite();
                String targetFilePath = succeedSetting.getTargetFilePath();
                FileType targetFileType = succeedSetting.getTargetFileType();
                Charset targetFileCharset = succeedSetting.getTargetFileCharset();
                String path = pathMap.get(targetFilePath);
                String downloadSignUrl = storageClient.getDownloadSignUrl(path, SecurityUtilsExt.getUser().getAuthUser().getTenantId());
                exportMapping.setOverwrite(overwrite);
                exportMapping.setTargetFileCharset(targetFileCharset.name());
                exportMapping.setTargetFileType(targetFileType.name());
                exportMapping.setTargetFilePath(path);
                exportMapping.setDownloadSignUrl(downloadSignUrl);
                exportMapping.setDatasourceAlias(alias);
                exportMapping.setSourceDataName(datasetName);
                succeedMappings.add(exportMapping);
            }
        }
        dataExportResult.setSuccessed(succeedMappings);
        List<ExportMapping> failedMappings = new LinkedList<>();
        ExportSetting[] failedSettings = exportResult.getFailedSettings();
        if (failedSettings != null) {
            for (ExportSetting failedSetting : failedSettings) {
                ExportMapping exportMapping = new ExportMapping();
                Object sourceData = failedSetting.getSourceData();
                Dataset dataset = (Dataset) sourceData;
                String datasetName = dataset.getName();
                DatasourceConnectionInfo connectionInfo = dataset.getDatasource().getConnectionInfo();
                String alias = connectionInfo.getAlias();
                EngineType engineType = connectionInfo.getEngineType();
                if (!EngineType.UDBX.equals(engineType) && !EngineType.UDB.equals(engineType)) {
                    String server = connectionInfo.getServer();
                    String database = connectionInfo.getDatabase();
                    exportMapping.setDatabaseServerAddr(server + "_" + database);
                }

                boolean overwrite = failedSetting.isOverwrite();
                String targetFilePath = failedSetting.getTargetFilePath();
                FileType targetFileType = failedSetting.getTargetFileType();
                Charset targetFileCharset = failedSetting.getTargetFileCharset();
                String path = pathMap.get(targetFilePath);
                exportMapping.setOverwrite(overwrite);
                exportMapping.setTargetFileCharset(targetFileCharset.name());
                exportMapping.setTargetFileType(targetFileType.name());
                exportMapping.setTargetFilePath(path);
                exportMapping.setDatasourceAlias(alias);
                exportMapping.setSourceDataName(datasetName);
                failedMappings.add(exportMapping);
            }
        }
        dataExportResult.setFailed(failedMappings);
        return dataExportResult;
    }



    @Override
    public List<GDataset> listDataset(DataSourceInfo dataSourceInfo) {
        if (null == dataSourceInfo) {
            return Collections.emptyList();
        }
        Datasource datasource =  datasourceParser.parserDatasource(dataSourceInfo);
        return listDatasets(datasource);
    }

    @Override
    public List<GDataset> listDataset(String datasourceId) {
        SysResourceDatasource sysResourceDatasource = sysResourceDatasourceService.getById(datasourceId);
        Boolean isSdx = sysResourceDatasource.getIsSdx();
        if (!isSdx) {
            return Collections.emptyList();
        }
        final DatasourceConnectionInfo datasourceConnectionInfo = convertHelper.conver2DatasourceConnectionInfo(sysResourceDatasource);
        Datasource datasource = DatasourceParser.openDatasource(datasourceConnectionInfo);
        List<GDataset> gDatasets = listDatasets(datasource);
        Workspace workspace = datasource.getWorkspace();
        workspace.close();
        workspace.dispose();
        return gDatasets;
    }


    /**
     * 获取数据源下的所有数据集
     * @param datasource 数据源
     * @return 数据集集合
     */
    private List<GDataset> listDatasets(Datasource datasource) {
        if (Objects.isNull(datasource)) {
            return Collections.emptyList();
        }
        Datasets datasets = datasource.getDatasets();
        int datasetCount = datasets.getCount();
        List<GDataset> data = new ArrayList<>(datasetCount);
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

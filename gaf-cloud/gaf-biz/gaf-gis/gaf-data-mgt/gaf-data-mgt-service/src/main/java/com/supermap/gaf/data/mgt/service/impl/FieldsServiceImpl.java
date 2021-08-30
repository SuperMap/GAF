package com.supermap.gaf.data.mgt.service.impl;

import com.supermap.data.*;
import com.supermap.gaf.commontypes.metadata.FieldMetadataInfo;
import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.data.mgt.entity.MmField;
import com.supermap.gaf.data.mgt.enums.DatasourceTypeEnum;
import com.supermap.gaf.data.mgt.service.FieldsService;
import com.supermap.gaf.data.mgt.service.SysResourceDatasourceService;
import com.supermap.gaf.data.mgt.support.ConvertHelper;
import com.supermap.gaf.data.mgt.support.JdbcConnectionInfo;
import com.supermap.gaf.data.mgt.util.DatamgtCommonUtils;
import com.supermap.gaf.data.mgt.util.IobjectUtils;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.utils.JdbcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FieldsServiceImpl implements FieldsService {
    @Autowired
    private SysResourceDatasourceService sysResourceDatasourceService;
    @Autowired
    private ConvertHelper convertHelper;

    
    @Override
    public void addField(String datasourceId, String datasetName, MmField mmField) {
        SysResourceDatasource sysResourceDatasource = sysResourceDatasourceService.getById(datasourceId);
        if (sysResourceDatasource.getIsSdx()) {
            // 空间数据源
            datasetProcessor(sysResourceDatasource, datasetName, dataset -> {
                IobjectUtils.addField(dataset, DatamgtCommonUtils.convert2FieldInfo(mmField,DatamgtCommonUtils.FIELD_TYPE_2_TYPE_CODE_CONVERT));
                return null;
            });
        } else {
            // 非空间数据源
            DatasourceTypeEnum datasourceType = DatasourceTypeEnum.fromName(sysResourceDatasource.getTypeCode());
            JdbcConnectionInfo jdbcConnectionInfo = datasourceType.convert2JdbcConnectionInfo(sysResourceDatasource);
            JdbcUtils.sqlProcessor(jdbcConnectionInfo.getUrl(), jdbcConnectionInfo.getUsername(), jdbcConnectionInfo.getPassword(),
                    connection -> {
                        JdbcUtils.addField(connection, datasetName, DatamgtCommonUtils.convert2FieldMetadataInfo(mmField,DatamgtCommonUtils.FIELD_TYPE_2_TYPE_CODE_CONVERT));
                        return null;
                    });
        }
    }

    @Override
    public void modifyField(String datasourceId, String datasetName, String fieldName, MmField mmField) {
        SysResourceDatasource sysResourceDatasource = sysResourceDatasourceService.getById(datasourceId);
        if (sysResourceDatasource.getIsSdx()) {
            // 空间数据源
            datasetProcessor(sysResourceDatasource, datasetName, dataset -> {
                IobjectUtils.modifyFiled(dataset, fieldName, DatamgtCommonUtils.convert2FieldInfo(mmField,DatamgtCommonUtils.FIELD_TYPE_2_TYPE_CODE_CONVERT));
                return null;
            });
        } else {
            // 非空间数据源
            DatasourceTypeEnum datasourceType = DatasourceTypeEnum.fromName(sysResourceDatasource.getTypeCode());
            JdbcConnectionInfo jdbcConnectionInfo = datasourceType.convert2JdbcConnectionInfo(sysResourceDatasource);
            JdbcUtils.sqlProcessor(jdbcConnectionInfo.getUrl(), jdbcConnectionInfo.getUsername(), jdbcConnectionInfo.getPassword(),
                    connection -> {
                        JdbcUtils.modifyField(connection, datasetName, fieldName,DatamgtCommonUtils.convert2FieldMetadataInfo(mmField,DatamgtCommonUtils.FIELD_TYPE_2_TYPE_CODE_CONVERT));
                        return null;
                    });
        }
    }

    @Override
    public void removeField(String datasourceId, String datasetName, String fieldName) {
        SysResourceDatasource sysResourceDatasource = sysResourceDatasourceService.getById(datasourceId);
        if (sysResourceDatasource.getIsSdx()) {
            // 空间数据源
            datasetProcessor(sysResourceDatasource, datasetName, dataset -> {
                IobjectUtils.removeField(dataset, fieldName);
                return null;
            });
        } else {
            // 非空间数据源
            DatasourceTypeEnum datasourceType = DatasourceTypeEnum.fromName(sysResourceDatasource.getTypeCode());
            JdbcConnectionInfo jdbcConnectionInfo = datasourceType.convert2JdbcConnectionInfo(sysResourceDatasource);
            JdbcUtils.sqlProcessor(jdbcConnectionInfo.getUrl(), jdbcConnectionInfo.getUsername(), jdbcConnectionInfo.getPassword(),
                    connection -> {
                        JdbcUtils.deleteField(connection, datasetName, fieldName);
                        return null;
                    });

        }
    }

    @Override
    public List<MmField> fields(String datasourceId, String datasetName) {
        SysResourceDatasource sysResourceDatasource = sysResourceDatasourceService.getById(datasourceId);
        if (sysResourceDatasource.getIsSdx()) {
            // 空间数据源
            return (List<MmField>) datasetProcessor(sysResourceDatasource, datasetName, dataset -> {
                return IobjectUtils.getRecordsetProperty(dataset);
            });
        } else {
            // 非空间数据源
            DatasourceTypeEnum datasourceType = DatasourceTypeEnum.fromName(sysResourceDatasource.getTypeCode());
            JdbcConnectionInfo jdbcConnectionInfo = datasourceType.convert2JdbcConnectionInfo(sysResourceDatasource);
            return (List<MmField>) JdbcUtils.sqlProcessor(jdbcConnectionInfo.getUrl(), jdbcConnectionInfo.getUsername(), jdbcConnectionInfo.getPassword(),
                    connection -> {
                        List<FieldMetadataInfo> fieldMetadataInfos = JdbcUtils.getTableMetadata(connection, datasetName, true).getFieldMetadataInfos();
                        List<MmField> mmFields = fieldMetadataInfos.stream().map(item->DatamgtCommonUtils.convert2MmField(item,sqlType->datasourceType.getFieldType(sqlType).getCode())).collect(Collectors.toList());                        return mmFields;
                    });
        }
    }

    Object datasetProcessor(SysResourceDatasource sysResourceDatasource, String datasetName, Function<Dataset, Object> process) {
        Workspace workspace = null;
        Datasource datasource = null;
        Object re = null;
        try {
            workspace = new Workspace();
            final DatasourceConnectionInfo datasourceConnectionInfo = convertHelper.conver2DatasourceConnectionInfo(sysResourceDatasource);
            datasource = workspace.getDatasources().open(datasourceConnectionInfo);
            Datasets datasets = datasource.getDatasets();
            int datasetCount = datasets.getCount();
            for (int i = 0; i < datasetCount; i++) {
                Dataset dataset = datasets.get(i);
                if (dataset.getName().equals(datasetName)) {
//                    Object[] args = new Object[extraArgs.length+1];
//                    args[0] = dataset;
//                    for(int j=0;j<extraArgs.length;j++){
//                        args[j+1] = extraArgs[j];
//                    }
                    re = process.apply(dataset);
                    break;
                }
            }
            return re;
        } catch (Exception e) {
            throw new GafException(e.getMessage());
        } finally {
            if (datasource != null && datasource.isOpened()) {
                try {
                    datasource.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (workspace != null) {
                workspace.close();
            }
        }
    }
}

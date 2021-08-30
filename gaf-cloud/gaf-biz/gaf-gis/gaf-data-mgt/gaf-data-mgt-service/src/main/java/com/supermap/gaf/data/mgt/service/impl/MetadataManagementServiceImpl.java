package com.supermap.gaf.data.mgt.service.impl;

import com.supermap.data.*;
import com.supermap.gaf.commontypes.metadata.TableMetadataInfo;
import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.data.mgt.entity.MmField;
import com.supermap.gaf.data.mgt.entity.vo.MetadataVo;
import com.supermap.gaf.data.mgt.enums.DatasourceTypeEnum;
import com.supermap.gaf.data.mgt.mapper.SysResourceDatasourceMapper;
import com.supermap.gaf.data.mgt.service.MetadataManagementService;
import com.supermap.gaf.data.mgt.service.SysResourceDatasourceService;
import com.supermap.gaf.data.mgt.support.ConvertHelper;
import com.supermap.gaf.data.mgt.support.JdbcConnectionInfo;
import com.supermap.gaf.data.mgt.util.DatamgtCommonUtils;
import com.supermap.gaf.data.mgt.util.IobjectUtils;
import com.supermap.gaf.data.mgt.vo.SysResourceDatasourceSelectVo;
import com.supermap.gaf.utils.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MetadataManagementServiceImpl implements MetadataManagementService {
    private final static Logger log = LoggerFactory.getLogger(MetadataManagementServiceImpl.class);
    @Autowired
    private ConvertHelper convertHelper;

    @Autowired
    private SysResourceDatasourceMapper sysResourceDatasourceMapper;
    @Autowired
    private SysResourceDatasourceService sysResourceDatasourceService;

    @Override
    public List<MetadataVo> selectAll() {
        List<MetadataVo> re = new ArrayList<>();
        List<SysResourceDatasource> sysResourceDatasources = sysResourceDatasourceMapper.selectList(SysResourceDatasourceSelectVo.builder().isTemplate(false).orderFieldName("updated_time").orderMethod("desc").build());
        Workspace workspace = null;
        try {
            workspace = new Workspace();
            String volumeRoot = null;
            try {
                volumeRoot = convertHelper.resolve("");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            for (SysResourceDatasource item : sysResourceDatasources) {
                if (!StringUtils.isEmpty(item.getPassword())) {
                    item.setPassword(sysResourceDatasourceService.decrypt(item.getPassword()));
                }
                try {
                    if (item.getIsSdx()) {
                        re.addAll(selectFromSpaceDatasource(workspace, volumeRoot, item));
                    } else {
                        re.addAll(selectFromNormalDatasource(item));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            if (workspace != null) {
                workspace.close();
            }
        }

        return re;
    }

    List<MetadataVo> selectFromSpaceDatasource(Workspace workspace, String volumeRoot, SysResourceDatasource sysResourceDatasource) {
        Datasource datasource = null;
        List<MetadataVo> re = new ArrayList<>();
        try {
            final DatasourceConnectionInfo datasourceConnectionInfo = convertHelper.conver2DatasourceConnectionInfo(sysResourceDatasource, volumeRoot);
            datasource = workspace.getDatasources().open(datasourceConnectionInfo);
            Datasets datasets = datasource.getDatasets();
            int datasetCount = datasets.getCount();
            for (int i = 0; i < datasetCount; i++) {
                Dataset dataset = datasets.get(i);
                MetadataVo metadataVo = MetadataVo.builder().datasourceName(sysResourceDatasource.getDsName()).datasourceId(sysResourceDatasource.getDatasourceId())
                        .recordName(dataset.getName())
                        .recordType(sysResourceDatasource.getTypeCode())
                        .catalogCode(sysResourceDatasource.getCatalogCode())
                        .owner(sysResourceDatasource.getCreatedBy())
                        .updatedTime(new Date())
                        .createdTime(new Date()).build();
                Map<String, Object> spatialInfo = new HashMap<>();
                spatialInfo.put("basic", IobjectUtils.getDatasetProperties(dataset));
                spatialInfo.put("prjCoordSys", IobjectUtils.getPrjCoordSysPropertes(dataset));
                spatialInfo.put("fields", IobjectUtils.getRecordsetProperty(dataset));
//                        metadataVo.setSpatialInfo(JSON.toJSONString(spatialInfo));
                metadataVo.setSpatialInfo(spatialInfo);
                dataset.close();
                re.add(metadataVo);
            }
        } finally {
            if (datasource != null && datasource.isOpened()) {
                try {
                    datasource.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return re;
    }

    List<MetadataVo> selectFromNormalDatasource(SysResourceDatasource sysResourceDatasource) {

        DatasourceTypeEnum datasourceType = DatasourceTypeEnum.fromName(sysResourceDatasource.getTypeCode());
        JdbcConnectionInfo jdbcConnectionInfo = datasourceType.convert2JdbcConnectionInfo(sysResourceDatasource);
        return (List<MetadataVo>) JdbcUtils.sqlProcessor(jdbcConnectionInfo.getUrl(), jdbcConnectionInfo.getUsername(), jdbcConnectionInfo.getPassword(),
                connection -> {
                    List<MetadataVo> re = new ArrayList<>();
                    List<TableMetadataInfo> tableMetadataInfos = JdbcUtils.getTableMetadatas(connection, true);
                    for (TableMetadataInfo tableMetadataInfo : tableMetadataInfos) {
                        MetadataVo metadataVo = MetadataVo.builder().datasourceName(sysResourceDatasource.getDsName()).datasourceId(sysResourceDatasource.getDatasourceId())
                                .recordName(tableMetadataInfo.getTableName())
                                .recordType(sysResourceDatasource.getTypeCode())
                                .catalogCode(sysResourceDatasource.getCatalogCode())
                                .owner(sysResourceDatasource.getCreatedBy())
                                .updatedTime(new Date())
                                .createdTime(new Date()).build();
                        Map<String, Object> spatialInfo = new HashMap<>();
                        metadataVo.setSpatialInfo(spatialInfo);
                        List<MmField> mmFields = tableMetadataInfo.getFieldMetadataInfos().stream().map(item->DatamgtCommonUtils.convert2MmField(item,sqlType->datasourceType.getFieldType(sqlType).getCode())).collect(Collectors.toList());
                        spatialInfo.put("fields", mmFields);
                        re.add(metadataVo);
                    }
                    return re;
                });
    }


}

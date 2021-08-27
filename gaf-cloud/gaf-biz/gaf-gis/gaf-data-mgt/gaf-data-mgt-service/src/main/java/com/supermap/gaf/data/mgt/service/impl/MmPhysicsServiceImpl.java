/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.data.*;
import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.data.mgt.conversion.ConversionConfig;
import com.supermap.gaf.data.mgt.entity.MmField;
import com.supermap.gaf.data.mgt.entity.MmModel;
import com.supermap.gaf.data.mgt.entity.MmPhysics;
import com.supermap.gaf.data.mgt.entity.MmTable;
import com.supermap.gaf.data.mgt.entity.vo.MmPhysicsVO;
import com.supermap.gaf.data.mgt.enums.DatasourceTypeEnum;
import com.supermap.gaf.data.mgt.mapper.MmPhysicsMapper;
import com.supermap.gaf.data.mgt.model.PhysicsResult;
import com.supermap.gaf.data.mgt.model.PhysicsSingleResult;
import com.supermap.gaf.data.mgt.service.*;
import com.supermap.gaf.data.mgt.support.ConvertHelper;
import com.supermap.gaf.data.mgt.support.JdbcConnectionInfo;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.util.PrjCoordSysUtil;
import com.supermap.gaf.data.mgt.vo.MmFieldSelectVo;
import com.supermap.gaf.data.mgt.vo.MmPhysicsSelectVo;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 物理服务实现类
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Service
public class MmPhysicsServiceImpl implements MmPhysicsService{
    
	private static final Logger  log = LoggerFactory.getLogger(MmPhysicsServiceImpl.class);
	
	@Autowired
    private MmPhysicsMapper mmPhysicsMapper;

	@Autowired
	private MmModelService mmModelService;

	@Autowired
	private MmTableService mmTableService;

    @Autowired
	private MmFieldService mmFieldService;

    @Autowired
    private SysResourceDatasourceService sysResourceDatasourceService;

    @Autowired
    private ConvertHelper convertHelper;
	
	@Override
    public MmPhysics getById(String physicsId){
        if(physicsId == null){
            throw new IllegalArgumentException("physicsId不能为空");
        }
        return  mmPhysicsMapper.select(physicsId);
    }

    @Override
    public Page<MmPhysicsVO> listWithDetail(MmPhysicsSelectVo mmPhysicsSelectVo, Integer pageNum, Integer pageSize) {
        PageInfo<MmPhysics> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            mmPhysicsMapper.selectList(mmPhysicsSelectVo);
        });
        List<MmPhysics> mmPhysicsList = pageInfo.getList();
        if (mmPhysicsList.isEmpty()) {
            List<MmPhysicsVO> result= Collections.emptyList();
            return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),result);
        }
        Set<String> datasourceIds = mmPhysicsList.stream().map(MmPhysics::getDatasourceId).collect(Collectors.toSet());
        List<SysResourceDatasource> sysResourceDatasources = sysResourceDatasourceService.listByIds(datasourceIds);
        Map<String, SysResourceDatasource> datasourceMap = sysResourceDatasources.stream().collect(Collectors.toMap(SysResourceDatasource::getDatasourceId, sysResourceDatasource -> sysResourceDatasource));
        List<MmPhysicsVO> result = mmPhysicsList.stream().map(mmPhysics -> {
            MmPhysicsVO mmPhysicsVO = new MmPhysicsVO();
            BeanUtils.copyProperties(mmPhysics, mmPhysicsVO);
            SysResourceDatasource datasource = datasourceMap.get(mmPhysics.getDatasourceId());
            if (datasource != null) {
                mmPhysicsVO.setDsName(datasource.getDsName());
                mmPhysicsVO.setAddr(datasource.getAddr());
                mmPhysicsVO.setDbName(datasource.getDbName());
                mmPhysicsVO.setIsSdx(datasource.getIsSdx());
                mmPhysicsVO.setIsTemplate(datasource.getIsTemplate());
                mmPhysicsVO.setTypeCode(datasource.getTypeCode());
            }
            return mmPhysicsVO;
        }).collect(Collectors.toList());
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),result);
    }

    @Override
    public Page<MmPhysics> listByPageCondition(MmPhysicsSelectVo mmPhysicsSelectVo, int pageNum, int pageSize) {
        PageInfo<MmPhysics> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            mmPhysicsMapper.selectList(mmPhysicsSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    @Override
    public Page<MmPhysics> listByModelId(String modelId, int pageNum, int pageSize) {
        PageInfo<MmPhysics> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            mmPhysicsMapper.listByModelId(modelId);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    @Override
    public MmPhysics insertMmPhysics(MmPhysics mmPhysics){
        // 主键非GeneratedKey，此处添加自定义主键生成策略
		mmPhysics.setPhysicsId(UUID.randomUUID().toString());
		
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		mmPhysics.setCreatedBy(shiroUser.getAuthUser().getName());
		mmPhysics.setUpdatedBy(shiroUser.getAuthUser().getName());
        mmPhysicsMapper.insert(mmPhysics);
        return mmPhysics;
    }
	
	@Override
    public void batchInsert(List<MmPhysics> mmPhysicss){
		if (mmPhysicss != null && mmPhysicss.size() > 0) {
	        mmPhysicss.forEach(mmPhysics -> {
				mmPhysics.setPhysicsId(UUID.randomUUID().toString());
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				mmPhysics.setCreatedBy(shiroUser.getAuthUser().getName());
				mmPhysics.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            mmPhysicsMapper.batchInsert(mmPhysicss);
        }
        
    }
	
	@Override
    public void deleteMmPhysics(String physicsId){
        mmPhysicsMapper.delete(physicsId);
    }

	@Override
    public void batchDelete(List<String> physicsIds){
        mmPhysicsMapper.batchDelete(physicsIds);
    }
	
	@Override
    public MmPhysics updateMmPhysics(MmPhysics mmPhysics){
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		mmPhysics.setUpdatedBy(shiroUser.getAuthUser().getName());
		mmPhysicsMapper.update(mmPhysics);
        return mmPhysics;
    }

    @Override
    public PhysicsResult physicalization(List<MmPhysics> mmPhysicsList) {
        if (mmPhysicsList == null || mmPhysicsList.size() == 0) {
            throw new IllegalArgumentException("批量物理化参数不能为空");
        }
        PhysicsResult physicsResult = new PhysicsResult();
        List<PhysicsSingleResult> successed = new LinkedList<>();
        List<PhysicsSingleResult> failed = new LinkedList<>();
        for (MmPhysics mmPhysics : mmPhysicsList) {
            PhysicsSingleResult physicsSingleResult = new PhysicsSingleResult();
            try {
                physicalizationSingle(mmPhysics);
                physicsSingleResult.setSuccess(true);
                successed.add(physicsSingleResult);
            } catch (Exception e) {
                physicsSingleResult.setSuccess(false);
                physicsSingleResult.setMessage(e.getMessage());
                failed.add(physicsSingleResult);
            }
            physicsSingleResult.setMmPhysics(mmPhysics);
        }
        physicsResult.setSuccessed(successed);
        physicsResult.setFailed(failed);
        return physicsResult;
    }

    private void physicalizationSingle(MmPhysics mmPhysics) {
	    // 检查是否已经物理化
        MmTable mmTable = mmTableService.getById(mmPhysics.getTableId());
        MmModel mmModel = mmModelService.getById(mmTable.getModelId());
        MmFieldSelectVo mmFieldSelectVo = new MmFieldSelectVo();
        mmFieldSelectVo.setTableId(mmPhysics.getTableId());
        mmFieldSelectVo.setOrderFieldName("sort_sn");
        mmFieldSelectVo.setOrderMethod("asc");
        List<MmField> mmFields = mmFieldService.selectList(mmFieldSelectVo);
        if (StringUtils.isEmpty(mmPhysics.getPhysicsName())) {
            mmPhysics.setPhysicsName(mmTable.getTableName());
        }
        String name =  mmPhysics.getPhysicsName();
        SysResourceDatasource sysResourceDatasource = sysResourceDatasourceService.getById(mmPhysics.getDatasourceId());
        if (sysResourceDatasource == null) {
            throw new IllegalArgumentException("数据源不存在");
        }
        if ("sdx".equals(mmModel.getModelType())) {
            // 空间模型
            String sdxInfoJson = mmTable.getSdxInfo();
            if (StringUtils.isEmpty(sdxInfoJson)) {
                throw new IllegalArgumentException("空间模型下的逻辑表"+ name+"对应的数据集属性信息不能为空");
            }
            JSONObject sdxInfoJO = JSONObject.parseObject(sdxInfoJson);
            String datasetTypeStr = sdxInfoJO.getString("type");
            if (StringUtils.isEmpty(datasetTypeStr)) {
                throw new IllegalArgumentException("空间模型下的逻辑表"+ name+"对应的数据集信息中数据集类型不能为空");
            }
            DatasetType datasetType = (DatasetType) DatasetType.parse(DatasetType.class, datasetTypeStr);
            DatasourceConnectionInfo connectionInfo = convertHelper.conver2DatasourceConnectionInfo(sysResourceDatasource);
            Workspace workspace = null;
            Datasource datasource = null;
            try {
                workspace = new Workspace();
                datasource = workspace.getDatasources().open(connectionInfo);
                Datasets datasets = datasource.getDatasets();

                if (DatasetType.GRID.equals(datasetType)) {
                    // 栅格
                    DatasetGridInfo datasetGridInfo = JSON.parseObject(sdxInfoJson,DatasetGridInfo.class, ConversionConfig.getParseConfig());
                    datasetGridInfo.setName(name);
                    DatasetGrid datasetGrid = datasets.create(datasetGridInfo);
                } else if (DatasetType.IMAGE.equals(datasetType) || DatasetType.WCS.equals(datasetType)|| DatasetType.WMS.equals(datasetType)) {
                    DatasetImageInfo datasetImageInfo = JSON.parseObject(sdxInfoJson, DatasetImageInfo.class, ConversionConfig.getParseConfig());
                    datasetImageInfo.setName(name);
                    DatasetImage datasetImage = datasets.create(datasetImageInfo);
                } else if (DatasetType.TOPOLOGY.equals(datasetType)) {
                    DatasetTopologyInfo datasetTopologyInfo = JSON.parseObject(sdxInfoJson, DatasetTopologyInfo.class, ConversionConfig.getParseConfig());
                    datasetTopologyInfo.setName(name);
                    DatasetTopology datasetTopology = datasets.create(datasetTopologyInfo);
                } else if (DatasetType.MOSAIC.equals(datasetType)) {
                    String prjCoordSysStr = sdxInfoJO.getString("prjCoordSys");
                    PrjCoordSys prjCoordSys = PrjCoordSysUtil.parse(prjCoordSysStr);
                    datasets.createDatasetMosaic(name,prjCoordSys);
                } else if (DatasetType.VOLUME.equals(datasetType)) {
                    DatasetVolumeInfo datasetTopologyInfo = JSON.parseObject(sdxInfoJson, DatasetVolumeInfo.class, ConversionConfig.getParseConfig());
                    datasetTopologyInfo.setName(name);
                    DatasetVolume datasetVolume = datasets.create(datasetTopologyInfo);
                } else {
                    // 矢量
                    DatasetVectorInfo datasetVectorInfo = JSON.parseObject(sdxInfoJson, DatasetVectorInfo.class, ConversionConfig.getParseConfig());
                    datasetVectorInfo.setName(name);
                    String prjCoordSysStr = sdxInfoJO.getString("prjCoordSys");
                    PrjCoordSys prjCoordSys = PrjCoordSysUtil.parse(prjCoordSysStr);
                    DatasetVector datasetVector = datasets.create(datasetVectorInfo, prjCoordSys);
                    datasetVector.open();
                    FieldInfos fieldInfos = datasetVector.getFieldInfos();
                    int size = mmFields.size();
                    if (size > 0) {
                        FieldInfo[] fieldInfosArray = new FieldInfo[size];
                        for (int i = 0; i < size; i++) {
                            MmField mmField = mmFields.get(i);
                            FieldInfo fieldInfo = new FieldInfo();
                            fieldInfo.setName(mmField.getFieldName());
                            fieldInfo.setCaption(mmField.getFieldAlias());
                            FieldType fieldType = (FieldType) FieldType.parse(FieldType.class, mmField.getFieldType().replace("sdx_", "").toUpperCase());
                            fieldInfo.setType(fieldType);
                            fieldInfo.setDefaultValue(mmField.getFieldDefault());
                            fieldInfo.setRequired(mmField.getFieldNotNull());
                            fieldInfo.setMaxLength(mmField.getFieldLength());
                            fieldInfosArray[i] = fieldInfo;
                        }
                        fieldInfos.addRange(fieldInfosArray);
                    }
                    datasetVector.close();
                }

            } finally {
                if (datasource != null) {
                    datasource.close();
                }
                if (workspace != null) {
                    workspace.close();
                    workspace.dispose();
                }
            }


        } else {
            DatasourceTypeEnum datasourceType = DatasourceTypeEnum.fromCode(mmModel.getModelType());
            if (mmFields.size() == 0) {
                throw new IllegalArgumentException("字段数量不能为0");
            }
            List<String> ddlFragments = mmFields.stream().sorted(Comparator.comparingInt(MmField::getSortSn)).map(datasourceType::convertToDdlFragment).collect(Collectors.toList());
            List<String> primaryKeys = mmFields.stream().filter(MmField::getFieldPrimaryKey).map(MmField::getFieldName).collect(Collectors.toList());

            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE " + name + " (");
            sb.append(String.join("," , ddlFragments));
            if (primaryKeys.size() > 0) {
                sb.append(", PRIMARY KEY (").append(String.join(",",primaryKeys)).append(")");
            }
            sb.append(");");
            String ddl = sb.toString();

            JdbcConnectionInfo jdbcConn = datasourceType.convert2JdbcConnectionInfo(sysResourceDatasource);
            try {
                Class.forName(jdbcConn.getDriverClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new GafException("数据库驱动加载失败"+ e.getMessage(),e);
            }
            try(Connection connection = DriverManager.getConnection(jdbcConn.getUrl(), jdbcConn.getUsername(), jdbcConn.getPassword());
                Statement statement = connection.createStatement()) {
                statement.executeUpdate(ddl);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                throw new GafException("创建表失败,原因:" + sqlException.getMessage() ,sqlException);
            }
        }
        insertMmPhysics(mmPhysics);
    }

}

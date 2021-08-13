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
import com.supermap.gaf.data.mgt.enums.DatasourceTypeEnum;
import com.supermap.gaf.data.mgt.mapper.MmPhysicsMapper;
import com.supermap.gaf.data.mgt.service.MmFieldService;
import com.supermap.gaf.data.mgt.service.MmModelService;
import com.supermap.gaf.data.mgt.service.MmPhysicsService;
import com.supermap.gaf.data.mgt.service.MmTableService;
import com.supermap.gaf.data.mgt.support.ConvertHelper;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmFieldSelectVo;
import com.supermap.gaf.data.mgt.vo.MmPhysicsSelectVo;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

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
    private ConvertHelper convertHelper;
	
	@Override
    public MmPhysics getById(String physicsId){
        if(physicsId == null){
            throw new IllegalArgumentException("physicsId不能为空");
        }
        return  mmPhysicsMapper.select(physicsId);
    }
	
	@Override
    public Page<MmPhysics> listByPageCondition(MmPhysicsSelectVo mmPhysicsSelectVo, int pageNum, int pageSize) {
        PageInfo<MmPhysics> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            mmPhysicsMapper.selectList(mmPhysicsSelectVo);
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
    public void physicalization(List<MmPhysics> mmPhysicsList) {




    }

    private void physicalizationSingle(MmPhysics mmPhysics) {
	    // 检查是否已经物理化
        MmTable mmTable = mmTableService.getById(mmPhysics.getTableId());
        MmModel mmModel = mmModelService.getById(mmTable.getModelId());
        MmFieldSelectVo mmFieldSelectVo = new MmFieldSelectVo();
        mmFieldSelectVo.setTableId(mmPhysics.getTableId());
        List<MmField> mmFields = mmFieldService.selectList(mmFieldSelectVo);

        String name = StringUtils.isEmpty(mmPhysics.getPhysicsName()) ?  mmTable.getTableName():mmPhysics.getPhysicsName();
        if ("sdx".equals(mmModel.getModelCode())) {
            // 空间模型
            String sdxInfoJson = mmTable.getSdxInfo();
            JSONObject sdxInfoJO = JSONObject.parseObject(sdxInfoJson);
            String datasetTypeStr = sdxInfoJO.getString("type");
            DatasetType datasetType = (DatasetType) DatasetType.parse(DatasetType.class, datasetTypeStr);

            SysResourceDatasource sysResourceDatasource = convert(mmPhysics);
            DatasourceConnectionInfo connectionInfo = convertHelper.conver2DatasourceConnectionInfo(sysResourceDatasource);
            Workspace workspace = new Workspace();
            Datasource datasource = workspace.getDatasources().open(connectionInfo);
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
                Integer prj = sdxInfoJO.getInteger("prjCoordSys");
                datasets.createDatasetMosaic(name,prj == null? null : PrjCoordSys.fromEPSG(prj) );
            } else if (DatasetType.VOLUME.equals(datasetType)) {
                DatasetVolumeInfo datasetTopologyInfo = JSON.parseObject(sdxInfoJson, DatasetVolumeInfo.class, ConversionConfig.getParseConfig());
                datasetTopologyInfo.setName(name);
                DatasetVolume datasetVolume = datasets.create(datasetTopologyInfo);
            } else {
                // 矢量
                DatasetVectorInfo datasetVectorInfo = JSON.parseObject(sdxInfoJson, DatasetVectorInfo.class, ConversionConfig.getParseConfig());
                datasetVectorInfo.setName(name);
                Integer prj = sdxInfoJO.getInteger("prjCoordSys");
                DatasetVector datasetVector;
                if (prj != null) {
                    datasetVector = datasets.create(datasetVectorInfo, PrjCoordSys.fromEPSG(prj));
                } else {
                    datasetVector = datasets.create(datasetVectorInfo);
                }
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
                        fieldInfo.setDefaultValue(fieldInfo.getDefaultValue());
                        fieldInfo.setRequired(mmField.getFieldNotNull());
                        fieldInfo.setMaxLength(mmField.getFieldLength());
                        fieldInfosArray[i] = fieldInfo;
                    }
                    fieldInfos.addRange(fieldInfosArray);
                }
                datasetVector.close();
            }
        } else {
            DatasourceTypeEnum datasourceType = DatasourceTypeEnum.fromCode(mmModel.getModelCode());




        }




    }

    private SysResourceDatasource convert(MmPhysics mmPhysics) {
        SysResourceDatasource datasource = new SysResourceDatasource();
        datasource.setAddr(mmPhysics.getServer());
        datasource.setDbName(mmPhysics.getDbName());
        datasource.setIsSdx(true);
        datasource.setDsName(UUID.randomUUID().toString());
        datasource.setUserName(mmPhysics.getUsername());
        datasource.setPassword(mmPhysics.getPassword());
        datasource.setTypeCode(mmPhysics.getType());
        return datasource;
    }





}

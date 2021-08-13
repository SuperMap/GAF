package com.supermap.gaf.data.mgt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.data.mgt.entity.MmField;
import com.supermap.gaf.data.mgt.enums.DatasourceTypeEnum;
import com.supermap.gaf.data.mgt.enums.SdxFieldTypeEnum;
import com.supermap.gaf.data.mgt.mapper.MmFieldMapper;
import com.supermap.gaf.data.mgt.model.FieldTypeInfo;
import com.supermap.gaf.data.mgt.service.MmFieldService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmFieldSelectVo;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 字段服务实现类
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Service
public class MmFieldServiceImpl implements MmFieldService{
    
	private static final Logger  log = LoggerFactory.getLogger(MmFieldServiceImpl.class);
	
	@Autowired
    private MmFieldMapper mmFieldMapper;
	
	@Override
    public MmField getById(String fieldId){
        if(fieldId == null){
            throw new IllegalArgumentException("fieldId不能为空");
        }
        return  mmFieldMapper.select(fieldId);
    }
    public List<MmField> selectList(MmFieldSelectVo mmFieldSelectVo) {
        return mmFieldMapper.selectList(mmFieldSelectVo);
    }

	@Override
    public Page<MmField> listByPageCondition(MmFieldSelectVo mmFieldSelectVo, int pageNum, int pageSize) {
        PageInfo<MmField> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            mmFieldMapper.selectList(mmFieldSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

	@Override
    public MmField insertMmField(MmField mmField){
        // 主键非GeneratedKey，此处添加自定义主键生成策略
		mmField.setFieldId(UUID.randomUUID().toString());
		
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		mmField.setCreatedBy(shiroUser.getAuthUser().getName());
		mmField.setUpdatedBy(shiroUser.getAuthUser().getName());
        mmFieldMapper.insert(mmField);
        return mmField;
    }
	
	@Override
    public void batchInsert(List<MmField> mmFields){
		if (mmFields != null && mmFields.size() > 0) {
	        mmFields.forEach(mmField -> {
				mmField.setFieldId(UUID.randomUUID().toString());
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				mmField.setCreatedBy(shiroUser.getAuthUser().getName());
				mmField.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            mmFieldMapper.batchInsert(mmFields);
        }
        
    }
	
	@Override
    public void deleteMmField(String fieldId){
        mmFieldMapper.delete(fieldId);
    }

	@Override
    public void batchDelete(List<String> fieldIds){
        mmFieldMapper.batchDelete(fieldIds);
    }
	
	@Override
    public MmField updateMmField(MmField mmField){
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		mmField.setUpdatedBy(shiroUser.getAuthUser().getName());
		mmFieldMapper.update(mmField);
        return mmField;
    }

    @Override
    public List<FieldTypeInfo> listTypeInfos(String modelType) {
        if ("sdx".equals(modelType)) {
            return SdxFieldTypeEnum.toFieldTypeInfoList();
        } else {
            DatasourceTypeEnum datasourceTypeEnum = DatasourceTypeEnum.fromCode(modelType);
            return datasourceTypeEnum.getFieldTypes();
        }
    }

}

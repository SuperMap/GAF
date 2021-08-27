package com.supermap.gaf.data.mgt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.data.mgt.entity.MmFieldAssociate;
import com.supermap.gaf.data.mgt.mapper.MmFieldAssociateMapper;
import com.supermap.gaf.data.mgt.service.MmFieldAssociateService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmFieldAssociateSelectVo;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 字段关联服务实现类
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Service
public class MmFieldAssociateServiceImpl implements MmFieldAssociateService{
    
	private static final Logger  log = LoggerFactory.getLogger(MmFieldAssociateServiceImpl.class);
	
	@Autowired
    private MmFieldAssociateMapper mmFieldAssociateMapper;
	
	@Override
    public MmFieldAssociate getById(String fieldAssociateId){
        if(fieldAssociateId == null){
            throw new IllegalArgumentException("fieldAssociateId不能为空");
        }
        return  mmFieldAssociateMapper.select(fieldAssociateId);
    }
	
	@Override
    public Page<MmFieldAssociate> listByPageCondition(MmFieldAssociateSelectVo mmFieldAssociateSelectVo, int pageNum, int pageSize) {
        PageInfo<MmFieldAssociate> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            mmFieldAssociateMapper.selectList(mmFieldAssociateSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    @Override
    public List<MmFieldAssociate> selectList(MmFieldAssociateSelectVo mmFieldAssociateSelectVo) {
        return mmFieldAssociateMapper.selectList(mmFieldAssociateSelectVo);
    }

    @Override
    public MmFieldAssociate insertMmFieldAssociate(MmFieldAssociate mmFieldAssociate){
        // 主键非GeneratedKey，此处添加自定义主键生成策略
		mmFieldAssociate.setFieldAssociateId(UUID.randomUUID().toString());
		
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		mmFieldAssociate.setCreatedBy(shiroUser.getAuthUser().getName());
		mmFieldAssociate.setUpdatedBy(shiroUser.getAuthUser().getName());
        mmFieldAssociateMapper.insert(mmFieldAssociate);
        return mmFieldAssociate;
    }
	
	@Override
    public void batchInsert(List<MmFieldAssociate> mmFieldAssociates){
		if (mmFieldAssociates != null && mmFieldAssociates.size() > 0) {
	        mmFieldAssociates.forEach(mmFieldAssociate -> {
				mmFieldAssociate.setFieldAssociateId(UUID.randomUUID().toString());
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				mmFieldAssociate.setCreatedBy(shiroUser.getAuthUser().getName());
				mmFieldAssociate.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            mmFieldAssociateMapper.batchInsert(mmFieldAssociates);
        }
        
    }
	
	@Override
    public void deleteMmFieldAssociate(String fieldAssociateId){
        mmFieldAssociateMapper.delete(fieldAssociateId);
    }

	@Override
    public void batchDelete(List<String> fieldAssociateIds){
        mmFieldAssociateMapper.batchDelete(fieldAssociateIds);
    }
	
	@Override
    public MmFieldAssociate updateMmFieldAssociate(MmFieldAssociate mmFieldAssociate){
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		mmFieldAssociate.setUpdatedBy(shiroUser.getAuthUser().getName());
		mmFieldAssociateMapper.update(mmFieldAssociate);
        return mmFieldAssociate;
    }

    @Transactional
    @Override
    public void refreshMmFieldAssociateByTables(String sourceFieldTableId,String targetFieldTableId,List<MmFieldAssociate> mmFieldAssociates) {
	    //验证都是两表间的数据
        if (!CollectionUtils.isEmpty(mmFieldAssociates)){
            mmFieldAssociates.forEach(mmFieldAssociate -> {
                if (!sourceFieldTableId.equals(mmFieldAssociate.getSourceFieldTableId()) || !targetFieldTableId.equals(mmFieldAssociate.getTargetFieldTableId())){
                    throw new IllegalArgumentException("传入的字段关联数据所属表参数不一致");
                }
            });
        }
        //查询两表已有的字段关联
        MmFieldAssociateSelectVo mmFieldAssociateSelectVo = new MmFieldAssociateSelectVo();
        mmFieldAssociateSelectVo.setSourceFieldTableId(sourceFieldTableId);
        mmFieldAssociateSelectVo.setTargetFieldTableId(targetFieldTableId);
        List<MmFieldAssociate> deleteMmFieldAssociateList = mmFieldAssociateMapper.selectList(mmFieldAssociateSelectVo);
        List<String> deleteFieldAssociateIds = deleteMmFieldAssociateList.stream().map(MmFieldAssociate::getFieldAssociateId).collect(Collectors.toList());
        //删除已有关联
        if (!CollectionUtils.isEmpty(deleteFieldAssociateIds)){
            batchDelete(deleteFieldAssociateIds);
        }
        //批量添加
        if (!CollectionUtils.isEmpty(mmFieldAssociates)){
            batchInsert(mmFieldAssociates);
        }
    }

}

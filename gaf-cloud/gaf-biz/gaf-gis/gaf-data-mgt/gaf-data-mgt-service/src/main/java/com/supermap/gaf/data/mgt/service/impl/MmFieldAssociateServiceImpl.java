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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
    
}

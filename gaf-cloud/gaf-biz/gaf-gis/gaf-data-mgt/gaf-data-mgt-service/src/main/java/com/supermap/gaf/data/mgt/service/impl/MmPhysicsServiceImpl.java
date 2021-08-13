package com.supermap.gaf.data.mgt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.data.mgt.entity.MmPhysics;
import com.supermap.gaf.data.mgt.mapper.MmPhysicsMapper;
import com.supermap.gaf.data.mgt.service.MmPhysicsService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmPhysicsSelectVo;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    
}

package com.supermap.gaf.data.mgt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.data.mgt.entity.MmTable;
import com.supermap.gaf.data.mgt.mapper.MmTableMapper;
import com.supermap.gaf.data.mgt.service.MmTableService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmTableSelectVo;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 逻辑服务实现类
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Service
public class MmTableServiceImpl implements MmTableService{
    
	private static final Logger  log = LoggerFactory.getLogger(MmTableServiceImpl.class);
	
	@Autowired
    private MmTableMapper mmTableMapper;
	
	@Override
    public MmTable getById(String tableId){
        if(tableId == null){
            throw new IllegalArgumentException("tableId不能为空");
        }
        return  mmTableMapper.select(tableId);
    }
	
	@Override
    public Page<MmTable> listByPageCondition(MmTableSelectVo mmTableSelectVo, int pageNum, int pageSize) {
        PageInfo<MmTable> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            mmTableMapper.selectList(mmTableSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

	@Override
    public MmTable insertMmTable(MmTable mmTable){
        // 主键非GeneratedKey，此处添加自定义主键生成策略
		mmTable.setTableId(UUID.randomUUID().toString());
		
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		mmTable.setCreatedBy(shiroUser.getAuthUser().getName());
		mmTable.setUpdatedBy(shiroUser.getAuthUser().getName());
        mmTableMapper.insert(mmTable);
        return mmTableMapper.select(mmTable.getTableId());
    }
	
	@Override
    public void batchInsert(List<MmTable> mmTables){
		if (mmTables != null && mmTables.size() > 0) {
	        mmTables.forEach(mmTable -> {
				mmTable.setTableId(UUID.randomUUID().toString());
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				mmTable.setCreatedBy(shiroUser.getAuthUser().getName());
				mmTable.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            mmTableMapper.batchInsert(mmTables);
        }
        
    }
	
	@Override
    public MmTable deleteMmTable(String tableId){
        MmTable mmTable = mmTableMapper.select(tableId);
        mmTableMapper.delete(tableId);
        return mmTable;
    }

	@Override
    public void batchDelete(List<String> tableIds){
        mmTableMapper.batchDelete(tableIds);
    }
	
	@Override
    public MmTable updateMmTable(MmTable mmTable){
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		mmTable.setUpdatedBy(shiroUser.getAuthUser().getName());
		mmTableMapper.update(mmTable);
        return mmTableMapper.select(mmTable.getTableId());
    }
    
}

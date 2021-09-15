package com.supermap.gaf.webgis.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.webgis.dao.ServiceSourceMapper;
import com.supermap.gaf.webgis.entity.ServiceSource;
import com.supermap.gaf.webgis.service.ServiceSourceService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.ServiceSourceSelectVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 服务来源关联表服务实现类
 * @author zrc
 * @date yyyy-mm-dd
 */
@Service
public class ServiceSourceServiceImpl implements ServiceSourceService {
    
	private static final Logger  log = LoggerFactory.getLogger(ServiceSourceServiceImpl.class);
	
	@Autowired
    private ServiceSourceMapper serviceSourceMapper;
	

	@Override
    public ServiceSource getById(String serviceSourceId){
        if(serviceSourceId == null){
            throw new IllegalArgumentException("serviceSourceId不能为空");
        }
        return serviceSourceMapper.select(serviceSourceId);
    }
	
	@Override
    public Page<ServiceSource> listByPageCondition(ServiceSourceSelectVo serviceSourceSelectVo, int pageNum, int pageSize) {
        PageInfo<ServiceSource> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            serviceSourceMapper.selectList(serviceSourceSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }


	@Override
    public ServiceSource insertServiceSource(ServiceSource serviceSource){
	    serviceSource.setServiceSourceId(UUID.randomUUID().toString());
        serviceSourceMapper.insert(serviceSource);
        return serviceSource;
    }
	
	@Override
    public void batchInsert(List<ServiceSource> serviceSources){
		if (serviceSources != null && serviceSources.size() > 0) {
	        serviceSources.forEach(serviceSource -> {
				serviceSource.setServiceSourceId(UUID.randomUUID().toString());
            });
            serviceSourceMapper.batchInsert(serviceSources);
        }
        
    }
	

	@Override
    public void deleteServiceSource(String serviceSourceId){
        serviceSourceMapper.delete(serviceSourceId);
    }

	@Override
    public void batchDelete(List<String> serviceSourceIds){
        serviceSourceMapper.batchDelete(serviceSourceIds);
    }
	

	@Override
    public ServiceSource updateServiceSource(ServiceSource serviceSource){
        serviceSourceMapper.update(serviceSource);
        return serviceSource;
    }
    
}

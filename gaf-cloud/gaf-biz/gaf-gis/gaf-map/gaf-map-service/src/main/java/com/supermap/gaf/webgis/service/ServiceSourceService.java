package com.supermap.gaf.webgis.service;

import com.supermap.gaf.webgis.entity.ServiceSource;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.ServiceSourceSelectVo;

import java.util.List;

/**
 * 服务来源关联表服务类
 * @author zrc 
 * @date yyyy-mm-dd
 */
public interface ServiceSourceService {
	
	/**
    * 根据id查询服务来源关联表
    * @return
    */
    ServiceSource getById(String serviceSourceId);
	
	/**
     * 分页条件查询
     * @param serviceSourceSelectVo 查询条件
     * @param pageNum 当前页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
	Page<ServiceSource> listByPageCondition(ServiceSourceSelectVo serviceSourceSelectVo, int pageNum, int pageSize);
	
	
    /**
    * 新增服务来源关联表
    * @return 新增的serviceSource
    */
    ServiceSource insertServiceSource(ServiceSource serviceSource);
	
	/**
    * 批量插入
    * 
    **/
    void batchInsert(List<ServiceSource> serviceSources);

    /**
    * 删除服务来源关联表
    * 
    */
    void deleteServiceSource(String serviceSourceId);

    /**
    * 批量删除
    * 
    **/
    void batchDelete(List<String> serviceSourceIds);
    /**
    * 更新服务来源关联表
    * @return 更新后的serviceSource
    */
    ServiceSource updateServiceSource(ServiceSource serviceSource);
    
}

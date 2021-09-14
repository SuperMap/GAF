package com.supermap.gaf.data.mgt.mapper;

import com.supermap.gaf.data.mgt.entity.ServiceSource;
import com.supermap.gaf.data.mgt.vo.ServiceSourceSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务来源关联表数据访问类
 * @author zrc 
 * @date yyyy-mm-dd
 */
@Mapper
@Component
public interface ServiceSourceMapper{
	/**
     * 根据主键 serviceSourceId查询
     * 
	 */
    ServiceSource select(@Param("serviceSourceId")String serviceSourceId);
	
	/**
     * 多条件查询
     * @param serviceSourceSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
	List<ServiceSource> selectList(ServiceSourceSelectVo serviceSourceSelectVo);

    /**
	 * 新增
	 * 
	 */
    int insert(ServiceSource serviceSource);
	
	/**
     * 批量插入
     * 
	 */
    int batchInsert(List<ServiceSource> serviceSources);
	
	/**
     * 批量删除
     *
	 */
    int batchDelete(List<String> serviceSourceIds);

	/**
     * 刪除
     * 
	 */
    int delete(@Param("serviceSourceId")String serviceSourceId);
    /**
     * 更新
     *
	 */
    int update(ServiceSource serviceSource);
}

package com.supermap.gaf.storage.dao;
import com.supermap.gaf.storage.entity.S3Server;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据访问类
 * @author zrc 
 * @date yyyy-mm-dd
 */
@Mapper
@Component
public interface S3ServerMapper{
	/**
     * 根据主键 id查询
     * 
	 */
    S3Server select(@Param("id") String id);
	
	/**
     * 多条件查询
     * @param S3Server 查询条件
     * @return 若未查询到则返回空集合
     */
	List<S3Server> selectList(S3Server S3Server);

    /**
	 * 新增
	 * 
	 */
    int insert(S3Server s3Server);
	
	/**
     * 批量插入
     * 
	 */
    int batchInsert(List<S3Server> s3Servers);
	
	/**
     * 批量删除
     *
	 */
    int batchDelete(List<String> ids);

	/**
     * 刪除
     * 
	 */
    int delete(@Param("id") String id);
    /**
     * 更新
     *
	 */
    int update(S3Server s3Server);
}

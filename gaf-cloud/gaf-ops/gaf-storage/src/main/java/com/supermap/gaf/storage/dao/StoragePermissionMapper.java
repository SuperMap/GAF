package com.supermap.gaf.storage.dao;

import com.supermap.gaf.storage.entity.Permission;
import com.supermap.gaf.storage.entity.vo.StoragePermissionSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

;

/**
 * 数据访问类
 *
 * @author zrc
 * @date yyyy-mm-dd
 */
@Mapper
@Component
public interface StoragePermissionMapper {
    /**
     * 根据主键 id查询
     */
    Permission select(@Param("id") String id);

    /**
     * 多条件查询
     *
     * @param storagePermissionSelectVo 查询条件
     * @return 若未查询到则返回空集合
     */
    List<Permission> selectList(StoragePermissionSelectVo storagePermissionSelectVo);

    /**
     * @param owers
     * @return
     */
    List<Permission> selectByOwersAndConfigName(@Param("configName") String configName, @Param("list") Collection<String> owers);

    /**
     * 新增
     */
    int insert(Permission permission);

    /**
     * 批量插入
     */
    int batchInsert(List<Permission> permissions);

    /**
     * 批量删除
     */
    int batchDelete(List<String> ids);

    int deleteByOwer(@Param("ower") String ower);

    int deleteByOwerAndParentResourcesNotResource(@Param("ower") String ower, @Param("resource") String resource, @Param("list") Collection<String> parentResources);

    int deleteByOwerAndResource(@Param("ower") String ower, @Param("resource") String resource);

    /**
     * 刪除
     */
    int delete(@Param("id") String id);

    /**
     * 更新
     */
    int update(Permission permission);
}

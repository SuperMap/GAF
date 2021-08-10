package com.supermap.gaf.storage.service;

import com.supermap.gaf.common.storage.entity.ObjectInfo;
import com.supermap.gaf.storage.entity.Permission;
import com.supermap.gaf.storage.entity.vo.StoragePermissionSelectVo;
import com.supermap.gaf.storage.utils.Page;

import java.util.List;

/**
 * 服务类
 *
 * @author zrc
 * @date yyyy-mm-dd
 */
public interface StoragePermissionService {

    /**
     * 根据id查询
     *
     * @return
     */
    Permission getById(String id);

    /**
     * 分页条件查询
     *
     * @param storagePermissionSelectVo 查询条件
     * @param pageNum                   当前页码
     * @param pageSize                  每页数量
     * @return 分页对象
     */
    Page<Permission> listByPageCondition(StoragePermissionSelectVo storagePermissionSelectVo, int pageNum, int pageSize);


    /**
     * 新增
     *
     * @return 新增的storagePermission
     */
    Permission insertStoragePermission(Permission permission);

    /**
     * 批量插入
     **/
    void batchInsert(List<Permission> permissions);

    /**
     * 删除
     */
    void deleteStoragePermission(String id);

    /**
     * 批量删除
     **/
    void batchDelete(List<String> ids);

    /**
     * 更新
     *
     * @return 更新后的storagePermission
     */
    Permission updateStoragePermission(Permission permission);

    int updateOwerStoragePermission(List<Permission> permission, String ower);

    int deleteOwerStoragePermission(String ower);

    List<ObjectInfo> listObjectsWithOwerPermissions(String ower, String configName, String prefix);
}

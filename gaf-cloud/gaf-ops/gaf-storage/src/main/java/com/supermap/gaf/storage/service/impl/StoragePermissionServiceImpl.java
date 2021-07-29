package com.supermap.gaf.storage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.common.storage.entity.ObjectInfo;
import com.supermap.gaf.common.storage.service.S3ClientService;
import com.supermap.gaf.storage.dao.StoragePermissionMapper;
import com.supermap.gaf.storage.entity.Permission;
import com.supermap.gaf.storage.entity.vo.StoragePermissionSelectVo;
import com.supermap.gaf.storage.enums.PermissionType;
import com.supermap.gaf.storage.service.StoragePermissionService;
import com.supermap.gaf.storage.utils.Page;
import com.supermap.gaf.storage.utils.StorageCommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务实现类
 * @author zrc
 * @date yyyy-mm-dd
 */
@Service
public class StoragePermissionServiceImpl implements StoragePermissionService {
    
	private static final Logger  log = LoggerFactory.getLogger(StoragePermissionServiceImpl.class);
	
	@Autowired
    private StoragePermissionMapper storagePermissionMapper;
    @Autowired
    private S3ClientService s3ClientService;

	@Override
    public Permission getById(String id){
        if(id == null){
            throw new IllegalArgumentException("id不能为空");
        }
        return storagePermissionMapper.select(id);
    }
	
	@Override
    public Page<Permission> listByPageCondition(StoragePermissionSelectVo storagePermissionSelectVo, int pageNum, int pageSize) {
        PageInfo<Permission> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            storagePermissionMapper.selectList(storagePermissionSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }


	@Override
    public Permission insertStoragePermission(Permission permission){
	    injectParentResource(permission);
	    permission.setId(UUID.randomUUID().toString());
        storagePermissionMapper.insert(permission);
        return permission;
    }
	
	@Override
    public void batchInsert(List<Permission> permissions){
		if (permissions != null && permissions.size() > 0) {
            storagePermissionMapper.batchInsert(permissions);
        }
        
    }
	

	@Override
    public void deleteStoragePermission(String id){
        storagePermissionMapper.delete(id);
    }

	@Override
    public void batchDelete(List<String> ids){
        storagePermissionMapper.batchDelete(ids);
    }
	

	@Override
    public Permission updateStoragePermission(Permission permission){
        storagePermissionMapper.update(permission);
        return permission;
    }

    void injectParentResource(Permission permission){
        String resource = permission.getResource();
        if(resource.endsWith("/")){
            resource = resource.substring(0,resource.length()-1);
        }
        int lastSlashIndex = resource.lastIndexOf('/');
        String parentResource = lastSlashIndex==-1?"":resource.substring(0,lastSlashIndex+1);
        permission.setParentResource(parentResource);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateOwerStoragePermission(List<Permission> permission, String ower) {
        List<Permission> all = storagePermissionMapper.selectList(StoragePermissionSelectVo.builder().ower(ower).build());
        if(!CollectionUtils.isEmpty(all)){
            String idTemplate = "%s:%s";
            List<String> deletes = new ArrayList<>();
            List<Permission> inserts = new ArrayList<>();
            Set<String> curUpdates = new HashSet<>();
            for(Permission item: permission){
                curUpdates.add(String.format(idTemplate,item.getConfigName(),item.getResource()));
                if(!StringUtils.isEmpty(item.getScope())){
                    inserts.add(item);
                    injectParentResource(item);
                    item.setId(UUID.randomUUID().toString());
                }
            }
            permission = inserts;
            for(Permission item:all){
                if(curUpdates.contains(String.format(idTemplate,item.getConfigName(),item.getResource()))){
                    deletes.add(item.getId());
                }
            }
            if(deletes.size()>0){
                storagePermissionMapper.batchDelete(deletes);
            }
        }
        if(permission.size()>0){
            return storagePermissionMapper.batchInsert(permission);
        }
        return 0;
	}

    @Override
    public int deleteOwerStoragePermission(String ower) {
        return storagePermissionMapper.deleteByOwer(ower);
    }

    @Override
    public List<ObjectInfo> listObjectsWithOwerPermissions(String ower, String configName, String prefix) {
        List<ObjectInfo> re = this.s3ClientService.listObjects(configName, prefix,false, null);
        if(!CollectionUtils.isEmpty(re)){
            Map<String, ObjectInfo> map = re.stream().collect(Collectors.toMap(ObjectInfo::getName, item->item,(v1, v2)->v1));
            List<Permission> permissions = storagePermissionMapper.selectList(StoragePermissionSelectVo.builder().configName(configName).ower(ower).parentResource(prefix).build());
            if(!CollectionUtils.isEmpty(permissions)) {
                for (Permission permission : permissions) {
                    ObjectInfo objectInfo = map.get(permission.getResource());
                    if (objectInfo != null) {
                        Set<PermissionType> permissionTypes = StorageCommonUtils.splitAndConvert(permission.getScope(), item -> PermissionType.valueOf(item), true, true);
                        if (objectInfo.containsKey("permissions")) {
                            ((Set<PermissionType>) objectInfo.get("permissions")).addAll(permissionTypes);
                        } else {
                            objectInfo.put("permissions", permissionTypes);
                        }
                    }
                }
            }
        }
        return re;
    }
}

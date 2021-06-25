package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.data.mgt.entity.IServerInstance;

import java.util.List;

/**
 * iserver 服务实例服务
 *
 * @author : yd
 * @date : 2021-06-25
 */
public interface IServerInstanceService {

    /**
     * 查询iserver上服务实例列表信息
     *
     * @param serviceType 服务类型，为空则查询所有工作空间
     * @return
     */
    List<IServerInstance> queryInstanceByType(String serviceType);

}

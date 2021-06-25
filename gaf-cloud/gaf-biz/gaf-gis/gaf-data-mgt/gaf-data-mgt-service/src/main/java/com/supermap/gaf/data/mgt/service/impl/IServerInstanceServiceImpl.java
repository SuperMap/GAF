package com.supermap.gaf.data.mgt.service.impl;

import com.supermap.gaf.data.mgt.common.IServerManager;
import com.supermap.gaf.data.mgt.entity.IServerInstance;
import com.supermap.gaf.data.mgt.service.IServerInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : yd
 * @date : 2021-06-25
 */
@Service
public class IServerInstanceServiceImpl implements IServerInstanceService {

    @Autowired
    private IServerManager iServerManager;

    @Override
    public List<IServerInstance> queryInstanceByType(String serviceType) {
        return iServerManager.queryInstanceByType(serviceType);
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.service.impl;

import com.supermap.gaf.authority.commontype.SysComponent;
import com.supermap.gaf.authority.dao.SysComponentQueryMapper;
import com.supermap.gaf.authority.service.SysComponentQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangxiaolong
 * @date:2021/3/25
 * 组件服务类
 */
@Service
public class SysComponentQueryServiceImpl implements SysComponentQueryService {
    @Autowired
    private SysComponentQueryMapper sysComponentQueryMapper;


    @Override
    public SysComponent getByComponentName(String componentName) {
        return sysComponentQueryMapper.getByComponentName(componentName);
    }
}

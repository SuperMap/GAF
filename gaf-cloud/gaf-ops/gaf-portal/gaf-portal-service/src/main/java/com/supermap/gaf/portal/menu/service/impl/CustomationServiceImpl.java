/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.portal.menu.service.impl;

import com.alibaba.fastjson.JSON;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.portal.menu.commontypes.CustomationInfo;
import com.supermap.gaf.portal.menu.dao.CustomationDao;
import com.supermap.gaf.portal.menu.dao.MenuDao;
import com.supermap.gaf.portal.menu.service.CustomationService;
import com.supermap.gaf.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
@Service
public class CustomationServiceImpl implements CustomationService {
    private static Logger logger = LogUtil.getLocLogger(CustomationServiceImpl.class);

    @Autowired
    private CustomationDao customationDao;

    @Autowired
    private MenuDao menuDao;

    @Override
    public String saveCustomation(CustomationInfo customation) {
        boolean success = false;

        String msg = "";
        Map<String, Object> res = new HashMap<String, Object>(16);
        if (StringUtils.isBlank(customation.getUser())) {
            res.put("success", success);

            msg = "用户未登录";
            res.put("msg", msg);

            return JSON.toJSONString(res);
        }

        customation.setNull2Default();

        String tenantId = customation.getTenantId();
        if (StringUtils.isBlank(tenantId)) {
            res.put("success", success);
            res.put("msg", "租户信息为空，操作失败");
            return JSON.toJSONString(res);
        }

        if (customationDao.queryCustomation(tenantId) == null) {
            customation.setId(UUID.randomUUID().toString());
            success = customationDao.addCustomation(customation) > 0;
            msg = success ? "添加成功" : "添加失败";
        } else {
            success = customationDao.updateCustomation(customation) > 0;
            msg = success ? "修改成功" : "修改失败";
        }

        res.put("success", success);
        res.put("msg", msg);
        return JSON.toJSONString(res);
    }

    @Override
    public MessageResult<Boolean> updateCustomation(CustomationInfo customationInfo) {
        MessageResult<Boolean> messageResult = new MessageResult<>();
        String tenantId = customationInfo.getTenantId();
        if (StringUtils.isBlank(tenantId)) {
            messageResult.setSuccessed(false);
            messageResult.setMessage("租户信息为空，操作失败");
            return messageResult;
        }

        boolean success = customationDao.updateCustomation(customationInfo) > 0;
        String message = success ? "保存成功" : "保存失败";
        messageResult.setSuccessed(success);
        messageResult.setMessage(message);
        return messageResult;
    }

    @Override
    public String queryCustomation(String tenantId) {
        String msg = "查询成功";
        boolean success = false;

        Map<String, Object> res = new HashMap<String, Object>(16);

        CustomationInfo customation = getCustomizedPortalConfig(tenantId);

        res.put("data", customation);
        res.put("success", success);
        res.put("msg", msg);
        return JSON.toJSONString(res);
    }

    @Override
    public MessageResult<Boolean> updateConfigInfo(CustomationInfo customationInfo) {
        MessageResult<Boolean> messageResult = new MessageResult<>();
        if (StringUtils.isBlank(customationInfo.getTenantId())) {
            messageResult.setSuccessed(false);
            messageResult.setMessage("租户信息为空，操作失败");
            return messageResult;
        }

        boolean success = customationDao.updateConfigInfo(customationInfo) > 0;
        String message = success ? "保存成功" : "保存失败";

        messageResult.setSuccessed(success);
        messageResult.setMessage(message);
        return messageResult;
    }

    @Override
    public MessageResult<Boolean> updateDefault2ConfigInfo(CustomationInfo customationInfo) {
        MessageResult<Boolean> messageResult = new MessageResult<>();
        if (StringUtils.isBlank(customationInfo.getTenantId())) {
            messageResult.setSuccessed(false);
            messageResult.setMessage("租户信息为空，操作失败");
            return messageResult;
        }

        boolean success = customationDao.updateDefault2ConfigInfo(customationInfo) > 0;
        String message = success ? "恢复默认定制成功" : "恢复默认定制失败";
        messageResult.setSuccessed(success);
        messageResult.setMessage(message);
        return messageResult;
    }

    @Override
    public MessageResult<Boolean> updateConfigInfo2Default(CustomationInfo customationInfo) {
        MessageResult<Boolean> messageResult = new MessageResult<>();
        if (StringUtils.isBlank(customationInfo.getTenantId())) {
            messageResult.setSuccessed(false);
            messageResult.setMessage("租户信息为空，操作失败");
            return messageResult;
        }

        boolean success = customationDao.updateConfigInfo2Default(customationInfo) > 0;
        String message = success ? "同步默认定制成功" : "同步默认定制失败";
        messageResult.setSuccessed(success);
        messageResult.setMessage(message);
        return messageResult;
    }


    private CustomationInfo getCustomizedPortalConfig(String tenantId) {
        CustomationInfo customation = null;
        if (StringUtils.isBlank(tenantId)) {
            tenantId = CustomationInfo.DEFUALT_TENANT_ID;
        }
        customation = customationDao.queryCustomation(tenantId);
        if (customation == null) {
            customation = customationDao.queryCustomation(CustomationInfo.DEFUALT_TENANT_ID);
        }

        if (StringUtils.isBlank(customation.getConfigInfo())) {
            if (StringUtils.isBlank(customation.getDefaultConfigInfo())) {
                CustomationInfo defaultCustomation = customationDao.queryCustomation(CustomationInfo.DEFUALT_TENANT_ID);
                String defaultConfigInfo = defaultCustomation.getConfigInfo() == null ? defaultCustomation.getDefaultConfigInfo() : defaultCustomation.getConfigInfo();

                customation.setDefaultConfigInfo(defaultConfigInfo);
            }

            customation.setConfigInfo(customation.getDefaultConfigInfo());
        }

        return customation;
    }
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.sys.mgt.service.impl;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.sys.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.sys.mgt.dao.SysResourceDatasourceQueryMapper;
import com.supermap.gaf.sys.mgt.service.SysResourceDatasourceQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * 数据源服实现务类
 *
 * @author wangxiaolong
 * @date:2021/3/25
 */
@Service
public class SysResourceDatasourceQueryServiceImpl implements SysResourceDatasourceQueryService {

    /**
     * 密钥长度
     */
    public static final int LENGTH_16 = 16;
    public static final int LENGTH_32 = 32;

    @Autowired
    private SysResourceDatasourceQueryMapper sysResourceDatasourceQueryMapper;

    @Value("${gaf.database.secretKey:}")
    private String secretKey;

    @Override
    public SysResourceDatasource getById(String datasourceId) {
        if (datasourceId == null) {
            throw new IllegalArgumentException("datasourceId不能为空");
        }
        SysResourceDatasource sysResourceDatasource = sysResourceDatasourceQueryMapper.select(datasourceId);
        if (!StringUtils.isEmpty(sysResourceDatasource.getPassword())) {
            sysResourceDatasource.setPassword(decrypt(sysResourceDatasource.getPassword(), secretKey));
        }
        return sysResourceDatasource;
    }

    private String decrypt(String text, String secretKey) {
        if (StringUtils.isEmpty(secretKey)) {
            throw new GafException("未配置数据库密码秘钥");
        }
        if (StringUtils.isEmpty(text)) {
            return text;
        }
        byte[] key = secretKey.getBytes(StandardCharsets.UTF_8);
        if (key.length != LENGTH_16 && key.length != LENGTH_32) {
            throw new GafException("数据库密码秘钥长度非法，应为16位或32位非中文字符");
        }
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        return aes.decryptStr(text);
    }


}

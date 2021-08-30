/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.service.impl;

import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.commontypes.Page;
import com.supermap.gaf.commontypes.tree.DefaultTreeNode;
import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.data.mgt.mapper.SysResourceDatasourceMapper;
import com.supermap.gaf.data.mgt.model.DatasourceConnectionInfo;
import com.supermap.gaf.data.mgt.service.SysResourceDatasourceService;
import com.supermap.gaf.data.mgt.vo.SysResourceDatasourceSelectVo;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.sys.mgt.client.SysDictClient;
import com.supermap.gaf.sys.mgt.model.DictData;
import com.supermap.gaf.sys.mgt.model.DictDataNode;
import com.supermap.gaf.utils.TreeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据源服实现务类
 * @author wangxiaolong
 * @date:2021/3/25
 *
 */
@Service
public class SysResourceDatasourceServiceImpl implements SysResourceDatasourceService {
    /**
     * 密钥长度
     */
    public static final int LENGTH_16 = 16;
    public static final int LENGTH_32 = 32;

    @Autowired
    private SysResourceDatasourceMapper sysResourceDatasourceMapper;
    @Autowired
    private SysDictClient sysDictClient;

    @Value("${gaf.database.secretKey:}")
    private String secretKey;

    public static void main(String[] args) {
        String s = new SysResourceDatasourceServiceImpl().encrypt("root","1q2w3e4r5t6y7u8i");
        System.out.println(s);
    }
    private String encrypt(String text, String secretKey) {
        if (StringUtils.isEmpty(secretKey)) {
            throw new GafException("未配置数据库密码秘钥");
        }
        if(StringUtils.isEmpty(text)) {
            return text;
        }
        byte[] key = secretKey.getBytes(StandardCharsets.UTF_8);
        if (key.length != LENGTH_16 && key.length != LENGTH_32) {
            throw new GafException("数据库密码秘钥长度非法，应为16位或32位非中文字符");
        }
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        return aes.encryptHex(text);
    }

    @Override
    public String decrypt(String CipherPassword) {
        return decrypt(CipherPassword,secretKey);
    }

    private String decrypt(String text, String secretKey) {
        if (StringUtils.isEmpty(secretKey)) {
            throw new GafException("未配置数据库密码秘钥");
        }
        if(StringUtils.isEmpty(text)) {
            return text;
        }
        byte[] key = secretKey.getBytes(StandardCharsets.UTF_8);
        if (key.length != LENGTH_16 && key.length != LENGTH_32) {
            throw new GafException("数据库密码秘钥长度非法，应为16位或32位非中文字符");
        }
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        return aes.decryptStr(text);
    }

    @Override
    public SysResourceDatasource getById(String datasourceId) {
        if (datasourceId == null) {
            throw new IllegalArgumentException("datasourceId不能为空");
        }
        SysResourceDatasource sysResourceDatasource = sysResourceDatasourceMapper.select(datasourceId);
        if (!StringUtils.isEmpty(sysResourceDatasource.getPassword())) {
            sysResourceDatasource.setPassword(decrypt(sysResourceDatasource.getPassword(), secretKey));
        }
        return sysResourceDatasource;
    }

    @Override
    public List<SysResourceDatasource> listByIds(@NotEmpty Collection<String> datasourceIds) {
        if (datasourceIds == null || datasourceIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<SysResourceDatasource> sysResourceDatasources = sysResourceDatasourceMapper.selectByIds(datasourceIds);
        sysResourceDatasources.forEach(sysResourceDatasource -> {
            if (!StringUtils.isEmpty(sysResourceDatasource.getPassword())) {
                sysResourceDatasource.setPassword(decrypt(sysResourceDatasource.getPassword(), secretKey));
            }
        });
        return sysResourceDatasources;
    }


    @Override
    public Page<SysResourceDatasource> listByPageCondition(SysResourceDatasourceSelectVo sysResourceDatasourceSelectVo, int pageNum, int pageSize) {
        PageInfo<SysResourceDatasource> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> sysResourceDatasourceMapper.selectList(sysResourceDatasourceSelectVo));
        Page<SysResourceDatasource> page = new Page<>();
        page.setPageIndex(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.setTotal((int)pageInfo.getTotal());
        List<SysResourceDatasource> pagelist = pageInfo.getList();
        if (pagelist != null && pagelist.size() > 0) {
            pagelist.forEach(sysResourceDatasource -> {
                if (!StringUtils.isEmpty(sysResourceDatasource.getPassword())) {
                    sysResourceDatasource.setPassword(decrypt(sysResourceDatasource.getPassword(), secretKey));
                }
            });
        }
        page.setContent(pagelist);
        page.calculateTotalPage();
        return page;
    }

	@Override
    public SysResourceDatasource insertSysResourceDatasource(SysResourceDatasource sysResourceDatasource) {
        sysResourceDatasource.setDatasourceId(UUID.randomUUID().toString());
        String notEncryptPassword = sysResourceDatasource.getPassword();
        if (!StringUtils.isEmpty(sysResourceDatasource.getPassword())) {
            sysResourceDatasource.setPassword(encrypt(sysResourceDatasource.getPassword(), secretKey));
        }
        sysResourceDatasourceMapper.insert(sysResourceDatasource);
        sysResourceDatasource.setPassword(notEncryptPassword);
        return sysResourceDatasource;
    }
	
	@Override
    public void batchInsert(List<SysResourceDatasource> sysResourceDatasources){
		if (sysResourceDatasources != null && sysResourceDatasources.size() > 0) {
	        sysResourceDatasources.forEach(sysResourceDatasource -> {
				sysResourceDatasource.setDatasourceId(UUID.randomUUID().toString());
				if(!StringUtils.isEmpty(sysResourceDatasource.getPassword())) {
                    sysResourceDatasource.setPassword(encrypt(sysResourceDatasource.getPassword(), secretKey));
                }
            });
            sysResourceDatasourceMapper.batchInsert(sysResourceDatasources);
        }
        
    }
	
	@Override
    public void deleteSysResourceDatasource(String datasourceId){
        sysResourceDatasourceMapper.delete(datasourceId);
    }

	@Override
    public void batchDelete(List<String> datasourceIds){
        sysResourceDatasourceMapper.batchDelete(datasourceIds);
    }
	
	@Override
    public SysResourceDatasource updateSysResourceDatasource(SysResourceDatasource sysResourceDatasource){
        if(!StringUtils.isEmpty(sysResourceDatasource.getPassword())) {
            sysResourceDatasource.setPassword(encrypt(sysResourceDatasource.getPassword(), secretKey) );
        }
        sysResourceDatasourceMapper.update(sysResourceDatasource);
        return sysResourceDatasource;
    }

    @Override
    public List<SysResourceDatasource> getDatasources(List<String> typeCodes, Boolean isTemplate) {
        SysResourceDatasourceSelectVo selectVo = new SysResourceDatasourceSelectVo();
        selectVo.setTypeCodes(typeCodes);
        selectVo.setTemplate(isTemplate);
        List<SysResourceDatasource> datasources = sysResourceDatasourceMapper.selectList(selectVo);
        datasources.forEach(sysResourceDatasource -> {
            if(!StringUtils.isEmpty(sysResourceDatasource.getPassword())) {
                sysResourceDatasource.setPassword(decrypt(sysResourceDatasource.getPassword(), secretKey));
            }
        });
        return datasources;
    }

    @Override
    public List<DefaultTreeNode> getTree() {
        final MessageResult<List<DictDataNode>> mr = sysDictClient.getDictDataTree("NR_DATA_CATEGORY", null, 3, false);
        if (!mr.isSuccessed()) {
            throw new GafException("查询字典NR_DATA_CATEGORY失败，原因:"+mr.getMessage());
        }
        List<DictDataNode> dataSourceTypeTree = mr.getData();
        if(dataSourceTypeTree == null || dataSourceTypeTree.size() == 0) {
            return null;
        }
        List<DefaultTreeNode> allNodes = new LinkedList<>();
        Map<String, DictDataNode> codeMap = new HashMap<>(16);
        TreeUtil.deepFirstTraverseTree(dataSourceTypeTree,(dictDataNode, iterator) -> {
            DefaultTreeNode node = new DefaultTreeNode();
            node.setKey(dictDataNode.getKey());
            node.setParentId(dictDataNode.getParentId());
            node.setTitle(dictDataNode.getLabel());
            node.setSortSn(dictDataNode.getSeq());
            DictData dictData = new DictData();
            BeanUtils.copyProperties(dictDataNode, dictData);
            node.setUserObject(dictData);
            allNodes.add(node);
            codeMap.putIfAbsent(dictDataNode.getValue(),dictDataNode);
            return TreeUtil.VisitResult.CONTINUE;
        });
        SysResourceDatasourceSelectVo selectVo = new SysResourceDatasourceSelectVo();
        selectVo.setSdx(true);
        selectVo.setTemplate(false);
        List<SysResourceDatasource> datasources = sysResourceDatasourceMapper.selectList(selectVo);
        if(datasources == null || datasources.size() == 0) {
            return null;
        }
        Map<String, List<SysResourceDatasource>> groups = datasources.stream().peek(sysResourceDatasource -> sysResourceDatasource.setPassword(decrypt(sysResourceDatasource.getPassword(), secretKey))).collect(Collectors.groupingBy(SysResourceDatasource::getCatalogCode));
        groups.forEach((catalogCode, sysResourceDatasources) -> {
            if(sysResourceDatasources!=null && sysResourceDatasources.size() > 0) {
                DictDataNode dictDataNode = codeMap.get(catalogCode);
                if(dictDataNode != null) {
                    int i = 0;
                    for (SysResourceDatasource datasource : sysResourceDatasources) {
                        datasource.setPassword(datasource.getPassword());
                        DefaultTreeNode node = new DefaultTreeNode();
                        node.setKey(datasource.getDatasourceId());
                        node.setParentId(dictDataNode.getKey());
                        node.setSortSn(i+1);
                        node.setTitle(datasource.getDsName());
                        DatasourceConnectionInfo datasourceConnectionInfo = new DatasourceConnectionInfo();
                        BeanUtils.copyProperties(datasource, datasourceConnectionInfo);
                        node.setUserObject(datasourceConnectionInfo);
                        allNodes.add(node);
                        i++;
                    }
                }
            }
        });
        DictDataNode firstNode = dataSourceTypeTree.get(0);
        DefaultTreeNode root = new DefaultTreeNode();
        root.setKey(firstNode.getParentId());
        return TreeUtil.getChildren(root, allNodes, Comparator.comparingInt(DefaultTreeNode::getSortSn));
    }

    @Override
    public List<SysResourceDatasource> getByName(String dsName, Boolean isSdx,Boolean isTemplate) {
        return sysResourceDatasourceMapper.getByName(dsName,isSdx,isTemplate);
    }

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.data.WorkspaceVersion;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.common.IServerManager;
import com.supermap.gaf.data.mgt.entity.DataWorkspace;
import com.supermap.gaf.data.mgt.mapper.DataWorkspaceMapper;
import com.supermap.gaf.data.mgt.service.DataWorkspaceService;
import com.supermap.gaf.data.mgt.service.publisher.config.UrlConfig;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.DataWorkspaceSelectVo;
import com.supermap.gaf.data.mgt.vo.WorkspaceIdServiceType;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import com.supermap.gaf.storage.service.MinioConfigHandlerI;
import com.supermap.gaf.storage.service.S3ClientService;
import com.supermap.services.providers.util.CommontypesConversion;
import com.supermap.services.rest.management.ServiceType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工作空间服务实现类
 * @author zrc 
 * @date yyyy-mm-dd
 */
@Service
public class DataWorkspaceServiceImpl implements DataWorkspaceService{
    private static final String SMWU="SMWU";
	private static final Logger  log = LoggerFactory.getLogger(DataWorkspaceServiceImpl.class);

	@Autowired
    private MinioConfigHandlerI minioConfigHandlerI;
	
	@Autowired
    private DataWorkspaceMapper dataWorkspaceMapper;

    @Autowired
    private IServerManager iServerManager;
	@Autowired
	private S3ClientService s3ClientService;
    @Autowired
    private RestTemplate restTemplate;
	
	@Override
    public DataWorkspace getById(String workspaceId){
        if(workspaceId == null){
            throw new IllegalArgumentException("workspaceId不能为空");
        }
        return  dataWorkspaceMapper.select(workspaceId);
    }
	
	@Override
    public Page<DataWorkspace> listByPageCondition(DataWorkspaceSelectVo dataWorkspaceSelectVo, int pageNum, int pageSize) {
        ShiroUser shiroUser = SecurityUtilsExt.getUser();
	    dataWorkspaceSelectVo.setTenantId(Objects.requireNonNull(shiroUser).getAuthUser().getTenantId());
        PageInfo<DataWorkspace> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            dataWorkspaceMapper.selectList(dataWorkspaceSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    @Override
    public void createWorkspace(String path) throws AuthenticationException {
	    Path root = Paths.get(minioConfigHandlerI.getVolumeRootPath());
        Path  serverPath = root.resolve(path);
        Workspace workspace = new Workspace();
        WorkspaceConnectionInfo connectionInfo = new WorkspaceConnectionInfo();
        connectionInfo.setServer(serverPath.toString());
        connectionInfo.setType(WorkspaceType.SMWU);
        workspace.saveAs(connectionInfo);
    }

	@Override
    public DataWorkspace insertDataWorkspace(DataWorkspace dataWorkspace){
        ShiroUser shiroUser = SecurityUtilsExt.getUser();

        if(SMWU.equals(dataWorkspace.getTypeCode()) ||SMWU.equals(dataWorkspace.getTypeCode())){
            DataWorkspaceSelectVo selectVo = DataWorkspaceSelectVo.builder().tenantId(shiroUser.getAuthUser().getTenantId()).server(dataWorkspace.getServer()).build();
            if(!CollectionUtils.isEmpty(dataWorkspaceMapper.selectList(selectVo))){
                throw new GafException("conflict");
            }
        }
		dataWorkspace.setWorkspaceId(UUID.randomUUID().toString());

		dataWorkspace.setCreatedBy(shiroUser.getAuthUser().getName());
		dataWorkspace.setUpdatedBy(shiroUser.getAuthUser().getName());
		dataWorkspace.setTenantId(shiroUser.getAuthUser().getTenantId());
        dataWorkspaceMapper.insert(dataWorkspace);
        return dataWorkspace;
    }
	
	@Override
    public void batchInsert(List<DataWorkspace> dataWorkspaces){
		if (dataWorkspaces != null && dataWorkspaces.size() > 0) {
	        dataWorkspaces.forEach(dataWorkspace -> {
				dataWorkspace.setWorkspaceId(UUID.randomUUID().toString());
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				dataWorkspace.setCreatedBy(shiroUser.getAuthUser().getName());
				dataWorkspace.setUpdatedBy(shiroUser.getAuthUser().getName());
                dataWorkspace.setTenantId(shiroUser.getAuthUser().getTenantId());
            });
            dataWorkspaceMapper.batchInsert(dataWorkspaces);
        }
        
    }
	
	@Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDataWorkspace(String workspaceId) throws AuthenticationException {
        DataWorkspace dataWorkspace = getById(workspaceId);
        if(dataWorkspace!=null){
            dataWorkspaceMapper.delete(workspaceId);
            if(isFileType(dataWorkspace.getTypeCode()) && !StringUtils.isEmpty(dataWorkspace.getServer())){
                s3ClientService.deleteObjects(Arrays.asList(dataWorkspace.getServer()));
            }
        }
    }
    boolean isFileType(String typeCode){
	    return WorkspaceType.SMWU.name().equals(typeCode)|| WorkspaceType.SXWU.name().equals(typeCode)|| WorkspaceType.SMW.name().equals(typeCode) || WorkspaceType.SXW.name().equals(typeCode);
    }
	@Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> workspaceIds) throws AuthenticationException {
        List<DataWorkspace> workspaces = dataWorkspaceMapper.selectByIds(workspaceIds);
        if(!CollectionUtils.isEmpty(workspaces)){
            dataWorkspaceMapper.batchDelete(workspaceIds);
            List<String> keyNames = workspaces.stream().filter(dataWorkspace -> isFileType(dataWorkspace.getTypeCode())&&!StringUtils.isEmpty(dataWorkspace.getServer()))
                    .map(DataWorkspace::getServer).collect(Collectors.toList());
            s3ClientService.deleteObjects(keyNames);
        }
    }
	
	@Override
    public DataWorkspace updateDataWorkspace(DataWorkspace dataWorkspace){
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		dataWorkspace.setUpdatedBy(shiroUser.getAuthUser().getName());
		dataWorkspaceMapper.update(dataWorkspace);
        return dataWorkspace;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MessageResult<Object>> publishService(List<WorkspaceIdServiceType> workspaceIdServiceTypes) throws IOException {
        List<MessageResult<Object>> resultList = new LinkedList<>();
	    for (WorkspaceIdServiceType workspaceIdServiceType: workspaceIdServiceTypes) {
            String workspaceId = workspaceIdServiceType.getWorkspaceId();
            DataWorkspace workspace = getById(workspaceId);
            List<String> serviceTypes = workspaceIdServiceType.getServiceTypes();
            // 1.校验
            if(Objects.isNull(workspace)) {
                MessageResult<Object> failedResult = MessageResult.failed(Object.class).message("未找到工作空间数据").build();
                failedResult.setResourceId(workspaceId);
                resultList.add(failedResult);
                break;
            }
            if(workspace.getPublished()) {
                MessageResult<Object> failedResult = MessageResult.failed(Object.class).message("工作空间已发布").build();
                failedResult.setResourceId(workspaceId);
                resultList.add(failedResult);
                break;
            }
            // 1.1 校验工作空间类型是否支持
            WorkspaceType workspaceType = (WorkspaceType) WorkspaceType.parse(WorkspaceType.class, workspace.getTypeCode());
            if(workspaceType == null) {
                MessageResult<Object> failedResult = MessageResult.failed(Object.class).message(String.format("不支持的工作空间类型:%s", workspace.getTypeCode())).build();
                failedResult.setResourceId(workspaceId);
                resultList.add(failedResult);
                break;
            }
            // 1.2 校验发布的服务类型 请求iserver 查询工作空间可支持的发布类型 todo:
            // 1.3 校验工作空间连接参数是否可用
            if (!WorkspaceType.SMWU.equals(workspaceType) && !WorkspaceType.SXWU.equals(workspaceType)) {
                Workspace workspace1 = new Workspace();
                WorkspaceConnectionInfo workspaceConnectionInfo = getWorkspaceConnectionInfo(workspace);
                boolean opened = workspace1.open(workspaceConnectionInfo);
                workspace1.close();
                workspace1.dispose();
                workspaceConnectionInfo.dispose();
                if (!opened) {
                    MessageResult<Object> failedResult = MessageResult.failed(Object.class).message("打开工作空间失败").build();
                    failedResult.setResourceId(workspaceId);
                    resultList.add(failedResult);
                    break;
                }
            }
            // 2.发布服务
            MessageResult<Object> publishResult = doPublishWorkspace(workspace, serviceTypes);
            resultList.add(publishResult);
        }
        return resultList;
    }


    private MessageResult<Object> doPublishWorkspace(DataWorkspace workspace , List<String> serviceTypes) throws IOException {
        com.supermap.services.rest.management.PublishServiceParameter publishServiceParameter = new com.supermap.services.rest.management.PublishServiceParameter();
        WorkspaceConnectionInfo workspaceConnectionInfo = getWorkspaceConnectionInfo(workspace);
        if (WorkspaceType.SXWU.equals(workspaceConnectionInfo.getType()) || WorkspaceType.SMWU.equals(workspaceConnectionInfo.getType())){
            if (StringUtils.isNotEmpty(workspaceConnectionInfo.getPassword())) {
                publishServiceParameter.workspaceConnectionInfo = "server=" + workspaceConnectionInfo.getServer() + ";password="+ workspaceConnectionInfo.getPassword();
            } else {
                publishServiceParameter.workspaceConnectionInfo = workspaceConnectionInfo.getServer();
            }
        } else {
            publishServiceParameter.workspaceConnectionInfo = CommontypesConversion.getWorkspaceConnectionInfo(workspaceConnectionInfo).toStandardString();
        }
        // publishServiceParameter.workspaceConnectionInfo = CommontypesConversion.getWorkspaceConnectionInfo(workspaceConnectionInfo).toStandardString();
        workspaceConnectionInfo.dispose();
        if(Objects.nonNull(serviceTypes) && !serviceTypes.isEmpty()){
            ServiceType[] serviceTypesArray = new ServiceType[serviceTypes.size()];
            for (int i = 0; i < serviceTypes.size(); i++) {
                serviceTypesArray[i] = ServiceType.valueOf(serviceTypes.get(i));
            }
            publishServiceParameter.servicesTypes = serviceTypesArray;
        }
        MessageResult<String> generateTokenResult = iServerManager.applyToken();
        String iServerUrl = iServerManager.getAvailableIServerSetting().getHostServerUrl();
        String publishWorkspaceUrl = UrlConfig.getPublishWorkspaceUrlWithReturn(iServerUrl, generateTokenResult.getData());
        MessageResult<Object> publishResult = executePublish(publishWorkspaceUrl, publishServiceParameter);
        publishResult.setResourceId(workspace.getWorkspaceId());
        if (publishResult.isSuccessed()) {
            workspace.setPublished(true);
            updateDataWorkspace(workspace);
        }
        return publishResult;
    }

    private WorkspaceConnectionInfo getWorkspaceConnectionInfo(DataWorkspace workspace) throws AuthenticationException {
        WorkspaceConnectionInfo workspaceConnectionInfo = new WorkspaceConnectionInfo();
        workspaceConnectionInfo.setName(workspace.getWsName());
        WorkspaceType type = (WorkspaceType)WorkspaceType.parse(WorkspaceType.class, workspace.getTypeCode());
        workspaceConnectionInfo.setType(type);
        workspaceConnectionInfo.setUser(workspace.getUserName());
        workspaceConnectionInfo.setPassword(workspace.getPassword());
        if (WorkspaceType.SXWU.equals(type) || WorkspaceType.SMWU.equals(type)){
            String volumeRootPath = minioConfigHandlerI.getVolumeRootPath();
            workspaceConnectionInfo.setServer(Paths.get(volumeRootPath,workspace.getServer()).toString());
        } else {
            workspaceConnectionInfo.setServer(workspace.getServer());
            workspaceConnectionInfo.setDatabase(workspace.getDatabase());
            workspaceConnectionInfo.setVersion(WorkspaceVersion.UGC70);
            if(WorkspaceType.SQL.equals(type)){
                workspaceConnectionInfo.setDriver("SQL Server");
            }
        }
        return workspaceConnectionInfo;
    }

    private MessageResult<Object> executePublish(String url, com.supermap.services.rest.management.PublishServiceParameter param){
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, param, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String body = responseEntity.getBody();
            if (StringUtils.isNotBlank(body)) {
                Object parse = JSONObject.parse(body);
                return MessageResult.data(parse).message("发布成功").build();
            }
        }
        return MessageResult.failed(Object.class).message("发布失败").build();
    }

}

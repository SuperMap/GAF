/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.webgis.service.impl;

import cn.hutool.core.util.URLUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.authority.client.AuthResourceApiClient;
import com.supermap.gaf.authority.commontype.AuthResourceApi;
import com.supermap.gaf.authority.enums.ResourceApiMethodEnum;
import com.supermap.gaf.authority.vo.TreeNode;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import com.supermap.gaf.sys.mgt.client.SysCatalogClient;
import com.supermap.gaf.sys.mgt.commontype.SysCatalog;
import com.supermap.gaf.sys.mgt.enums.CatalogTypeEnum;
import com.supermap.gaf.utils.AppConfigParser;
import com.supermap.gaf.webgis.cache.RegistryResultCacheI;
import com.supermap.gaf.webgis.dao.WebgisDataServiceFieldMapper;
import com.supermap.gaf.webgis.dao.WebgisServiceAssociationMapper;
import com.supermap.gaf.webgis.dao.WebgisServiceMapper;
import com.supermap.gaf.webgis.domain.BatchRegistryServiceResult;
import com.supermap.gaf.webgis.domain.WebgisServiceDo;
import com.supermap.gaf.webgis.entity.WebgisDataServiceField;
import com.supermap.gaf.webgis.entity.WebgisService;
import com.supermap.gaf.webgis.enums.ServiceTypeEnum;
import com.supermap.gaf.webgis.service.WebgisCatalogLayerService;
import com.supermap.gaf.webgis.service.WebgisConfigService;
import com.supermap.gaf.webgis.service.WebgisServiceService;
import com.supermap.gaf.webgis.util.Page;
import com.supermap.gaf.webgis.vo.WebgisServiceConditonVo;
import com.supermap.gaf.webgis.vo.WebgisServiceSelectVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * GIS服务服务实现类
 * @author wangxiaolong 
 * @date 2020-12-05
 */
@Service
public class WebgisServiceServiceImpl implements WebgisServiceService{

    public static final String WEBGIS_DEFAULT_COMPONENT_ID = "webgisDefaultComponentId";
    public static final String REGISTRY_TYPE_SERVER = "server";
    public static final String REGISTRY_TYPE_BATCH = "batch";
    public static final String REGISTRY_TYPE_SINGLE = "single";


    @Autowired
    public RegistryResultCacheI registryResultCacheI;


    @Autowired
    private WebgisServiceMapper webgisServiceMapper;

    @Autowired
    private WebgisCatalogLayerService webgisCatalogLayerService;


    @Autowired
    private SysCatalogClient sysCatalogClient;

    @Autowired
    private AuthResourceApiClient authResourceApiClient;

    @Autowired
    private WebgisServiceAssociationMapper webgisServiceAssociationMapper;

    @Autowired
    private WebgisDataServiceFieldMapper webgisDataServiceFieldMapper;

    @Autowired
    private WebgisConfigService webgisConfigService;
	
	@Override
    public WebgisService getById(String gisServiceId){
        if(gisServiceId == null){
            throw new IllegalArgumentException("gisServiceId不能为空");
        }
        return  webgisServiceMapper.select(gisServiceId);
    }

    @Override
    public BatchRegistryServiceResult getRegistryServiceResult(String resultCode) {
        BatchRegistryServiceResult registryServiceResult = registryResultCacheI.get(resultCode);
        if(registryServiceResult==null){
            throw new GafException("指定requestCode不存在");
        }
        return registryServiceResult;
    }

    @Override
    public Page<WebgisService> listByPageCondition(WebgisServiceSelectVo webgisServiceSelectVo, int pageNum, int pageSize) {
        PageInfo<WebgisService> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            webgisServiceMapper.selectList(webgisServiceSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

	@Override
    public void insertWebgisService(WebgisService webgisService,String type){
	    if(!REGISTRY_TYPE_SINGLE.equals(type)){
	        throw new GafException("未知注册类型");
        }
	    ShiroUser shiroUser = SecurityUtilsExt.getUser();
        webgisService.setCreatedBy(shiroUser.getAuthUser().getName());
        webgisService.setUpdatedBy(shiroUser.getAuthUser().getName());
        webgisConfigService.parseConfig(webgisService);
        registryWebgis(webgisService);
    }



    @Override
    @Transactional
    public void registryWebgis(WebgisService webgisService){
	    if(!"MAPWORLD".equals(webgisService.getTypeCode())){
            List<WebgisService> webgisServices = webgisServiceMapper.selectList(WebgisServiceSelectVo.builder().address(webgisService.getAddress()).build());
	        if(!CollectionUtils.isEmpty(webgisServices)){

	            throw new GafException("服务地址重复",409);
            }
        }
        webgisService.setGisServiceId(UUID.randomUUID().toString());
        AuthResourceApi authResourceApi = addWebgisServiceApi(webgisService);
        webgisService.setResourceApiId(authResourceApi.getResourceApiId());
        webgisServiceMapper.insert(webgisService);
    }

    private AuthResourceApi addWebgisServiceApi(WebgisService webgisService) {
        List<SysCatalog> catalogs = listCatalogs(SysCatalog.builder().status(true).type(CatalogTypeEnum.WEBGIS_SERVICE_GROUP_TYPE.getValue()).name(ServiceTypeEnum.getMap().get(webgisService.getTypeCode())).build());
        SysCatalog webgisTypeCatalog = null;
        if (catalogs.size() <= 0) {
            // 插入目录 前查找名为webgis服务的根目录
            List<SysCatalog> webgisRootCatalogs = listCatalogs(SysCatalog.builder().status(true).type(CatalogTypeEnum.WEBGIS_SERVICE_GROUP_TYPE.getValue()).name("webgis服务").build());
            SysCatalog webgisRoot = null;
            if (webgisRootCatalogs.size() <= 0) {
                // 插入根目录
                webgisRoot = addSysCatalog(SysCatalog.builder()
                        .status(true)
                        .type(CatalogTypeEnum.WEBGIS_SERVICE_GROUP_TYPE.getValue())
                        .parentId("0")
                        .name("webgis服务")
                        .description("webgis服务根目录")
                        .sortSn(1)
                        .build());
            } else {
                webgisRoot = webgisRootCatalogs.get(0);
            }
            webgisTypeCatalog = addSysCatalog(SysCatalog.builder()
                    .status(true)
                    .type(webgisRoot.getType())
                    .parentId(webgisRoot.getCatalogId())
                    .name(ServiceTypeEnum.getMap().get(webgisService.getTypeCode()))
                    .description("webgis服务类型为" + ServiceTypeEnum.getMap().get(webgisService.getTypeCode()) + "对应的分组目录")
                    .sortSn(1)
                    .build());
        } else {
            webgisTypeCatalog = catalogs.get(0);
        }
        String routeUrl = URLUtil.getPath(URLUtil.normalize(webgisService.getAddress()));
        // 查询在该分组下是否有同名的routeUrl
//        List<AuthResourceApi> sameNameApis = listApis(AuthResourceApi.builder().status(true).apiCatalogId(webgisTypeCatalog.getCatalogId()).routeUrl(routeUrl).build());
//        if (sameNameApis.size() > 0) {
//            return sameNameApis.get(0);
//        }
        AuthResourceApi authResourceApi = new AuthResourceApi();
        authResourceApi.setMethod(ResourceApiMethodEnum.GET.getValue());
        authResourceApi.setRouteUrl(routeUrl);
        authResourceApi.setName(webgisService.getName());
        authResourceApi.setStatus(true);
        // 类型2表示第三方
        authResourceApi.setType("2");
        authResourceApi.setApiCatalogId(webgisTypeCatalog.getCatalogId());
        authResourceApi.setSysComponentId(WEBGIS_DEFAULT_COMPONENT_ID);
        return addApi(authResourceApi);
    }

    private List<SysCatalog> listCatalogs(SysCatalog queryCatalog) {
        MessageResult<List<SysCatalog>> messageResult = sysCatalogClient.list(queryCatalog);
        if (!messageResult.isSuccessed()) {
            throw new GafException(messageResult.getMessage());
        }
        return messageResult.getData();
    }

    private SysCatalog addSysCatalog(SysCatalog sysCatalog) {
        MessageResult<SysCatalog> messageResult = sysCatalogClient.insertSysCatalog(sysCatalog);
        if (!messageResult.isSuccessed()) {
            throw new GafException(messageResult.getMessage());
        }
        return messageResult.getData();
    }

    private List<AuthResourceApi> listApis(AuthResourceApi query) {
        MessageResult<List<AuthResourceApi>> messageResult = authResourceApiClient.list(query);
        if (!messageResult.isSuccessed()) {
            throw new GafException(messageResult.getMessage());
        }
        return messageResult.getData();
    }

    private AuthResourceApi addApi(AuthResourceApi authResourceApi) {
        MessageResult<AuthResourceApi> messageResult = authResourceApiClient.insertAuthResourceApi(authResourceApi);
        if (!messageResult.isSuccessed()) {
            throw new GafException(messageResult.getMessage());
        }
        return messageResult.getData();
    }



    // todo: 若要使用该方法 需要远程调用api资源服务去批量新增api
	@Override
    public void batchInsert(List<WebgisService> webgisServices){
		if (webgisServices != null && webgisServices.size() > 0) {
	        webgisServices.forEach(webgisService -> {
                // 尝试解析配置，提前告知格式问题，特别是json串
                webgisConfigService.parseConfig(webgisService);
				webgisService.setGisServiceId(UUID.randomUUID().toString());
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				webgisService.setCreatedBy(shiroUser.getAuthUser().getName());
				webgisService.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            webgisServiceMapper.batchInsert(webgisServices);
        }
        
    }

    @Transactional
	@Override
    public void deleteWebgisService(String gisServiceId){
        WebgisService webgisService = this.getById(gisServiceId);
        if (webgisService == null) {
            throw new GafException("未找到该webgis服务");
        }
        MessageResult<Void> result = authResourceApiClient.deleteAuthResourceApi(webgisService.getResourceApiId());
        if (!result.isSuccessed()) {
            throw new GafException(result.getMessage());
        }
        webgisServiceMapper.delete(gisServiceId);
    }

    // 不要使用。若要使用需要远程调用api资源服务去批量删除api
	@Override
    public void batchDelete(List<String> gisServiceIds){
        webgisServiceMapper.batchDelete(gisServiceIds);
    }
	
	@Override
    public WebgisService updateWebgisService(WebgisService webgisService){
        // 尝试解析配置，提前告知格式问题，特别是json串
        webgisConfigService.parseConfig(webgisService);
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		webgisService.setUpdatedBy(shiroUser.getAuthUser().getName());
        WebgisService oldWebgisService = this.getById(webgisService.getGisServiceId());
        if (!Objects.equals(oldWebgisService.getAddress(), webgisService.getAddress())
            || !Objects.equals(oldWebgisService.getName(), webgisService.getName())
        ) {
            AuthResourceApi authResourceApi = new AuthResourceApi();
            authResourceApi.setResourceApiId(oldWebgisService.getResourceApiId());
            authResourceApi.setName(webgisService.getName());
            String path = URLUtil.getPath(URLUtil.normalize(webgisService.getAddress()));
            authResourceApi.setRouteUrl(path);
            MessageResult<Void> result = authResourceApiClient.updateAuthResourceApi(authResourceApi, oldWebgisService.getResourceApiId());
            if (!result.isSuccessed()) {
                throw new GafException(result.getMessage());
            }
        }
        webgisService.setResourceApiId(oldWebgisService.getResourceApiId());
        if (StringUtils.isBlank(webgisService.getMoreProperties())) {
            webgisService.setMoreProperties(null);
        }
        webgisServiceMapper.update(webgisService);
        return webgisService;
    }

    @Override
    public List<TreeNode> getServiceTypes() {
        ServiceTypeEnum[] values = ServiceTypeEnum.values();
        List<TreeNode> res = new LinkedList<>();
        for (int i = 0; i < values.length; i++) {
            TreeNode treeNode = new TreeNode();
            treeNode.setParentId("0");
            treeNode.setKey(values[i].getCode());
            treeNode.setTitle(values[i].getName());
            treeNode.setSortSn(i+1);
            treeNode.setType(0);
            res.add(treeNode);
        }
        return res;
    }

    @Override
    public List<WebgisService> listByIds(List<String> ids) {
        return webgisServiceMapper.selectByIds(ids);
    }

    @Override
    public Page<WebgisService> listByPageCondition(WebgisServiceConditonVo webgisServiceConditonVo, Integer pageNum, Integer pageSize, String layerCatalogId) {
        List<String> serviceIds = webgisCatalogLayerService.listGisIdsByCatalogId(layerCatalogId);
        Set<String> serviceIdSet = new HashSet<>(serviceIds);
        PageInfo<WebgisService> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            webgisServiceMapper.selectNotInSet(webgisServiceConditonVo, serviceIdSet);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

    // [{url: '',title: '', fields: [], alias: []}]
    @Override
    public List selectAssociationDataServices(String gisServiceId) {
        WebgisService webgisService = getById(gisServiceId);
        if(webgisService== null) {
            return new ArrayList();
        }
        List<WebgisServiceDo> serviceDos  = new ArrayList<>();
        // 本身就是数据服务
        if(ServiceTypeEnum.RESTDATA.getCode().equals(webgisService.getTypeCode())){
            WebgisServiceDo webgisServiceDo = new WebgisServiceDo();
            BeanUtils.copyProperties(webgisService,webgisServiceDo);
            serviceDos.add(webgisServiceDo);
        }else{
            serviceDos = webgisServiceAssociationMapper.selectAssociationServices(gisServiceId, WebgisServiceSelectVo.builder().typeCode(ServiceTypeEnum.RESTDATA.getCode()).build());
        }

        List re = (List) AppConfigParser.parse(serviceDos);
        for(Object item:re){
            Map<String,Object> itemMap = (Map<String, Object>) item;
            List<WebgisDataServiceField> selectedFields = webgisDataServiceFieldMapper.selectByCombination(WebgisDataServiceField.builder()
                    .gisDataServiceId((String) itemMap.get("resourceId")).build());
            if(!CollectionUtils.isEmpty(selectedFields)){
                List<String> fields = new ArrayList<>();
                List<String> alias = new ArrayList<>();
                for(WebgisDataServiceField serviceField:selectedFields){
                    fields.add(serviceField.getFieldName());
                    alias.add(serviceField.getFieldAlias());
                }
                itemMap.put("fields",fields);
                itemMap.put("alias",alias);
            }
        }

        return re;
    }



//    public static void main(String[] args) {
//
//        RestTemplate restTemplate = new RestTemplate();
//        JSONArray list = restTemplate.getForObject("http://nsip.net.cn/iserver/services.json", JSONArray.class);
//        Map<String,List<WebgisService>> re = new HashMap<>();
//        for(int i=0;i<list.size();++i){
//            JSONObject item = list.getJSONObject(i);
//            String typeCode = parseServiceType(item.getString("interfaceType"),item.getString("componentType"));
//            String url = item.getString("url");
//            String name = item.getString("name");
//            List<WebgisService> webgisServices = listService(typeCode,url,name);
//            if(re.containsKey(typeCode)){
//                re.get(typeCode).addAll(webgisServices);
//            }else{
//                re.put(typeCode,webgisServices);
//            }
//
//        }
//        System.out.println(JSON.toJSONString(re));
//
//    }



}

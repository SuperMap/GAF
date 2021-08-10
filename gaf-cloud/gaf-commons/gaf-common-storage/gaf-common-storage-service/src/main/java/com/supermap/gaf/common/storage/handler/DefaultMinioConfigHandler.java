///*
// * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
// * This program are made available under the terms of the Apache License, Version 2.0
// * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
//*/
//package com.supermap.gaf.common.storage.handler;
//
//import com.supermap.gaf.common.storage.config.StorageConfig;
//import com.supermap.gaf.common.storage.entity.MinioConfig;
//import com.supermap.gaf.common.storage.entity.MinioConfig;
//import com.supermap.gaf.common.storage.enums.TenantMode;
//import com.supermap.gaf.common.storage.spi.TenantInfoI;
//import org.springframework.util.CollectionUtils;
//
//import java.util.List;
//import java.util.Map;
//
//import static com.supermap.gaf.common.storage.enums.TenantMode.NONE;
//
///**
// * @date:2021/3/25
// * @author heykb
// */
//public class DefaultMinioConfigHandler extends AbstractMinioConfigHandler{
//
//
//    private Map<String, List<MinioConfig>> tenantConfigMaps;
//    private List<MinioConfig> globalConfigs;
//
//    public DefaultMinioConfigHandler(StorageConfig storageConfig, TenantInfoI tenantInfoI, Map<String, List<MinioConfig>> tenantConfigMaps, List<MinioConfig> globalConfigs) {
//        super(storageConfig, tenantInfoI);
//        this.tenantConfigMaps = tenantConfigMaps;
//        this.globalConfigs = globalConfigs;
//    }
//
//    @Override
//    public MinioConfig getGlobalConfig(String name) {
//        MinioConfig re = null;
//        if(!CollectionUtils.isEmpty(globalConfigs)){
//            for(MinioConfig item:globalConfigs){
//                if(name.equals(item.getName())){
//                    re = item;
//                    break;
//                }
//            }
//        }
//        if(storageConfig.getMode()== TenantMode.SINGLE_NODE_MULTI_BUCKET){
//            re.setBucketName(tenantInfoI.getTenantId());
//        }
//        return re;
//    }
//
//    @Override
//    public MinioConfig getTenantConfig(String name) {
//        MinioConfig re = null;
//        if(storageConfig.getMode() != NONE){
//            List<MinioConfig> tenantConfigs = tenantConfigMaps.get(tenantInfoI.getTenantId());
//            if(!CollectionUtils.isEmpty(tenantConfigs)){
//                for(MinioConfig item:tenantConfigs){
//                    if(name.equals(item.getName())){
//                        re = item;
//                        break;
//                    }
//                }
//            }
//        }
//
//        return re;
//    }
//
//    @Override
//    public String getVolumeRootPath(String configName) {
//        return null;
//    }
//}

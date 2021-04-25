/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.api.scanner.configuration.listener;

import com.supermap.gaf.api.scanner.entity.ApiDoc;
import com.supermap.gaf.api.scanner.entity.SysComponent;
import com.supermap.gaf.api.scanner.service.SwaggerApiDocService;
import com.supermap.gaf.api.scanner.service.SwaggerApiInitializingService;
import com.supermap.gaf.utils.LogUtil;
import io.swagger.config.ScannerFactory;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.util.ObjectMapperFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/4/24 2:48 PM
 */
@Component
public class SwaggerApiListener extends ObjectMapperFactory implements ApplicationRunner {

    @Autowired
    private SwaggerApiInitializingService swaggerApiInitializingService;
    @Autowired
    private SwaggerApiDocService apiDocService;

    @Value("${spring.application.name}")
    private String appName;
    @Value("${spring.jersey.application-path:}")
    private String pathPrefix;

    private static Logger logger = LogUtil.getLocLogger(SwaggerApiListener.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("swagger-api文档json入库程序【启动】...");
        Map<String, Path> swaggerMap;
        Swagger swagger;
        try {
            BeanConfig beanConfig = (BeanConfig) ScannerFactory.getScanner();
            //获取swagger
            swagger = beanConfig.getSwagger();
            //加统一前缀
            if(StringUtils.isEmpty(swagger.getBasePath())){
                swagger.setBasePath(pathPrefix);
            }
            swaggerMap = swagger.getPaths();
            if (swaggerMap == null){
                logger.info("swagger-api文档json入库程序【无API】...");
                return;
            }
        }catch (Exception e){
            logger.info("swagger-api文档json入库程序【无API】...");
            return;
        }
        Set<String> keySet = swaggerMap.keySet();
        for (String key : keySet){
            Path path = swaggerMap.get(key);
            //调整对象内参数备注问题
            fixSwaggerParameters(path);
        }
        //上传resource到数据库
        try {
            SysComponent sysComponent = swaggerApiInitializingService.searchSysComponent();
            if (null != sysComponent){
                swaggerApiInitializingService.initializeSysCatalog(sysComponent, swagger);
                swaggerApiInitializingService.initializeSysResourceApi(sysComponent,swagger);
                logger.info("resource-api入库【成功】...");
            }else {
                throw new NullPointerException("未查询到系统模块数据，暂不入库");
            }
        }catch (Exception e){
//            e.printStackTrace();
            logger.error("resource-api入库【失败】...");
        }
        //上传api_doc到数据库
        try {
            ApiDoc apiDoc = new ApiDoc(appName,createJson().writeValueAsString(swagger));
            apiDocService.syncApiDoc(apiDoc);
            logger.info("swagger-api文档api_doc入库程序【成功】...");
        }catch (Exception e){
//            e.printStackTrace();
            logger.error("swagger-api文档api_doc入库程序【失败】...");
        }
    }

    /**
     * 1.修复swagger在使用@ApiImplicitParams时，会在Parameters字段生成重复的参数，并且放在前排。
     * 这里采取策略为：如果有相同参数，有describe字段的参数放列表前面
     * 2.修复swagger在使用@ApiImplicitParams时，如果type为body，会导致生成重复的参数。
     * 这里采取策略为：如果有相同参数，删除没有describe字段的参数
     */
    private void fixSwaggerParameters(Path path){
        List<Operation> operations = path.getOperations();
        for (Operation operation : operations){
            List<Parameter> parameters = operation.getParameters();
            //修复1的swagger对象问题
            parameters = parameters.stream().sorted((o1, o2) -> {
                if (!StringUtils.isEmpty(o1.getDescription()) && StringUtils.isEmpty(o2.getDescription())){
                    return -1;
                }else {
                    return 0;
                }
            }).collect(Collectors.toList());
            //修复2的swagger对象问题
            String bodyTypeName = "body";
            int bodyTypeDescribeIndex = -1;
            int bodyTypeNoDescribeIndex = -1;
            for (int i = 0; i < parameters.size(); i++) {
                Parameter parameter = parameters.get(i);
                if (parameter.getIn().equals(bodyTypeName)){
                    if (!StringUtils.isEmpty(parameter.getDescription())){
                        bodyTypeDescribeIndex = i;
                    }else {
                        bodyTypeNoDescribeIndex = i;
                    }
                }
            }
            if (bodyTypeNoDescribeIndex != -1 && bodyTypeDescribeIndex != -1){
                parameters.remove(bodyTypeNoDescribeIndex);
            }
            operation.setParameters(parameters);
        }
    }
}

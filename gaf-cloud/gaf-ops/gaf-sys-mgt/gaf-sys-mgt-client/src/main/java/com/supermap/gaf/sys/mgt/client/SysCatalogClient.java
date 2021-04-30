/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.client;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.sys.mgt.commontype.SysCatalog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 系统管理相关接口调用
 * @date:2021/3/25
 * @author wxl
 */
@FeignClient(name = "GAF-SYS-MGT"/*,url = "http://localhost:8081"*/,contextId = "sysCatalogClient")
public interface SysCatalogClient {
    // 目录相关接口
    @GetMapping("/sys-mgt/sys-catalogs/condition")
    MessageResult<List<SysCatalog>> list(@SpringQueryMap SysCatalog queryCatalog);
    @PostMapping("/sys-mgt/sys-catalogs")
    MessageResult<SysCatalog> insertSysCatalog(@RequestBody @NotNull SysCatalog sysCatalog);
}

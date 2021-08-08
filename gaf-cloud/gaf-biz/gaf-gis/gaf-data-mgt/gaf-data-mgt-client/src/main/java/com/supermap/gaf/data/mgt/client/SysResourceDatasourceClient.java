/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.client;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.commontypes.Page;
import com.supermap.gaf.commontypes.tree.DefaultTreeNode;
import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.data.mgt.vo.DatasourceOptionVo;
import com.supermap.gaf.data.mgt.vo.SysResourceDatasourceSelectVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import java.util.List;


/**
 * 数据源管理相关接口调用
 *
 * @author wxl
 * @since 2021/3/25
 */
@Component
@FeignClient(name = "GAF-DATA-MGT", contextId = "sysResourceDatasourceClient")
public interface SysResourceDatasourceClient {

    @GetMapping("/data-mgt/sys-resource-datasources/{datasourceId}")
    MessageResult<SysResourceDatasource> getById(@PathVariable("datasourceId")String datasourceId);

    @GetMapping("/data-mgt/sys-resource-datasources/tree")
    MessageResult<List<DefaultTreeNode>> getTree();

    @GetMapping("/data-mgt/sys-resource-datasources/options")
    MessageResult<List<DatasourceOptionVo>> getOptions(@RequestParam("typeCodes") List<String> typeCodes,
                                                       @RequestParam("isTemplate") @DefaultValue("false") Boolean isTemplate);

    @GetMapping("/data-mgt/sys-resource-datasources")
    MessageResult<Page<SysResourceDatasource>> pageList(@Valid @SpringQueryMap SysResourceDatasourceSelectVo sysResourceDatasourceSelectVo,
                                                               @DefaultValue("1")@RequestParam("pageNum")Integer pageNum,
                                                               @DefaultValue("10")@RequestParam("pageSize")Integer pageSize);
    @PostMapping("/data-mgt/sys-resource-datasources/connection-param/check")
    MessageResult<Void> checkConnection(@NotNull @RequestBody SysResourceDatasource sysResourceDatasource);

    @PostMapping("/data-mgt/sys-resource-datasources")
    MessageResult<SysResourceDatasource> insertSysResourceDatasource(@RequestBody SysResourceDatasource sysResourceDatasource);
    @PostMapping("/data-mgt/sys-resource-datasources/batch")
    MessageResult<Void> batchInsert(@RequestBody List<SysResourceDatasource> sysResourceDatasources);
    @DeleteMapping("/data-mgt/sys-resource-datasources/{datasourceId}")
    MessageResult<Void> deleteSysResourceDatasource(@PathVariable("datasourceId")String datasourceId);

    @DeleteMapping("/data-mgt/sys-resource-datasources")
    MessageResult<Void> batchDelete(@RequestBody List<String> datasourceIds);
    @PutMapping("/data-mgt/sys-resource-datasources/{datasourceId}")
    MessageResult<Void> updateSysResourceDatasource(@RequestBody SysResourceDatasource sysResourceDatasource,@PathVariable("datasourceId")String datasourceId);

    @GetMapping("/data-mgt/sys-resource-datasources/getDsName")
    MessageResult<List<SysResourceDatasource>> checkRepeat(
            @RequestParam("dsName")String dsName,
            @RequestParam("isSdx")@DefaultValue("true")Boolean isSdx,
            @RequestParam("isTemplate") @DefaultValue("false") Boolean isTemplate);

}

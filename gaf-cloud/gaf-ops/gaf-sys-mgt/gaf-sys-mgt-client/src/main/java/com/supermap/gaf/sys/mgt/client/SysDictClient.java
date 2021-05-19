/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.sys.mgt.client;

import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.sys.mgt.model.DictData;
import com.supermap.gaf.sys.mgt.model.DictDataNode;
import com.supermap.gaf.sys.mgt.model.DictType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.DefaultValue;
import java.util.List;


/**
 * 系统管理字典相关接口调用
 * @date:2021/3/25
 * @author wxl
 */
@FeignClient(name = "GAF-SYS-MGT"/*,url = "http://localhost:8081"*/,contextId = "sysDictClient")
public interface SysDictClient {

    @GetMapping("/sys-mgt/sys-dicts/{dictTypeCode}/tree")
    MessageResult<List<DictDataNode>> getDictDataTree(@PathVariable("dictTypeCode") String dictTypeCode,
                                                             @RequestParam("value") String value,
                                                             @RequestParam("level") @DefaultValue("0") int level,
                                                             @RequestParam("onlyVisible") @DefaultValue("false") boolean onlyVisible);
    @GetMapping("/sys-mgt/sys-dicts/{dictTypeCode}/dict-data")
    MessageResult<List<DictData>> getDictData(@NotEmpty @PathVariable("dictTypeCode") String dictTypeCode,
                                                     @DefaultValue("false") @RequestParam("onlyVisible") Boolean onlyVisible);

    @GetMapping("/sys-mgt/sys-dicts/{dictTypeCode}/{value}/path")
    MessageResult<List<DictData>> getPath(@NotEmpty @PathVariable("dictTypeCode") String dictTypeCode,
                                                 @NotEmpty @RequestParam("value") String value);

    @GetMapping("/sys-mgt/sys-dicts/{dictTypeCode}/detail")
    MessageResult<DictType> getByTypeCode(@NotEmpty @PathVariable("dictTypeCode") String dictTypeCode);

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.client;

import com.supermap.gaf.authority.commontype.AuthResourceApi;
import com.supermap.gaf.commontypes.MessageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;


/**
 * 权限控制模块api资源服务调用
 * @date:2021/3/25
 * @author wxl
 */
@FeignClient(name = "GAF-AUTHORITY"/*,url = "http://localhost:8081"*/, contextId = "authResourceApiClient")
public interface AuthResourceApiClient {
    // 资源api相关接口
    @GetMapping("/authority/auth-resource-apis/condition")
    MessageResult<List<AuthResourceApi>> list(@SpringQueryMap AuthResourceApi query);
    @PostMapping("/authority/auth-resource-apis")
    MessageResult<AuthResourceApi> insertAuthResourceApi(@RequestBody AuthResourceApi authResourceApi);

    @DeleteMapping("/authority/auth-resource-apis/{resourceApiId}")
    MessageResult<Void> deleteAuthResourceApi(@NotEmpty @PathVariable(value = "resourceApiId") String resourceApiId);

    @PutMapping("/authority/auth-resource-apis/{resourceApiId}")
    MessageResult<Void> updateAuthResourceApi(@RequestBody AuthResourceApi authResourceApi, @NotEmpty @PathVariable("resourceApiId") String resourceApiId);
}

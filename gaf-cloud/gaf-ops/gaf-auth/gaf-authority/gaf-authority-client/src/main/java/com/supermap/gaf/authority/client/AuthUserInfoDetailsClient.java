/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.client;

import com.supermap.gaf.authority.commontype.AuthUserInfoDetails;
import com.supermap.gaf.commontypes.MessageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;



/**
 * 用户信息查询接口
 * @author dqc
 */
@FeignClient(name = "GAF-AUTHORITY", contextId = "authUserInfoClient")
public interface AuthUserInfoDetailsClient {
    /**
     * 查询用户信息的调用接口
     * @return
     */
    @GetMapping("/authority/auth-user-details")
    MessageResult<AuthUserInfoDetails> getAuthUserInfoDetails(@RequestParam("username")String username);

}

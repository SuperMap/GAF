/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.client;

import com.supermap.gaf.authentication.entity.entity.AuthenticationParam;
import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.authentication.entity.entity.AuthorizationParam;
import com.supermap.gaf.commontypes.MessageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/23 5:43 PM
 */
@FeignClient(name = "GAF-AUTHENTICATION")
@Component
public interface ValidateClient {
    /**
     * 请求认证中心进行认证校验
     * @param authenticationParam
     * @return
     */
    @PostMapping(value = "/authentication/validate",consumes = APPLICATION_JSON_VALUE)
    MessageResult<AuthenticationResult> authentication(@RequestBody AuthenticationParam authenticationParam);

    /**
     * 请求认证中心进行鉴权验证
     * @return
     */
    @PostMapping(value = "/authentication/validate/authorization",consumes = APPLICATION_JSON_VALUE)
    Boolean authorization(@RequestBody AuthorizationParam authorizationParam);

}




/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authentication.service;


import com.supermap.gaf.authentication.entity.entity.AuthenticationParam;
import com.supermap.gaf.authentication.entity.entity.AuthenticationResult;
import com.supermap.gaf.authentication.entity.entity.AuthorizationParam;


/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/17 3:33 PM
 */
public interface ValidateAuthenticationService {

    /**
     * session和jwtToken获取认证结果
     *
     * @param authenticationParam
     * @return
     */
    AuthenticationResult authentication(AuthenticationParam authenticationParam);

    /**
     * 获取API鉴权结果
     *
     * @param authorizationParam
     * @return
     */
    Boolean authorization(AuthorizationParam authorizationParam);

}

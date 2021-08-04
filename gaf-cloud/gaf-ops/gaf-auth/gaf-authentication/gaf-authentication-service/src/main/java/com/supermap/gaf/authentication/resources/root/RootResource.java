/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authentication.resources.root;

import com.supermap.gaf.authentication.resources.*;
import io.swagger.annotations.Api;

import javax.ws.rs.Path;

/**
 * @author dqc
 * @date:2021/3/25
 */
@Path("/")
@Api("认证中心接口")
public class RootResource {

    @Path("/login")
    public Class<LoginResource> loginResourceClass() {
        return LoginResource.class;
    }

    @Path("/token")
    public Class<TokenResource> tokenResourceClass() {
        return TokenResource.class;
    }

    @Path("/user")
    public Class<UserResource> userResourceClass() {
        return UserResource.class;
    }

    @Path("/logout")
    public Class<LogoutResource> logoutResourceClass() {
        return LogoutResource.class;
    }

    @Path("/validate")
    public Class<ValidateResource> validateResourceClass() {
        return ValidateResource.class;
    }


}

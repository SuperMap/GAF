/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.authority.resources.root;

import com.supermap.gaf.authority.resources.*;
import io.swagger.annotations.Api;

import javax.ws.rs.Path;

/**
 * @author wxl
 * @date:2021/3/25
 */
@Path("/")
@Api("权限控制组件")
public class RootResource {

    @Path("/auth-tenants")
    public Class<AuthTenantResource> authTenantResource() {
        return AuthTenantResource.class;
    }

    @Path("/auth-departments")
    public Class<AuthDepartmentResource> authDepartmentResource() {
        return AuthDepartmentResource.class;
    }

    @Path("/auth-posts")
    public Class<AuthPostResource> authPostResource() {
        return AuthPostResource.class;
    }

    @Path("/auth-roles")
    public Class<AuthRoleResource> authRoleResource() {
        return AuthRoleResource.class;
    }

    @Path("/auth-users")
    public Class<AuthUserResource> authUserResourceClass() {
        return AuthUserResource.class;
    }

    @Path("/auth-user-role")
    public Class<AuthUserRoleResource> authUserRoleResource() {
        return AuthUserRoleResource.class;
    }

    @Path("/auth-role-api")
    public Class<AuthRoleApiResource> authRoleApiResource() {
        return AuthRoleApiResource.class;
    }


    @Path("/auth-role-menu")
    public Class<AuthRoleMenuResource> authRoleMenuResource() {
        return AuthRoleMenuResource.class;
    }

    @Path("/auth-resource-apis")
    public Class<AuthResourceApiResource> authResourceApiResource() {
        return AuthResourceApiResource.class;
    }


    @Path("/auth-resource-menus")
    public Class<AuthResourceMenuResource> authResourceMenuResource() {
        return AuthResourceMenuResource.class;
    }


    @Path("/auth-user-details")
    public Class<AuthUserInfoDetailsResource> authUserInfoDetailsResourceClass() {
        return AuthUserInfoDetailsResource.class;
    }


}

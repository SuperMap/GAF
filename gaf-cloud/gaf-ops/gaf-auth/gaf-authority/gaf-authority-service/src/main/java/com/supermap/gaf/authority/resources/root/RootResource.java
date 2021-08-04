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
@Api("系统与权限控制")
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


    @Path("/sys-components")
    public Class<SysComponentResource> sysComponentResource() {
        return SysComponentResource.class;
    }

    @Path("/auth-post-role")
    public Class<AuthPostRoleResource> authPostRoleResource() {
        return AuthPostRoleResource.class;
    }

    @Path("/auth-users")
    public Class<AuthUserResource> authUserResourceClass() {
        return AuthUserResource.class;
    }

    @Path("/auth-user-role")
    public Class<AuthUserRoleResource> authUserRoleResource() {
        return AuthUserRoleResource.class;
    }

    @Path("/auth-user-parttime")
    public Class<AuthUserParttimeResource> authUserParttimeResource() {
        return AuthUserParttimeResource.class;
    }

    @Path("/auth-role-api")
    public Class<AuthRoleApiResource> authRoleApiResource() {
        return AuthRoleApiResource.class;
    }

    @Path("/auth-role-module")
    public Class<AuthRoleModuleResource> authRoleModuleResource() {
        return AuthRoleModuleResource.class;
    }

    @Path("/auth-role-menu")
    public Class<AuthRoleMenuResource> authRoleMenuResource() {
        return AuthRoleMenuResource.class;
    }

    @Path("/auth-resource-apis")
    public Class<AuthResourceApiResource> authResourceApiResource() {
        return AuthResourceApiResource.class;
    }

    @Path("/auth-resource-modules")
    public Class<AuthResourceModuleResource> authResourceModuleResource() {
        return AuthResourceModuleResource.class;
    }

    @Path("/auth-resource-menus")
    public Class<AuthResourceMenuResource> authResourceMenuResource() {
        return AuthResourceMenuResource.class;
    }

    @Path("/auth-module-apis")
    public Class<AuthModuleApiResource> authModuleApiResource() {
        return AuthModuleApiResource.class;
    }


    @Path("/auth-p3-mapping-rule")
    public Class<AuthP3MappingRuleResource> authP3MappingRuleResource() {
        return AuthP3MappingRuleResource.class;
    }

    @Path("/auth-p3-tenant-mapping")
    public Class<AuthP3TenantMappingResource> authP3TenantMappingResource() {
        return AuthP3TenantMappingResource.class;
    }

    @Path("/auth-p3-sync-mapping")
    public Class<AuthP3SyncMappingResource> authP3SyncMappingResource() {
        return AuthP3SyncMappingResource.class;
    }


    @Path("/auth-user-details")
    public Class<AuthUserInfoDetailsResource> authUserInfoDetailsResourceClass() {
        return AuthUserInfoDetailsResource.class;
    }


}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.portal.resources.root;

import javax.ws.rs.Path;

import com.supermap.gaf.portal.resources.*;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;

/**
* @author:yw
 * @date:2021/3/25
* @Date 2021-3-12
**/
@Path("/")
@Api(value = "门户组件")
public class PortalRootResoure {

	@Path("/manager")
	public Class<ManagerPortalResoure> portalResource() {
		return ManagerPortalResoure.class;
	}

	@Path("/menus")
	public Class<MenuResource> menuResource() {
		return MenuResource.class;
	}

	@Path("/user/profile")
	public Class<UserProfileResource> userProfileResource() {
		return UserProfileResource.class;
	}

	@Path("/customization")
	public Class<CustomationResource> customationResource() {
		return CustomationResource.class;
	}

	@Path("/customizations")
	public Class<CustomationsResource> customationsResource() {
		return CustomationsResource.class;
	}

	@Path("/env")
	public Class<PropResource> envResourceClass() {
		return PropResource.class;
	}

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.configmgt.resources.root;

import com.supermap.gaf.configmgt.resources.ConfigurationsResource;
import io.swagger.annotations.Api;

import javax.ws.rs.Path;

/**
 * @date:2021/3/25
 * @author dqc
 */
@Path("/")
@Api(value = "配置中心管理Api")
public class ConfMgtRootResoure {

	@Path("/manager")
	public Class<ConfigurationsResource> configurationsResourceClass() {
		return ConfigurationsResource.class;
	}


}

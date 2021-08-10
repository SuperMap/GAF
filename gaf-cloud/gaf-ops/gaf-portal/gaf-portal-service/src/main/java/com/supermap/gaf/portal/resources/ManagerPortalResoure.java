/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.portal.resources;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

/**
 * @author:yw
 * @date:2021/3/25
 * @Date 2021-3-12
 **/
@Component
public class ManagerPortalResoure {

    @Path("/menus")
    public Class<ManagerMenuResource> menuResource() {
        return ManagerMenuResource.class;
    }

    @Path("/theme")
    public Class<ThemeResource> themeResource() {
        return ThemeResource.class;
    }

    @Path("/customization")
    public Class<ManagerCustomationResource> customationResource() {
        return ManagerCustomationResource.class;
    }
}

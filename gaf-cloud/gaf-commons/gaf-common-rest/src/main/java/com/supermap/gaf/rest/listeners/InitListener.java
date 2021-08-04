/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.rest.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.supermap.gaf.rest.utils.WebTool;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String webPath = sce.getServletContext().getRealPath("/");
        WebTool.setWebConfigPath(webPath);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //do nothings
    }

}

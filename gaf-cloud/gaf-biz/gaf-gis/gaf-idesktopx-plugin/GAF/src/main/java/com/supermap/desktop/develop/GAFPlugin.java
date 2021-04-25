/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop;

import com.supermap.data.Workspace;
import com.supermap.desktop.core.AbstractPlugin;
import com.supermap.desktop.core.PluginInfo;
import com.supermap.desktop.core.license.LicenseException;
import com.supermap.desktop.develop.utils.ApplicationContextUtils;
import com.supermap.desktop.develop.utils.CommonUtils;

/**
 * @author SuperMap
 * @date:2021/3/25
 * @time 2018/4/16 0016 上午 10:05
 */
public class GAFPlugin extends AbstractPlugin {

	public GAFPlugin(String name, PluginInfo pluginInfo) throws LicenseException {
		super(name, pluginInfo);
		Workspace workspace = ApplicationContextUtils.getWorkspace();
		workspace.addOpenedListener(event->{
			CommonUtils.checkGafPopMenuStatus();
		});
	}

	@Override
	public boolean isGranted() {
		return true;
	}

	@Override
	public String getPluginTitle() {
		return "GAF";
	}

	@Override
	public String getPluginName() {
		return "SuperMap.Desktop.GAF";
	}
}


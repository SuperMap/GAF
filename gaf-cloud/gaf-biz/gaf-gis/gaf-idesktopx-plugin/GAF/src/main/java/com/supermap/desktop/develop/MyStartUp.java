/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop;

import com.supermap.desktop.core.Application;

/**
 * @date:2021/3/25
 * @author SuperMap
 */
public class MyStartUp {
	public static void main(String[] args) {
		if (!Application.getActiveApplication().initialize()) {
			System.exit(0);
		}
	}
}

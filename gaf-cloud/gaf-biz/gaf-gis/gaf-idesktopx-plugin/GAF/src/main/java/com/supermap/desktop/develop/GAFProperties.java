/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop;

import com.supermap.desktop.core.Application;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author SuperMap
 * @date 2018/11/6 0006、上午 9:21
 * @description 获取词条
 */
public class GAFProperties {

	private static final String DEVELOP = "GAF";

	public static String getString(String key) {
		return getString(DEVELOP, key);
	}

	private static String getString(String baseName, String key) {
		String result = "";

		ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, Locale.getDefault());
		if (resourceBundle != null) {
			try {
				result = resourceBundle.getString("String_Alias");
				if(!key.equals("String_Alias")){
					result = resourceBundle.getString(key).replace("${alias}",result);
				}
			} catch (Exception e) {
				Application.getActiveApplication().getOutput().output(e);
			}
		}
		return result;
	}
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.desktop.develop;

import com.supermap.desktop.core.Application;
import com.supermap.desktop.core._XMLTag;
import com.supermap.desktop.core.utilties.FileUtilities;
import com.supermap.desktop.core.utilties.PathUtilities;
import com.supermap.desktop.core.utilties.XmlUtilities;
import com.supermap.desktop.develop.entity.GafGlobalEnvironments;
import com.supermap.desktop.frame.ctrlAction.settings.JDialogSettings;

/**
 * @author SuperMap
 * @date:2021/3/25
 */
public class MyStartUp {
    public static void main(String[] args) {

        if (!Application.getActiveApplication().initialize()) {
//			JDialogSettings
//			FileUtilities.getAppDataPath() + _XMLTag.SYSTEM_STARTUP_XML;
//			PathUtilities.getFullPathName("../" + _XMLTag.SYSTEM_STARTUP_XML, false);
//			XmlUtilities.getDocument(filePath);
            System.exit(0);
        }
        GafGlobalEnvironments.initResource();
    }
}

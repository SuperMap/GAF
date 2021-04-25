/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop.utils;

import com.supermap.data.Workspace;
import com.supermap.desktop.controls.ui.controls.DockbarManager;
import com.supermap.desktop.core.Application;
import com.supermap.desktop.core.Interface.IContextMenuManager;
import com.supermap.desktop.core.Interface.IOutput;
import com.supermap.desktop.dataview.framemenus.CtrlActionWorkspaceSaveAs;

/**
 * @date:2021/3/25
 * @author heykb
 */
public class ApplicationContextUtils {
    public static IOutput getOutput(){
        return Application.getActiveApplication().getOutput();
    }
    public static IContextMenuManager getContextMenuManager(){
        return Application.getActiveApplication().getMainFrame().getContextMenuManager();
    }
    public static DockbarManager getDockbarManager(){
        return (DockbarManager) Application.getActiveApplication().getMainFrame().getDockbarManager();
    }
    public static Workspace getWorkspace(){
        return Application.getActiveApplication().getWorkspace();
    }

    public static void print(String message){
        Application.getActiveApplication().getOutput().output(message);
    }
}

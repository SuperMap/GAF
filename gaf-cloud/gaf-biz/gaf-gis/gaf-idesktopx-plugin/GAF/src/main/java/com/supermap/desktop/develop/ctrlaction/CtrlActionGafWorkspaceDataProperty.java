/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.desktop.develop.ctrlaction;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.desktop.controls.property.WorkspaceTreeDataPropertyFactory;
import com.supermap.desktop.controls.ui.trees.NodeDataType;
import com.supermap.desktop.controls.ui.trees.TreeNodeData;
import com.supermap.desktop.core.Application;
import com.supermap.desktop.core.Interface.IBaseItem;
import com.supermap.desktop.core.Interface.IProperty;
import com.supermap.desktop.core.Interface.IPropertyManager;
import com.supermap.desktop.core.implement.CtrlAction;
import com.supermap.desktop.develop.ui.GafWorkspaceManager;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author SuperMap
 * @date:2021/3/25
 */
public class CtrlActionGafWorkspaceDataProperty extends CtrlAction {
    public CtrlActionGafWorkspaceDataProperty(IBaseItem caller) {
        super(caller);
    }

    @Override
    public void run() {
        Workspace workspace = new Workspace();
        WorkspaceConnectionInfo workspaceConnectionInfo = (WorkspaceConnectionInfo) ((DefaultMutableTreeNode) GafWorkspaceManager.gafWorkspaceManagerTree.getLastSelectedPathComponent()).getUserObject();
        workspace.open(workspaceConnectionInfo);
        TreeNodeData nodeData = new TreeNodeData(workspace, NodeDataType.WORKSPACE);
        IProperty[] properties = WorkspaceTreeDataPropertyFactory.createProperties(nodeData);
        IPropertyManager propertyManager = Application.getActiveApplication().getMainFrame().getPropertyManager();
        propertyManager.setProperty(properties);
        Application.getActiveApplication().getMainFrame().getPropertyManager().setPropertyVisible(true);
    }
}

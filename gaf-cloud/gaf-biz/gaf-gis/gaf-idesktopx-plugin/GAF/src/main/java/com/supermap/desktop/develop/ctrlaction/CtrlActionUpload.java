/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop.ctrlaction;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.desktop.controls.property.WorkspaceTreeDataPropertyFactory;
import com.supermap.desktop.controls.ui.UICommonToolkit;
import com.supermap.desktop.controls.ui.trees.NodeDataType;
import com.supermap.desktop.controls.ui.trees.TreeNodeData;
import com.supermap.desktop.controls.utilities.WorkspaceUIUtilities;
import com.supermap.desktop.core.Application;
import com.supermap.desktop.core.Interface.IBaseItem;
import com.supermap.desktop.core.Interface.IProperty;
import com.supermap.desktop.core.Interface.IPropertyManager;
import com.supermap.desktop.core.implement.CtrlAction;
import com.supermap.desktop.core.properties.CoreProperties;
import com.supermap.desktop.core.utilties.JOptionPaneUtilities;
import com.supermap.desktop.core.utilties.WorkspaceUtilities;
import com.supermap.desktop.develop.exception.FileUploadException;
import com.supermap.desktop.develop.ui.GafDatasourceManager;
import com.supermap.desktop.develop.utils.ApplicationContextUtils;
import com.supermap.desktop.develop.utils.CommonUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @date:2021/3/25
 * @author SuperMap
 */
public class CtrlActionUpload extends CtrlAction {
	public CtrlActionUpload(IBaseItem caller) {
		super(caller);
	}

    @Override
    public boolean enable() {
        return CommonUtils.online;
    }

    @Override
	public void run() {
        TreePath selectedPath = UICommonToolkit.getWorkspaceManager().getWorkspaceTree().getSelectionPath();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)selectedPath.getLastPathComponent();
        TreeNodeData data = (TreeNodeData)selectedNode.getUserObject();
        NodeDataType type = data.getType();
        try{
            if (type == NodeDataType.WORKSPACE) {
                handleWorkspace();
            } else if (type == NodeDataType.DATASOURCE) {
                handleDatasource((Datasource) data.getData());
            }
         }catch (FileUploadException e) {
            ApplicationContextUtils.getOutput().output(e.getMessage()+"上传失败");
        } catch (Exception e) {
            ApplicationContextUtils.getOutput().output(e.getMessage());
        }

	}
    void handleDatasource(Datasource datasource){
        DatasourceConnectionInfo connectionInfo = datasource.getConnectionInfo();
        if(!CommonUtils.isFileTypeSource(connectionInfo)){
            JOptionPaneUtilities.showErrorMessageDialog("要求是文件型");
        }else{
            if(CtrlActionSynchronizeWorkspace.checkDatasource(connectionInfo) == null){
                JOptionPaneUtilities.showErrorMessageDialog("未找到同名数据源，请注册，或者更改文件名！");
                return;
            }
            CommonUtils.uploadDatasource(datasource);

        }
    }
	void handleWorkspace(){
        Workspace oldWorkspace = ApplicationContextUtils.getWorkspace();
        if(WorkspaceUtilities.isWorkspaceModified()) {
            int result = JOptionPaneUtilities.showConfirmDialogWithCancel(CoreProperties.getString("String_SaveWorkspacePrompt"));
            if (result == 1 && CommonUtils.isFileTypeSource(oldWorkspace.getConnectionInfo())) {
                // 不保存
                Workspace workspace2 = new Workspace();
                workspace2.open(CommonUtils.copyWorkspaceConn(oldWorkspace.getConnectionInfo()));
                oldWorkspace = workspace2;
            } else if (result == 0) {
                // 保存
                WorkspaceUIUtilities.workspaceSave();
            } else if (result == 2 || result == -1) {
                return;
            }

        }
        WorkspaceConnectionInfo connectionInfo = oldWorkspace.getConnectionInfo();
	    if(!CommonUtils.isFileTypeSource(connectionInfo)){
            JOptionPaneUtilities.showErrorMessageDialog("类型不符合");
        }else{
            if(CtrlActionSynchronizeWorkspace.checkWorkspace(connectionInfo) == null){
                JOptionPaneUtilities.showErrorMessageDialog("未找到同名工作空间，请注册，或者更改文件名！");
                return;
            }
            CommonUtils.uploadWorkspace(oldWorkspace);
        }
    }
}

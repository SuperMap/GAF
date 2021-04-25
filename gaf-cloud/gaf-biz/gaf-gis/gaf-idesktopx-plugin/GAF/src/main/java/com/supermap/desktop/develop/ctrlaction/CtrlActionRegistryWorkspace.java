/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop.ctrlaction;

import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.desktop.controls.ui.controls.DialogResult;
import com.supermap.desktop.controls.utilities.WorkspaceUIUtilities;
import com.supermap.desktop.core.Interface.IBaseItem;
import com.supermap.desktop.core.implement.CtrlAction;
import com.supermap.desktop.core.properties.CoreProperties;
import com.supermap.desktop.core.utilties.JOptionPaneUtilities;
import com.supermap.desktop.core.utilties.StringUtilities;
import com.supermap.desktop.core.utilties.WorkspaceUtilities;
import com.supermap.desktop.dataview.framemenus.CtrlActionWorkspaceSaveAs;
import com.supermap.desktop.develop.GAFProperties;
import com.supermap.desktop.develop.client.GafClient;
import com.supermap.desktop.develop.exception.FileUploadException;
import com.supermap.desktop.develop.ui.GafWorkspaceManager;
import com.supermap.desktop.develop.utils.ApplicationContextUtils;
import com.supermap.desktop.develop.utils.CommonUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @date:2021/3/25
 * @author SuperMap
 */
public class CtrlActionRegistryWorkspace extends CtrlAction {

    public static boolean enable = false;
	public CtrlActionRegistryWorkspace(IBaseItem caller) {
		super(caller);
	}

    @Override
    public boolean enable() {
        return enable;
    }

    @Override
	public void run() {
        Workspace oldWorkspace = ApplicationContextUtils.getWorkspace();
        // 检查是否修改以及是否需要先保存
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
        // 先在工作空间树查询是否存在
        DefaultMutableTreeNode re = CtrlActionSynchronizeWorkspace.checkWorkspace(oldWorkspace.getConnectionInfo());
        
        if(re!=null){
            JOptionPaneUtilities.showMessageDialog("存在同名工作空间，请修改文件名！");
            return;
        }else{
            if(JOptionPaneUtilities.showConfirmDialog(GAFProperties.getString("String_WorkspaceRegistry"))==0){
                registry(oldWorkspace);
            }
        }
	}

    private void registry(Workspace oldWorkspace) {
        try {
            WorkspaceConnectionInfo connectionInfo = oldWorkspace.getConnectionInfo();

            GafClient.instance().registryWorkspace(connectionInfo,"datas/");
            CommonUtils.refreshWorkspaceTree();
            CommonUtils.uploadWorkspace(oldWorkspace);
        }catch (Exception e) {
            ApplicationContextUtils.getOutput().output(e.getMessage());
        }
    }

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop.ctrlaction;

import com.supermap.data.*;
import com.supermap.desktop.controls.utilities.WorkspaceUIUtilities;
import com.supermap.desktop.core.Interface.IBaseItem;
import com.supermap.desktop.core.implement.CtrlAction;
import com.supermap.desktop.core.properties.CoreProperties;
import com.supermap.desktop.core.utilties.DatasourceUtilities;
import com.supermap.desktop.core.utilties.JOptionPaneUtilities;
import com.supermap.desktop.core.utilties.WorkspaceUtilities;
import com.supermap.desktop.develop.exception.FileUploadException;
import com.supermap.desktop.develop.ui.GafDatasourceManager;
import com.supermap.desktop.develop.ui.GafWorkspaceManager;
import com.supermap.desktop.develop.utils.ApplicationContextUtils;
import com.supermap.desktop.develop.utils.CommonUtils;
import org.bouncycastle.jcajce.provider.digest.MD2;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * @date:2021/3/25
 * @author SuperMap
 */
public class CtrlActionSynchronizeWorkspace extends CtrlAction {
    public static boolean enable = false;
	public CtrlActionSynchronizeWorkspace(IBaseItem caller) {
		super(caller);
	}

    @Override
    public boolean enable() {
        return enable;
    }
    boolean checkStatus(){
        return false;
    }

    public static DefaultMutableTreeNode checkWorkspace(WorkspaceConnectionInfo curConn){
        try {
            CommonUtils.refreshWorkspaceTree();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DefaultMutableTreeNode re = CommonUtils.searchTree(GafWorkspaceManager.gafWorkspaceManagerTree.getTreeModel(), node -> {
            if(node.getUserObject() instanceof WorkspaceConnectionInfo ){
                WorkspaceConnectionInfo connectionInfo = (WorkspaceConnectionInfo) node.getUserObject();

                if(curConn.getType()==connectionInfo.getType()){
                    if(CommonUtils.isFileTypeSource(curConn)){
                        // 比较文件名
                        Optional<String> fileName = CommonUtils.getFileName(connectionInfo);
                        if(!fileName.isPresent()){
                            return false;
                        }
                        return Paths.get(curConn.getServer()).getFileName().toString().equals(fileName.get());
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }
            return false;
        });
        return re;
    }
    public static DefaultMutableTreeNode checkDatasource(DatasourceConnectionInfo curConn){
        try {
            CommonUtils.refreshDatasourceTree();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DefaultMutableTreeNode re = CommonUtils.searchTree(GafDatasourceManager.gafDatasourceManagerTree.getTreeModel(), node -> {
            if(node.getUserObject() instanceof DatasourceConnectionInfo ){
                DatasourceConnectionInfo connectionInfo = (DatasourceConnectionInfo) node.getUserObject();
                if(curConn.getEngineType()==connectionInfo.getEngineType()){
                    if(CommonUtils.isFileTypeSource(curConn)){
                        // 比较文件名
                        Optional<String> fileName = CommonUtils.getFileName(connectionInfo);
                        if(!fileName.isPresent()){
                            return false;
                        }
                        return Paths.get(curConn.getServer()).getFileName().toString().equals(fileName.get());
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }
            return false;
        });
        return re;
    }


    @Override
	public void run() {
	    if(enable()){

            Workspace oldWorkspace = ApplicationContextUtils.getWorkspace();
            // 检查是否修改以及是否需要先保存
            if(WorkspaceUtilities.isWorkspaceModified()) {
                int result = JOptionPaneUtilities.showConfirmDialogWithCancel(CoreProperties.getString("String_SaveWorkspacePrompt"));
                if (result == 1) {
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
            if(CommonUtils.isFileTypeSource(oldWorkspace.getConnectionInfo())){
                if(CtrlActionSynchronizeWorkspace.checkWorkspace(oldWorkspace.getConnectionInfo()) == null){
                    JOptionPaneUtilities.showErrorMessageDialog("未找到同名工作空间，请注册，或者更改文件名！");
                    return;
                }
            }
            CommonUtils.uploadWorkspace(oldWorkspace);
            Datasources datasources = oldWorkspace.getDatasources();
            for(int i=0;i<datasources.getCount();++i){
                Datasource datasource = datasources.get(i);
                DatasourceConnectionInfo dConn = datasource.getConnectionInfo();
                if(CommonUtils.isFileTypeSource(dConn)){
                    Path dPath = Paths.get(dConn.getServer());
                    if(checkDatasource(dConn)==null){
                        ApplicationContextUtils.getOutput().output(dPath.getFileName().toString()+"未注册");
                        continue;
                    }
                    CommonUtils.uploadDatasource(datasource);
                }
            }
        }
    }
}

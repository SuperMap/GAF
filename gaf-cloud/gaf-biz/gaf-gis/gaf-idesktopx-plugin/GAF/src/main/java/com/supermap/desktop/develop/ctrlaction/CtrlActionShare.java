/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop.ctrlaction;

import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.desktop.controls.dialog.SmOptionPane;
import com.supermap.desktop.controls.ui.UICommonToolkit;
import com.supermap.desktop.controls.ui.controls.SmFileChoose;
import com.supermap.desktop.core.FileChooseMode;
import com.supermap.desktop.core.Interface.IBaseItem;
import com.supermap.desktop.core.implement.CtrlAction;
import com.supermap.desktop.core.utilties.JOptionPaneUtilities;
import com.supermap.desktop.develop.client.GafClient;
import com.supermap.desktop.develop.ui.ShareDialog;
import com.supermap.desktop.develop.utils.ApplicationContextUtils;
import com.supermap.desktop.develop.utils.CommonUtils;

import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @date:2021/3/25
 * @author SuperMap
 */
public class CtrlActionShare extends CtrlAction {
    private Object conn;


    public Object getConn() {
        return conn;
    }

    public void setConn(Object conn) {
        this.conn = conn;
    }

    @Override
    public boolean enable() {
       return CommonUtils.isFileTypeSource(conn);
    }

    public CtrlActionShare(IBaseItem caller) {
		super(caller);
	}

    @Override
	public void run() {
        if(enable()){
            Optional<String> fileName = CommonUtils.getFileName(conn);
            if(!fileName.isPresent()){
                JOptionPaneUtilities.showErrorMessageDialog("无法分享该文件");
                return;
            }
            if(CommonUtils.tipIfNotExist("/datas/"+fileName.get())){
                return;
            }
            try {
                String url = GafClient.instance().downloadPresignUrl("/datas/"+fileName.get());
                ShareDialog dialog = new ShareDialog();
                dialog.setTitle("15分钟内有效");
                dialog.showMessageDialog(url);
            } catch (Exception e) {
                JOptionPaneUtilities.showMessageDialog("获取分享链接失败："+e.getMessage());
            }
        }

	}
}

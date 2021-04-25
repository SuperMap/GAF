/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop.ctrlaction;

import com.supermap.data.Datasource;
import com.supermap.data.Datasources;
import com.supermap.data.Workspace;
import com.supermap.desktop.core.Interface.IBaseItem;
import com.supermap.desktop.core.implement.CtrlAction;
import com.supermap.desktop.develop.entity.Catalog;
import com.supermap.desktop.develop.entity.SelectableDatasource;
import com.supermap.desktop.develop.ui.DialogSelectDatasource;
import com.supermap.desktop.develop.ui.GafDatasourceManager;
import com.supermap.desktop.develop.utils.ApplicationContextUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

/**
 * @date:2021/3/25
 * @author SuperMap
 */
public class CtrlActionDatasourceRegistry extends CtrlAction {
	public CtrlActionDatasourceRegistry(IBaseItem caller) {
		super(caller);
	}

    @Override
	public void run() {
        Catalog catalog = (Catalog) ((DefaultMutableTreeNode) GafDatasourceManager.gafDatasourceManagerTree.getLastSelectedPathComponent()).getUserObject();
        Workspace workspace = ApplicationContextUtils.getWorkspace();
        Datasources datasources= workspace.getDatasources();
        List<SelectableDatasource> datasourceList = new ArrayList<>();
        for(int i=0;i<datasources.getCount();++i){
            if(datasources.get(i).isOpened()){
                datasourceList.add(new SelectableDatasource(datasources.get(i)));
            }
        }
        new DialogSelectDatasource(datasourceList,catalog).showDialog();
	}
}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop.ui;


import com.supermap.desktop.controls.ui.controls.button.SmButton;
import com.supermap.desktop.core.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.dataview.DataViewResources;
import com.supermap.desktop.develop.utils.CommonUtils;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;

/**
 * @date:2021/3/25
 * @author heykb
 */
public class GafDatasourceManager extends JPanel{
    public static GafTree gafDatasourceManagerTree;
    private JScrollPane jScrollPane;
    private SmButton refreshButton;
//    AttributeMan
    public void updateTree(GafTree tree) {
        remove(jScrollPane);
        remove(refreshButton);
        repaint();
        initComponent(tree);
        revalidate();

    }
    void initComponent(GafTree tree){
        gafDatasourceManagerTree = tree;
        this.jScrollPane = new JScrollPane(tree);
        this.setLayout(new GridBagLayout());
        this.refreshButton = new SmButton();
        this.refreshButton.setIcon(DataViewResources.getIcon("/dataviewresources/Catalog/Image_Refresh.png"));
        this.add(this.refreshButton, (new GridBagConstraintsHelper(0, 1)).setFill(BOTH));
        this.add(jScrollPane, new GridBagConstraintsHelper(0, 0).setWeight(1, 1).setFill(BOTH));
        this.refreshButton.addActionListener(e->{
            try {
                CommonUtils.refreshDatasourceTree();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
//        tree.addTreeSelectionListener(new TreeSelectionListener(){
//            @Override
//            public void valueChanged(TreeSelectionEvent e) {
//                DefaultMutableTreeNode node= (DefaultMutableTreeNode) tree.getLastSelectedPathComponent(); //返回最后选中的结点
//                String name=node.toString();//获得这个结点的名称
//                if(node.isLeaf()){
//                   clickHandler(node);
//                }else{
//                    tree.expandPath(new TreePath(node.getPath()));
//                }
//            }
//        });
    }
    public GafDatasourceManager() {
        initComponent(new GafTree(null, GafTree.GafTreeType.DATASOURCE_TREE));
    }



}

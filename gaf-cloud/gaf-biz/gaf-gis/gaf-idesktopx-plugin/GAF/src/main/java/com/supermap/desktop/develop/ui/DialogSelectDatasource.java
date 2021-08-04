/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.desktop.develop.ui;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.desktop.controls.ui.controls.DialogResult;
import com.supermap.desktop.controls.ui.controls.SmDialog;
import com.supermap.desktop.controls.ui.controls.button.SmButton;
import com.supermap.desktop.controls.ui.controls.smTables.TableHeaderCheckBoxCellRender;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.core.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.core.utilties.CoreResources;
import com.supermap.desktop.develop.client.GafClient;
import com.supermap.desktop.develop.entity.Catalog;
import com.supermap.desktop.develop.entity.SelectableDatasource;
import com.supermap.desktop.develop.utils.ApplicationContextUtils;
import com.supermap.desktop.develop.utils.CommonUtils;
import sun.tools.jar.resources.jar;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

import static java.awt.GridBagConstraints.BOTH;

/**
 * @author heykb
 * @date:2021/3/25
 */
public class DialogSelectDatasource extends SmDialog {
    // 面板
    private JPanel panel;
    private SmButton buttonOK;
    private SmButton buttonCancel;
    private java.util.List<SelectableDatasource> datasources;
    private Catalog catalog;
    private JTable table;
    private JScrollPane scrollPane;

    public DialogSelectDatasource(java.util.List<SelectableDatasource> datasources, Catalog catalog) {
//        DialogServerReleaseWorkspace
        super();
        this.datasources = datasources;
        this.catalog = catalog;
        // 设置对话框大小
        this.setSize(new Dimension(500, 250));
        // 新建各个组件
        initComponents();

        // 组装ui组件
        initLayout();
        // 使用国际化资源设置标题、文本内容
        initResources();
        registerEvents();
        this.componentList.add(this.buttonOK);
        this.componentList.add(this.buttonCancel);
        this.setFocusTraversalPolicy(this.policy);
    }


    private void initComponents() {
        //  FlowLayout布局管理器创建新面板
        this.panel = new JPanel();
        // 确定按钮
        this.buttonOK = new SmButton();
        buttonOK.setText("确定");
        // 关闭
        this.buttonCancel = ComponentFactory.createButtonCancel();
        this.table = new JTable();
        this.table.setModel(new DatasourceSelectTableModel());
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, r);

        table.getColumnModel().getColumn(0).setMaxWidth(30);
        MyCheckBoxRenderer check = new MyCheckBoxRenderer();
        check.setHorizontalAlignment(JLabel.CENTER);
        table.getColumn("select").setHeaderRenderer(check);
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (table.getColumnModel().getColumnIndexAtX(e.getX()) == 0) {//如果点击的是第0列，即checkbox这一列
                    JCheckBox Checkbox = (JCheckBox) check;
                    boolean b = !check.isSelected();
                    check.setSelected(b);
                    table.getTableHeader().repaint();
                    for (int i = 0; i < table.getRowCount(); i++) {
                        table.getModel().setValueAt(b, i, 0);//把这一列都设成和表头一样
                    }
                }
            }
        });
        scrollPane = new JScrollPane(this.table);

    }

    private void initLayout() {
        // 向面板插入主要元素
        initLayoutPanelSourceData();

        // 将面板插入对话框
        this.setLayout(new GridBagLayout());
        this.add(this.scrollPane, new GridBagConstraintsHelper(0, 0, 1, 1).setInsets(10, 10, GridBagConstraintsHelper.FRAME_CONTROL_GAP, GridBagConstraintsHelper.FRAME_CONTROL_GAP).setWeight(1, 1).setFill(BOTH));// 对话框插入按钮
        this.add(ComponentFactory.createButtonPanel(this.buttonOK, this.buttonCancel), new GridBagConstraintsHelper(0, 3, 1, 1).setInsets(0, GridBagConstraintsHelper.FRAME_CONTROL_GAP, GridBagConstraintsHelper.FRAME_CONTROL_GAP, GridBagConstraintsHelper.FRAME_CONTROL_GAP).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
    }

    private void initLayoutPanelSourceData() {
        // 设置面板布局为网格
        this.panel.setLayout(new GridBagLayout());
    }


    private void initResources() {
        // 设置对话框标题
        this.setTitle("选择");
    }

    private void registerEvents() {

        // 按钮添加事件

        this.buttonOK.addActionListener(this.actionListenerOK);
        this.buttonCancel.addActionListener(this.actionListenerCancel);
    }


    private final ActionListener actionListenerOK = e -> {
        run();
        setDialogResult(DialogResult.CANCEL);
        dispose();
    };

    private final ActionListener actionListenerCancel = e -> dispose();


    private void run() {
        try {
            CommonUtils.refreshDatasourceTree();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DefaultMutableTreeNode re = CommonUtils.searchTree(GafDatasourceManager.gafDatasourceManagerTree.getTreeModel(), node -> {
            if (node.getUserObject() instanceof DatasourceConnectionInfo) {
                DatasourceConnectionInfo connectionInfo = (DatasourceConnectionInfo) node.getUserObject();
                for (SelectableDatasource selectableDatasource : datasources) {
                    if (selectableDatasource.isSelect()) {
                        DatasourceConnectionInfo curConn = selectableDatasource.getDatasource().getConnectionInfo();
                        if (curConn.getEngineType() == connectionInfo.getEngineType()) {
                            if (CommonUtils.isFileTypeSource(curConn)) {
                                // 比较文件名
                                Optional<String> fileName = CommonUtils.getFileName(connectionInfo);
                                if (!fileName.isPresent()) {
                                    return false;
                                }
                                boolean result = Paths.get(curConn.getServer()).getFileName().toString().equals(fileName.get());
                                if (result) {
                                    ApplicationContextUtils.getOutput().output(selectableDatasource.getDatasource().getAlias() + "注册失败：文件已存在");
                                    selectableDatasource.setSelect(false);
                                }
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                }

            }
            return false;
        });
        for (SelectableDatasource selectableDatasource : datasources) {
            if (selectableDatasource.isSelect()) {
                Datasource datasource = selectableDatasource.getDatasource();
                DatasourceConnectionInfo connectionInfo = datasource.getConnectionInfo();
                try {
                    GafClient.instance().registryDatasource(connectionInfo, catalog.getCatalogCode(), "datas/");
                    CommonUtils.uploadDatasource(datasource);
                    ApplicationContextUtils.getOutput().output(datasource.getAlias() + "数据源注册成功");
                } catch (Exception e) {
                    ApplicationContextUtils.getOutput().output(datasource.getAlias() + "数据源注册失败");
                }

            }
        }
        try {
            CommonUtils.refreshDatasourceTree();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class DatasourceSelectTableModel extends DefaultTableModel {


        @Override
        public String getColumnName(int column) {
            if (column == 0) {
                return "select";
            } else if (column == 1) {
                return " 数据源";
            } else {
                return "  类型";
            }
        }


        @Override
        public int getRowCount() {
            return DialogSelectDatasource.this.datasources.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public Object getValueAt(int row, int column) {
            SelectableDatasource selectableDatasource = DialogSelectDatasource.this.datasources.get(row);
            Datasource datasource = selectableDatasource.getDatasource();
            if (column == 0) {
                return selectableDatasource.isSelect();
            } else if (column == 1) {
                return "  " + datasource.getAlias();
            } else {
                return "  " + datasource.getEngineType().name();
            }


        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Boolean.class;
            } else {
                return String.class;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 0;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                SelectableDatasource selectableDatasource = DialogSelectDatasource.this.datasources.get(rowIndex);
                selectableDatasource.setSelect((Boolean) aValue);
                fireTableCellUpdated(rowIndex, columnIndex);
            }
        }
    }

    static class MyCheckBoxRenderer extends JCheckBox implements TableCellRenderer {

        public MyCheckBoxRenderer() {
            this.setBorderPainted(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
}

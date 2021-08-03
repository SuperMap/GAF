/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.desktop.develop.ui;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.data.*;
import com.supermap.desktop.controls.ui.controls.SmFileChoose;
import com.supermap.desktop.controls.utilities.WorkspaceTreeManagerUIUtilities;
import com.supermap.desktop.core.Application;
import com.supermap.desktop.core.FileChooseMode;
import com.supermap.desktop.core.Interface.IBaseItem;
import com.supermap.desktop.core.implement.SmPopupMenu;
import com.supermap.desktop.core.utilties.JOptionPaneUtilities;
import com.supermap.desktop.core.utilties.WorkspaceUtilities;
import com.supermap.desktop.develop.client.GafClient;
import com.supermap.desktop.develop.ctrlaction.CtrlActionDownload;
import com.supermap.desktop.develop.ctrlaction.CtrlActionShare;
import com.supermap.desktop.develop.entity.Catalog;
import com.supermap.desktop.develop.exception.FileDownloadException;
import com.supermap.desktop.develop.utils.ApplicationContextUtils;
import com.supermap.desktop.develop.utils.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTreeCellRenderer;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author heykb
 * @date:2021/3/25
 */
public class GafTree extends JTree {
    private SmPopupMenu leafPopupMenu;
    private SmPopupMenu parentPopupMenu;
    private SmFileChoose smFileChoose;

    public enum GafTreeType {
        WORKSPACE_TREE, DATASOURCE_TREE, EMPTY;
    }

    private TreeModel treeModel;
    private GafTreeType treeType;

    public GafTree() {
        this(null, GafTreeType.EMPTY);
    }

    public TreeModel getTreeModel() {
        return treeModel;
    }

    public GafTree(JSONArray list, GafTreeType type) {
        this.treeType = type;
        initFileChoose();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        buildTree(root, list, type);
        treeModel = new DefaultTreeModel(root);
        setModel(treeModel);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setShowsRootHandles(true);
        setCellRenderer(new TreeCellRender());
        setRootVisible(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent evt) {
                treeMousePressed(evt);
            }
        });
        expandRow(0);
        expandRow(1);
        expandRow(2);
        expandRow(3);
        expandRow(4);
        expandRow(5);
    }

    private void initFileChoose() {
        this.smFileChoose = new SmFileChoose("", FileChooseMode.SAVE_ONE, ".");
        this.smFileChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.smFileChoose.setTitle("下载工作空间到");
    }

    public void buildTree(DefaultMutableTreeNode root, JSONArray list, GafTreeType type) {
        if (list != null && list.size() > 0) {
            switch (type) {
                case DATASOURCE_TREE: {
                    buildDatasourceTree(root, list);
                    break;
                }
                case WORKSPACE_TREE: {
                    buildWorkSpaceTree(root, list);
                    break;
                }
                default:
                    break;
            }
        } else {
            buildDatasourceTree(root, list);
        }
    }

    public void buildDatasourceTree(DefaultMutableTreeNode root, JSONArray list) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); ++i) {
                JSONObject node = list.getJSONObject(i);
                recursionBuildTree(node, root);
            }
        }
    }

    boolean isCatalog(JSONObject node) {
        if (node.getBoolean("leaf") && node.getJSONObject("userObject") != null && StringUtils.isEmpty(node.getJSONObject("userObject").getString("dictTypeCode"))) {
            return false;
        } else {
            return true;
        }
    }

    private void recursionBuildTree(JSONObject node, DefaultMutableTreeNode parent) {
        if (isCatalog(node)) {
            Catalog catalog = new Catalog(node.getString("title"), node.getJSONObject("userObject").getString("value"));
            DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(catalog);
            parent.add(treeNode);
            JSONArray children = node.getJSONArray("children");
            if (children != null) {
                for (int i = 0; i < children.size(); ++i) {
                    recursionBuildTree(children.getJSONObject(i), treeNode);
                }
            }
            // 默认展开
        } else {
            JSONObject conn = node.getJSONObject("userObject");
            if (conn != null) {
                DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
                connectionInfo.setServer(conn.getString("addr"));
                connectionInfo.setAlias(conn.getString("dsName"));
                connectionInfo.setDatabase(conn.getString("dbName"));
                connectionInfo.setUser(conn.getString("userName"));
                connectionInfo.setPassword(conn.getString("password"));
                try {
                    connectionInfo.setEngineType((EngineType) EngineType.parse(EngineType.class, conn.getString("typeCode")));
                } catch (RuntimeException e) {
                    ApplicationContextUtils.getOutput().output(String.format("%s数据源连接信息解析失败，不存在类型%s", conn.getString("dsName"), conn.getString("typeCode")));
                }
                if (EngineType.SQLPLUS == connectionInfo.getEngineType()) {
                    connectionInfo.setDriver("SQL SERVER");
                }
                parent.add(new DefaultMutableTreeNode(connectionInfo));
            } else {
                parent.add(new DefaultMutableTreeNode(node.getString("title")));
            }
        }
    }

    public void buildWorkSpaceTree(DefaultMutableTreeNode root, JSONArray list) {
        if (list != null && list.size() > 0) {
            DefaultMutableTreeNode ConnTypeNode = new DefaultMutableTreeNode("数据连接型");
            DefaultMutableTreeNode fileTypeNode = new DefaultMutableTreeNode("文件型");
            root.add(ConnTypeNode);
            root.add(fileTypeNode);
            for (int i = 0; i < list.size(); ++i) {
                JSONObject workspaceConn = list.getJSONObject(i);
                WorkspaceConnectionInfo connectionInfo = new WorkspaceConnectionInfo();

                connectionInfo.setServer(workspaceConn.getString("server"));
                connectionInfo.setUser(workspaceConn.getString("userName"));
                connectionInfo.setPassword(workspaceConn.getString("password"));
                connectionInfo.setDatabase(workspaceConn.getString("database"));
                connectionInfo.setName(workspaceConn.getString("wsName"));
                try {
                    connectionInfo.setType((WorkspaceType) WorkspaceType.parse(WorkspaceType.class, workspaceConn.getString("typeCode")));
                } catch (RuntimeException e) {
                    connectionInfo.setName("❌" + connectionInfo.getName());

                    ApplicationContextUtils.getOutput().output(String.format("%s工作空间连接信息解析失败，不存在类型%s", workspaceConn.getString("wsName"), workspaceConn.getString("typeCode")));
                }
                if (WorkspaceType.SQL.equals(connectionInfo.getType())) {
                    connectionInfo.setDriver("SQL SERVER");
                }
                if (WorkspaceType.SMWU.equals(connectionInfo.getType())
                        || WorkspaceType.SXWU.equals(connectionInfo.getType())
                        || WorkspaceType.SMW.equals(connectionInfo.getType())
                        || WorkspaceType.SXW.equals(connectionInfo.getType())
                ) {
                    fileTypeNode.add(new DefaultMutableTreeNode(connectionInfo));
                } else {
                    ConnTypeNode.add(new DefaultMutableTreeNode(connectionInfo));
                }

            }
        }
    }

    private void treeMousePressed(MouseEvent evt) {
        try {
            int clickCount = evt.getClickCount();
            int selRow = getRowForLocation(evt.getX(), evt.getY());
            TreePath selPath = getPathForLocation(evt.getX(), evt.getY());
            if (selPath == null) {
                return;
            }
            setSelectionPath(selPath);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
            if (selRow != -1) {
                if (SwingUtilities.isLeftMouseButton(evt) && clickCount == 2) {
                    //双击事件
                    doubleClickHandler(node);
                }
                if (SwingUtilities.isLeftMouseButton(evt) && clickCount == 1) {
                    // 点击
                    if (!node.isLeaf()) {
                        if (isExpanded(selPath)) {
                            collapsePath(selPath);
                        } else {
                            expandPath(selPath);
                        }
                    }
                }
                if (SwingUtilities.isRightMouseButton(evt) && clickCount == 1) {
                    //右键单击
                    JPopupMenu popupMenu = getPopMenu(node);
                    if (popupMenu != null) {
                        popupMenu.show(this, evt.getX(), evt.getY());
                    }
                }
            }
        } catch (Exception e) {
            Application.getActiveApplication().getOutput().output(e);
        }

    }

    private void doubleClickHandler(WorkspaceConnectionInfo connectionInfo) {
        try {
            if (CommonUtils.isFileTypeSource(connectionInfo)) {
                Optional<String> fileName = CommonUtils.getFileName(connectionInfo);
                if (!fileName.isPresent()) {
                    JOptionPaneUtilities.showErrorMessageDialog("无法下载该文件");
                    return;
                }
                if (CommonUtils.tipIfNotExist("/datas/" + fileName.get())) {
                    return;
                }
                int re = CommonUtils.dirChoose(smFileChoose, fileName.get());
                if (re == -1) {
                    return;
                } else if (re == 0) {
                    Path downloadPath = Paths.get(smFileChoose.getFilePath() + "/" + fileName.get());
                    connectionInfo.setServer(downloadPath.toString());
                    // todo: 执行下载
                    CommonUtils.downloadAsync("/datas/" + downloadPath.getFileName(), downloadPath, evt -> {
                        if (evt.getPropertyName().equals("progress")) {
                            Integer progress = (Integer) evt.getNewValue();
                            if (progress % 10 == 0) {
                                ApplicationContextUtils.getOutput().output("已下载" + downloadPath.getFileName().toString() + ":" + progress + "%");
                            }
                            if (progress == 100) {
                                ApplicationContextUtils.getOutput().output(downloadPath.getFileName().toString() + "下载成功");
                                WorkspaceUtilities.openWorkspace(connectionInfo, true);
                                ApplicationContextUtils.getOutput().output("工作空间打开成功！");
                            }
                        }
                    });
                    return;
                }
            }
            if (WorkspaceType.SQL.equals(connectionInfo.getType())) {
                connectionInfo.setDriver("SQL SERVER");
            }
            Workspace workspaceTemp = new Workspace();
            boolean openResult = workspaceTemp.open(connectionInfo);
            if (openResult) {
                workspaceTemp.close();
                if (WorkspaceUtilities.closeWorkspace()) {
                    ApplicationContextUtils.getWorkspace().open(connectionInfo);
                    ApplicationContextUtils.getWorkspace().setCaption(connectionInfo.getName());
                    ApplicationContextUtils.getOutput().output("工作空间打开成功！");
                }
            } else {
                ApplicationContextUtils.getOutput().output("工作空间打开失败！");
            }
        } catch (FileDownloadException e) {
            ApplicationContextUtils.getOutput().output(e.getMessage() + "下载失败");
        } catch (Exception e) {
            ApplicationContextUtils.getOutput().output(e.getMessage());
        }
    }

    private void doubleClickHandler(DatasourceConnectionInfo connectionInfo) {
        try {
            if (CommonUtils.isFileTypeSource(connectionInfo)) {
                Optional<String> fileName = CommonUtils.getFileName(connectionInfo);
                if (!fileName.isPresent()) {
                    JOptionPaneUtilities.showErrorMessageDialog("无法下载该文件");
                    return;
                }
                if (CommonUtils.tipIfNotExist("/datas/" + fileName.get())) {
                    return;
                }
                int re = CommonUtils.dirChoose(smFileChoose, fileName.get().toString());
                if (re == -1) {
                    return;
                } else if (re == 0) {
                    Path downloadPath = Paths.get(smFileChoose.getFilePath() + "/" + fileName.get());

                    Path downloadPath2 = downloadPath;
                    if (downloadPath.toString().endsWith(".udb")) {
                        String uddFileName = fileName.get().substring(0, fileName.get().length() - 1) + "d";
                        Path uddDownloadPath = downloadPath2.getParent().resolve(uddFileName);
                        CommonUtils.downloadAsync("/datas/" + uddDownloadPath.getFileName(), uddDownloadPath, evt -> {
                            if (evt.getPropertyName().equals("progress")) {
                                Integer progress = (Integer) evt.getNewValue();
                                if (progress % 10 == 0) {
                                    ApplicationContextUtils.getOutput().output("已下载" + uddDownloadPath.getFileName().toString() + ":" + progress + "%");
                                }
                                if (progress == 100) {
                                    ApplicationContextUtils.getOutput().output(uddDownloadPath.getFileName().toString() + "下载成功");
                                    downloadAndOpen(downloadPath, connectionInfo);
                                }
                            }
                        });
                    } else {
                        downloadAndOpen(downloadPath, connectionInfo);
                    }
                }
            } else {
                ApplicationContextUtils.getWorkspace().getDatasources().open(connectionInfo);
                ApplicationContextUtils.getOutput().output("数据源打开成功！");
            }
        } catch (FileDownloadException e) {
            ApplicationContextUtils.getOutput().output(e.getMessage() + "下载失败");
        } catch (Throwable e) {
            ApplicationContextUtils.getOutput().output(e.getMessage());
        }
    }

    private void downloadAndOpen(Path downloadPath, DatasourceConnectionInfo connectionInfo) {
        try {
            connectionInfo.setServer(downloadPath.toString());
            CommonUtils.downloadAsync("/datas/" + downloadPath.getFileName(), downloadPath, evt -> {
                if (evt.getPropertyName().equals("progress")) {
                    Integer progress = (Integer) evt.getNewValue();
                    if (progress % 10 == 0) {
                        ApplicationContextUtils.getOutput().output("已下载" + downloadPath.getFileName().toString() + ":" + progress + "%");
                    }
                    if (progress == 100) {
                        ApplicationContextUtils.getOutput().output(downloadPath.getFileName().toString() + "下载成功");
                        ApplicationContextUtils.getWorkspace().getDatasources().open(connectionInfo);
                        ApplicationContextUtils.getOutput().output("数据源打开成功！");
                    }
                }
            });
        } catch (FileDownloadException e) {
            ApplicationContextUtils.getOutput().output(e.getMessage() + "下载失败");
        } catch (Throwable e) {
            ApplicationContextUtils.getOutput().output(e.getMessage());
        }
    }

    private void doubleClickHandler(DefaultMutableTreeNode node) {
        if (node.getUserObject() instanceof WorkspaceConnectionInfo) {
            WorkspaceConnectionInfo connectionInfo = (WorkspaceConnectionInfo) node.getUserObject();
            doubleClickHandler(connectionInfo);
        }
        if (node.getUserObject() instanceof DatasourceConnectionInfo) {
            DatasourceConnectionInfo connectionInfo = (DatasourceConnectionInfo) node.getUserObject();
            doubleClickHandler(connectionInfo);
        }
    }

    private static class TreeCellRender extends SubstanceDefaultTreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) value;
            Object userObject = defaultMutableTreeNode.getUserObject();
            if (userObject instanceof String) {
                setIcon(WorkspaceTreeManagerUIUtilities.getIconByPath(WorkspaceTreeManagerUIUtilities.SYMBOL_ICON));
                setText((String) userObject);
            }
            if (userObject instanceof Catalog) {
                Catalog catalog = (Catalog) userObject;
                setIcon(WorkspaceTreeManagerUIUtilities.getIconByPath(WorkspaceTreeManagerUIUtilities.SYMBOL_ICON));
                setText(catalog.getCatalogName());
            }
            if (userObject instanceof WorkspaceConnectionInfo) {
                WorkspaceConnectionInfo connectionInfo = (WorkspaceConnectionInfo) userObject;
                setIcon(CommonUtils.getICon(connectionInfo.getType()));
                if (CommonUtils.isFileTypeSource(userObject)) {
                    Optional<String> fileName = CommonUtils.getFileName(connectionInfo);
                    if (fileName.isPresent()) {
                        setText(fileName.get());
                    } else {
                        setText("❌" + connectionInfo.getServer());
                    }
                } else {
                    setText(connectionInfo.getName());
                }
            }
            if (userObject instanceof DatasourceConnectionInfo) {
                DatasourceConnectionInfo connectionInfo = (DatasourceConnectionInfo) userObject;
                setIcon(CommonUtils.getICon(connectionInfo.getEngineType()));
                if (CommonUtils.isFileTypeSource(userObject)) {
                    Optional<String> fileName = CommonUtils.getFileName(connectionInfo);
                    if (fileName.isPresent()) {
                        setText(fileName.get());
                    } else {
                        setText("❌" + connectionInfo.getServer());
                    }
                } else {
                    setText(connectionInfo.getAlias());
                }
            }

            return this;
        }

    }

    JPopupMenu getPopMenu(DefaultMutableTreeNode treeNode) {
        SmPopupMenu popupMenu = null;
        if (GafTreeType.DATASOURCE_TREE == treeType && treeNode.getUserObject() instanceof Catalog) {
            this.parentPopupMenu = this.parentPopupMenu != null ? this.parentPopupMenu : ApplicationContextUtils.getContextMenuManager().get("SuperMap.Desktop.UI.GafDatasourceListManager.ContextMenuDatasourceParent");
            popupMenu = this.parentPopupMenu;
        } else {
            if (GafTreeType.WORKSPACE_TREE == treeType) {
                this.leafPopupMenu = this.leafPopupMenu != null ? this.leafPopupMenu : ApplicationContextUtils.getContextMenuManager().get("SuperMap.Desktop.UI.GafWorkspaceListManager.ContextMenuWorkspace");
                popupMenu = this.leafPopupMenu;
            } else if (GafTreeType.DATASOURCE_TREE == treeType) {
                this.leafPopupMenu = this.leafPopupMenu != null ? this.leafPopupMenu : ApplicationContextUtils.getContextMenuManager().get("SuperMap.Desktop.UI.GafDatasourceListManager.ContextMenuDatasource");
                popupMenu = this.leafPopupMenu;
            } else {
                return null;
            }
            for (IBaseItem iBaseItem : this.leafPopupMenu.items()) {
                if ("download".equals(iBaseItem.getID()) || "share".equals(iBaseItem.getID())) {
                    if (iBaseItem.getCtrlAction().getCurrentCtrlAction() instanceof CtrlActionDownload) {
                        CtrlActionDownload actionDownload = (CtrlActionDownload) iBaseItem.getCtrlAction().getCurrentCtrlAction();
                        actionDownload.setConn(treeNode.getUserObject());
                    }
                    if (iBaseItem.getCtrlAction().getCurrentCtrlAction() instanceof CtrlActionShare) {
                        CtrlActionShare actionDownload = (CtrlActionShare) iBaseItem.getCtrlAction().getCurrentCtrlAction();
                        actionDownload.setConn(treeNode.getUserObject());
                    }
                }
            }
        }
        return popupMenu;
    }
}

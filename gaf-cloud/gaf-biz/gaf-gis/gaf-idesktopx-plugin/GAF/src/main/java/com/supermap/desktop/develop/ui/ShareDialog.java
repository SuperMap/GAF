/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.desktop.develop.ui;

import com.supermap.desktop.controls.ui.controls.SmDialog;
import com.supermap.desktop.controls.ui.controls.button.SmButton;
import com.supermap.desktop.core.GlobalParameters;
import com.supermap.desktop.core.properties.CoreProperties;
import com.supermap.desktop.core.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.core.utilties.SystemPropertyUtilities;
import com.supermap.desktop.develop.GAFProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author heykb
 * @date:2021/3/25
 */
public class ShareDialog extends SmDialog {
    private JPanel panelButton;
    private JCheckBox checkBox;
    private SmButton buttonYes;
    private SmButton buttonNo;
    private SmButton buttonCancel;
    private JTextArea textAreaMessage;
    private int result = -1;
    private String defaultTitle = GlobalParameters.getDesktopTitle();
    private static final Dimension size = new Dimension((int) (600.0D * SystemPropertyUtilities.getSystemSizeRate()), (int) (160.0D * SystemPropertyUtilities.getSystemSizeRate()));

    public ShareDialog() {
        this.init();
    }

    public boolean isCheckBoxSelected() {
        return this.checkBox.isSelected() && this.checkBox.isVisible() && this.checkBox.isEnabled();
    }

    public ShareDialog(JFrame owner) {
        super(owner);
        this.init();
    }

    public ShareDialog(JDialog owner) {
        super(owner);
        this.init();
    }

    private void init() {
        this.initComponents();
        this.initLayout();
        this.addListeners();
        this.initResources();
        this.initComponentState();
    }

    private void initComponents() {
        this.checkBox = new JCheckBox();
        this.panelButton = new JPanel();
        this.buttonYes = new SmButton();
        this.buttonNo = new SmButton();
        this.buttonCancel = new SmButton();
        this.textAreaMessage = new JTextArea();
        this.textAreaMessage.setLineWrap(true);
        this.textAreaMessage.setWrapStyleWord(true);
        this.textAreaMessage.setEditable(false);
        this.textAreaMessage.setOpaque(false);
        this.setSize(size);
        this.setMinimumSize(size);
        this.setLocationRelativeTo((Component) null);
        this.setTitle(this.defaultTitle);
        this.getRootPane().setDefaultButton(this.buttonYes);
    }

    private void initLayout() {
        this.panelButton.setLayout(new GridBagLayout());
        this.panelButton.add(this.buttonYes, (new GridBagConstraintsHelper(0, 0, 1, 1)).setFill(0).setWeight(1.0D, 1.0D).setAnchor(13).setInsets(0, 0, 20, 10));
        this.panelButton.add(this.buttonNo, (new GridBagConstraintsHelper(1, 0, 1, 1)).setFill(0).setWeight(0.0D, 1.0D).setAnchor(13).setInsets(0, 0, 10, 10));
        this.panelButton.add(this.buttonCancel, (new GridBagConstraintsHelper(2, 0, 1, 1)).setFill(0).setWeight(0.0D, 1.0D).setAnchor(13).setInsets(0, 0, 10, 10));
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.add(this.textAreaMessage, (new GridBagConstraintsHelper(0, 0, 1, 1)).setFill(2).setWeight(1.0D, 1.0D).setAnchor(10).setInsets(10));
        panel.add(this.checkBox, (new GridBagConstraintsHelper(0, 1, 1, 1)).setFill(2).setWeight(1.0D, 1.0D).setAnchor(10).setInsets(10));
        panel.add(this.panelButton, (new GridBagConstraintsHelper(0, 2, 1, 1)).setFill(1).setWeight(0.0D, 0.0D).setAnchor(15));
        this.setLayout(new GridBagLayout());
        this.add(panel, (new GridBagConstraintsHelper(0, 0)).setWeight(1.0D, 1.0D).setAnchor(10).setFill(1));
    }

    private void addListeners() {
        this.buttonYes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                Clipboard systemClipboard = defaultToolkit.getSystemClipboard();
                String url = ShareDialog.this.textAreaMessage.getText();
                StringSelection stringSelection = new StringSelection(url);
                systemClipboard.setContents(stringSelection, null);
                ShareDialog.this.result = 0;
                ShareDialog.this.setVisible(false);
            }
        });
        this.buttonNo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShareDialog.this.result = 1;
                ShareDialog.this.setVisible(false);
            }
        });
        this.buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                ShareDialog.this.result = 2;
                ShareDialog.this.setVisible(false);
            }
        });
    }

    private void initResources() {
        this.buttonYes.setText(GAFProperties.getString("String_Copy"));
        this.buttonNo.setText(CoreProperties.getString("String_Cancel"));
        this.buttonCancel.setText(CoreProperties.getString("String_Cancel"));
    }

    public ShareDialog setCheckBoxText(String text) {
        this.checkBox.setText(text);
        return this;
    }

    public ShareDialog setCheckBoxEnable(boolean enable) {
        this.checkBox.setEnabled(enable);
        return this;
    }

    public ShareDialog setCheckBoxVisible(boolean visible) {
        this.checkBox.setVisible(visible);
        return this;
    }

    public ShareDialog setCheckBoxSelected(boolean selected) {
        this.checkBox.setSelected(selected);
        return this;
    }

    private void initComponentState() {
        this.buttonCancel.setVisible(false);
        this.checkBox.setVisible(false);
    }

    public int showMessageDialog(String message) {
        this.buttonNo.setVisible(false);
        return this.showDialog(message);
    }

    public int showConfirmDialog(String message) {
        return this.showDialog(message);
    }

    public int showConfirmDialogWithCancel(String message) {
        this.buttonYes.setText(CoreProperties.getString("String_yes"));
        this.buttonNo.setText(CoreProperties.getString("String_no"));
        this.buttonCancel.setVisible(true);
        return this.showDialog(message);
    }

    public int showErrorDialog(String message) {
        this.buttonNo.setVisible(false);
        return this.showDialog(message);
    }

    public int showWarningDialog(String message) {
        return this.showDialog(message);
    }

    public int showConfirmDialogYesNo(String message) {
        this.buttonYes.setText(CoreProperties.getString("String_yes"));
        this.buttonNo.setText(CoreProperties.getString("String_no"));
        return this.showDialog(message);
    }

    public int showDialog(String message) {
        this.setAlwaysOnTop(true);
        this.result = -1;
        this.textAreaMessage.setText(message);
        this.initTravelPolicy();
        this.showDialog();
        this.buttonYes.setText(CoreProperties.getString("String_OK"));
        this.buttonNo.setText(CoreProperties.getString("String_Cancel"));
        this.buttonNo.setVisible(true);
        this.buttonCancel.setVisible(false);
        return this.result;
    }

    private void initTravelPolicy() {
        if (this.componentList.size() > 0) {
            this.componentList.clear();
        }

        if (this.buttonYes.isVisible()) {
            this.componentList.add(this.buttonYes);
        }

        if (this.buttonNo.isVisible()) {
            this.componentList.add(this.buttonNo);
        }

        if (this.buttonCancel.isVisible()) {
            this.componentList.add(this.buttonCancel);
        }

        this.setFocusTraversalPolicy(this.policy);
    }

    public String getCheckBoxText() {
        return this.checkBox.getText();
    }

    public ShareDialog setButtonNoText(String noText) {
        this.buttonNo.setText(noText);
        this.buttonNo.setVisible(noText != null);
        return this;
    }

    public ShareDialog setButtonCancelText(String cancelText) {
        this.buttonCancel.setText(cancelText);
        this.buttonCancel.setVisible(cancelText != null);
        return this;
    }

    public ShareDialog setButtonOkText(String okText) {
        this.buttonYes.setText(okText);
        this.buttonYes.setVisible(okText != null);
        return this;
    }
}

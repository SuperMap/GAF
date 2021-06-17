/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop.ui;

import com.supermap.desktop.controls.ui.controls.DialogResult;
import com.supermap.desktop.controls.ui.controls.SmDialog;
import com.supermap.desktop.controls.ui.controls.button.SmButton;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.core.implement.CtrlAction;
import com.supermap.desktop.core.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.core.utilties.ColorUtilities;
import com.supermap.desktop.core.utilties.DefaultValues;
import com.supermap.desktop.develop.GAFProperties;
import com.supermap.desktop.develop.client.GafClient;
import com.supermap.desktop.develop.entity.GafGlobalEnvironments;
import com.supermap.desktop.develop.utils.ApplicationContextUtils;
import com.supermap.desktop.develop.utils.CommonUtils;
import com.supermap.desktop.netservices.iserver.action.CtrlActionReleaseWorkspace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * @author SuperMap
 * @date:2021/3/25
 * 对话框
 */
public class DialogLoginGaf extends SmDialog {

	// 面板
	private JPanel panel;

	private JLabel labelUserName;

	private JTextField username;

	private JLabel labelPassword;

	private JPasswordField password;
    private JLabel labelError;

	private SmButton buttonOK;
	private SmButton buttonCancel;


	public DialogLoginGaf() {

		super();
		// 设置对话框大小
		this.setSize(new Dimension(430, 160));
		// 新建各个组件
		initComponents();

		// 组装ui组件
		initLayout();
		// 使用国际化资源设置标题、文本内容
		initResources();
		registerEvents();
		this.componentList.add(this.buttonOK);
		this.componentList.add(this.buttonCancel);
		}

	private void initComponents() {
		//  FlowLayout布局管理器创建新面板
		this.panel = new JPanel();

		// 标签组件，可以包含文本和图片甚至HTML 文本
		this.labelUserName = new JLabel();
		// 数据源下拉框
		this.username = new JTextField(16);
		// 数据集下拉框
		this.labelPassword = new JLabel();
		this.password = new JPasswordField(16);
		this.password.setEchoChar('*');
		// 错误信息
		this.labelError = new JLabel();
		// 确定按钮
		this.buttonOK = ComponentFactory.createButtonOK();
		// 关闭
		this.buttonCancel = ComponentFactory.createButtonCancel();
		getRootPane().setDefaultButton(buttonOK);
	}


	private void initLayout() {
		// 向面板插入主要元素
		initLayoutPanelSourceData();

		// 将面板插入对话框
		this.setLayout(new GridBagLayout());
		this.add(this.panel, new GridBagConstraintsHelper(0, 0, 1, 1).setInsets(GridBagConstraintsHelper.FRAME_CONTROL_GAP, GridBagConstraintsHelper.FRAME_CONTROL_GAP, 0, GridBagConstraintsHelper.FRAME_CONTROL_GAP).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
        this.add(this.labelError, new GridBagConstraintsHelper(0, 1, 1, 1).setInsets(GridBagConstraintsHelper.FRAME_CONTROL_GAP, GridBagConstraintsHelper.FRAME_CONTROL_GAP, 0, GridBagConstraintsHelper.FRAME_CONTROL_GAP).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));

//		this.add(new JPanel(), new GridBagConstraintsHelper(0, 2, 1, 1).setInsets(0, GridBagConstraintsHelper.FRAME_CONTROL_GAP, 0, GridBagConstraintsHelper.FRAME_CONTROL_GAP).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		// 对话框插入按钮
		this.add(ComponentFactory.createButtonPanel(this.buttonOK, this.buttonCancel), new GridBagConstraintsHelper(0, 3, 1, 1).setInsets(0, GridBagConstraintsHelper.FRAME_CONTROL_GAP, GridBagConstraintsHelper.FRAME_CONTROL_GAP, GridBagConstraintsHelper.FRAME_CONTROL_GAP).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));
	}

	private void initLayoutPanelSourceData() {
		// 设置面板布局为网格
		this.panel.setLayout(new GridBagLayout());
		this.labelUserName.setPreferredSize(DefaultValues.getLabelDefaultSize());
		this.labelPassword.setPreferredSize(DefaultValues.getLabelDefaultSize());



		// 面板插入数据源标签、与数据源下拉框
		this.panel.add(this.labelUserName, new GridBagConstraintsHelper(0, 0, 1, 1).setInsets(GridBagConstraintsHelper.CONTROLS_GAP, GridBagConstraintsHelper.CONTROLS_GAP, 0, 0));
		this.panel.add(this.username, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(GridBagConstraintsHelper.CONTROLS_GAP, GridBagConstraintsHelper.CONTROLS_GAP, 0, GridBagConstraintsHelper.CONTROLS_GAP).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));

		// 面板插入数据集标签、与数据集下拉框
		this.panel.add(this.labelPassword, new GridBagConstraintsHelper(0, 1, 1, 1).setInsets(GridBagConstraintsHelper.CONTROLS_GAP, GridBagConstraintsHelper.CONTROLS_GAP, 0, 0));
		this.panel.add(this.password, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.HORIZONTAL).setInsets(GridBagConstraintsHelper.CONTROLS_GAP, GridBagConstraintsHelper.CONTROLS_GAP, 0, GridBagConstraintsHelper.CONTROLS_GAP).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL));


    }


	private void initResources() {
		// 设置对话框标题
		this.setTitle(GAFProperties.getString("String_GafLogin"));
		// 设置数据源、数据集标签内容
		this.labelUserName.setText(GAFProperties.getString("String_Label_Username"));
		this.labelPassword.setText(GAFProperties.getString("String_Label_Password"));

	}

	private void registerEvents() {
		// 按钮添加事件
		this.buttonOK.addActionListener(this.actionListenerOK);
		this.username.addActionListener(this.actionListenerOK);
		this.password.addActionListener(this.actionListenerOK);
		this.buttonCancel.addActionListener(this.actionListenerCancel);
	}


	@Override
	public void windowOpened(WindowEvent e) {
		this.username.requestFocus();
		super.windowOpened(e);
	}
	private final ActionListener actionListenerOK = e -> {
		String usernameText = username.getText();//获取输入内容
		String passwordText = password.getText();//获取输入内容
		String gafServerText = GafGlobalEnvironments.getServer();
		if("".equals(usernameText)||"".equals(passwordText)){
            this.labelError.setForeground(ColorUtilities.getErrorColor());
            this.labelError.setText("用户名密码不能为空");
			ApplicationContextUtils.getOutput().output("用户名密码不能为空");
		}else{
		    try{
                run();

                setDialogResult(DialogResult.OK);
                dispose();
               ApplicationContextUtils.getOutput().output("登录成功");
            }catch (Exception ex){
				CommonUtils.checkGafStatus(false);
                this.labelError.setForeground(ColorUtilities.getErrorColor());
                this.labelError.setText("登录失败！");
		       ApplicationContextUtils.getOutput().output(ex.getMessage());
            }
		}
	};

	private final ActionListener actionListenerCancel = e -> dispose();


	private void run() throws Exception {
        this.labelError.setForeground(null);
        this.labelError.setText("");
		String usernameText = username.getText();//获取输入内容
		String passwordText = new String(password.getPassword());//获取输入内容
		String host = GafGlobalEnvironments.getServer();

		new GafClient(usernameText,passwordText,host);
		//todo:
		CommonUtils.checkGafStatus(true);
		// todo:调用接口获取资源目录
		CommonUtils.refreshDatasourceTree();
		CommonUtils.refreshWorkspaceTree();
	}
}


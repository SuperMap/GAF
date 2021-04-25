//package com.supermap.desktop.develop.ui;
//
//import com.supermap.desktop.controls.ControlsProperties;
//import com.supermap.desktop.controls.ui.controls.SmFileChoose;
//import com.supermap.desktop.controls.ui.controls.SmFileChooserControl;
//import com.supermap.desktop.controls.ui.controls.panels.JPanelWorkspaceSaveAsFile;
//import com.supermap.desktop.controls.ui.controls.textFields.syntaxTextArea.SmPasswordField;
//import com.supermap.desktop.core.FileChooseMode;
//import com.supermap.desktop.core.properties.CoreProperties;
//import com.supermap.desktop.core.utilties.FileUtilities;
//import com.supermap.desktop.core.utilties.StringUtilities;
//
//import javax.swing.*;
//import java.awt.event.*;
//
///**
// * @author heykb
// */
//public class JPanelWorkspaceOpenFile extends JPanel {
//    private static final long serialVersionUID = 1L;
//    private String fileName;
//    private Boolean isSymbolSample = false;
//    private final ActionListener symbolSampleListener = new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//            isSymbolSample = jCheckBoxSymbolSample.isSelected();
//        }
//    };
//    private final KeyListener enterListener = new KeyAdapter() {
//        public void keyPressed(KeyEvent e) {
//            if (e.getKeyCode() == 10) {
//                firePropertyChange("ENTER", (Object)null, (Object)null);
//            }
//
//        }
//    };
//    private JCheckBox jCheckBoxSymbolSample;
//    private JComboBox<String> jComboBoxVersion;
//    private JLabel jLabelFileName;
//    private JLabel jLabelPassword;
//    private JLabel jLabelPasswordConfrim;
//    private JLabel jLabelVersion;
//    private SmFileChooserControl smFileChooserControl;
//    private SmPasswordField jPasswordFieldPassword;
//    private SmPasswordField jPasswordFieldPasswordConfrim;
//
//
//    public JPanelWorkspaceOpenFile() {
//        this.initComponents();
//        this.initListeners();
//        this.initResources();
//        this.initLayout();
//    }
//
//    private void initComponents() {
//        this.smFileChooserControl = this.getFileChooserControlSrcFile();
//        this.smFileChooserControl.setEditable(true);
//        this.jLabelFileName = new JLabel();
//        this.jPasswordFieldPassword = new SmPasswordField();
//        this.jLabelPassword = new JLabel();
//        this.jPasswordFieldPasswordConfrim = new SmPasswordField();
//        this.jLabelPasswordConfrim = new JLabel();
//        this.jComboBoxVersion = new JComboBox();
//        this.jLabelVersion = new JLabel();
//        this.jCheckBoxSymbolSample = new JCheckBox();
//    }
//
//    private SmFileChooserControl getFileChooserControlSrcFile() {
//        if (null == this.jPasswordFieldPasswordConfrim) {
//            SmFileChoose smFileChoose = (new SmFileChoose("DatasourceAndWorkspaceFile", FileChooseMode.OPEN_ONE)).setTitle(ControlsProperties.getString("String_Title_WorkSpaceSaveAs")).addFileFilter(ControlsProperties.getString("String_WorkspaceSMWUFilterName"), new String[]{ControlsProperties.getString("String_WorkspaceSMWUFilters")}).addFileFilter(ControlsProperties.getString("String_WorkspaceSXWUFilterName"), new String[]{ControlsProperties.getString("String_WorkspaceSXWUFilters")});
//            this.smFileChooserControl = new SmFileChooserControl();
//            this.smFileChooserControl.setFileChooser(smFileChoose);
//        }
//
//        return this.smFileChooserControl;
//    }
//
//    private void initResources() {
//        this.jLabelFileName.setText(CoreProperties.getString("String_WorkspaceFileName"));
//        this.jLabelPassword.setText(CoreProperties.getString("String_WorkspacePassword"));
//        this.jLabelPasswordConfrim.setText(CoreProperties.getString("String_LabelWorkspacePassword_Confirm"));
//        this.jLabelVersion.setText(ControlsProperties.getString("String_Label_WorkspaceVersion"));
//        this.jCheckBoxSymbolSample.setText(ControlsProperties.getString("String_SymbolSample"));
//        this.jComboBoxVersion.setModel(new DefaultComboBoxModel(new String[]{"SuperMap UGC 7.0", "SuperMap UGC 6.0"}));
//        this.jComboBoxVersion.setSelectedIndex(0);
//    }
//
//    public JPasswordField getJPasswordFieldPassword() {
//        return this.jPasswordFieldPassword;
//    }
//
//    private void checkPasswordEnable() {
//        if (!StringUtilities.isNullOrEmpty(this.smFileChooserControl.getPath())) {
//            if (this.smFileChooserControl.getPath().toLowerCase().endsWith(".smwu")) {
//                this.jPasswordFieldPassword.setEditable(true);
//                this.jPasswordFieldPasswordConfrim.setEnabled(true);
//            } else if (this.smFileChooserControl.getPath().toLowerCase().endsWith(".sxwu")) {
//                this.jPasswordFieldPassword.setEditable(false);
//                this.jPasswordFieldPasswordConfrim.setEnabled(false);
//            }
//        }
//
//    }
//
//    private void OpenFileAction() {
//        if (this.smFileChooserControl.getPath() != null) {
//            this.fileName = FileUtilities.getFileName(this.smFileChooserControl.getPath());
//            this.checkPasswordEnable();
//        }
//
//    }
//
//    public int getSelectedVersion() {
//        return this.jComboBoxVersion.getSelectedIndex();
//    }
//
//    private void initLayout() {
//        GroupLayout layout = new GroupLayout(this);
//        this.setLayout(layout);
//        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabelFileName).addComponent(this.jLabelPassword).addComponent(this.jLabelPasswordConfrim).addComponent(this.jLabelVersion).addComponent(this.jCheckBoxSymbolSample)).addGap(20, 20, 20).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.smFileChooserControl, -1, -1, 32767)).addComponent(this.jPasswordFieldPassword).addComponent(this.jPasswordFieldPasswordConfrim).addComponent(this.jComboBoxVersion)))).addContainerGap()));
//        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.smFileChooserControl, -2, -1, -2).addComponent(this.jLabelFileName)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jPasswordFieldPassword, -2, -1, -2).addComponent(this.jLabelPassword)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jPasswordFieldPasswordConfrim, -2, -1, -2).addComponent(this.jLabelPasswordConfrim)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jComboBoxVersion, -2, -1, -2).addComponent(this.jLabelVersion)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(this.jCheckBoxSymbolSample)).addContainerGap(-1, 32767)));
//    }
//
//    private void initListeners() {
//        this.smFileChooserControl.addKeyListener(new KeyAdapter() {
//            public void keyReleased(KeyEvent e) {
//                if (e.getKeyCode() == 10) {
//                    checkPasswordEnable();
//                }
//
//            }
//        });
//        this.smFileChooserControl.addFocusListener(new FocusAdapter() {
//            public void focusLost(FocusEvent e) {
//                checkPasswordEnable();
//            }
//        });
//        this.smFileChooserControl.addPathChangedListener(this::OpenFileAction);
//        this.removeEvents();
//        this.jPasswordFieldPassword.addKeyListener(this.enterListener);
//        this.jPasswordFieldPasswordConfrim.addKeyListener(this.enterListener);
//        this.jComboBoxVersion.addKeyListener(this.enterListener);
//        this.smFileChooserControl.addKeyListener(this.enterListener);
//        this.smFileChooserControl.addKeyListener(this.enterListener);
//        this.jCheckBoxSymbolSample.addActionListener(this.symbolSampleListener);
//    }
//
//    private void removeEvents() {
//        this.jPasswordFieldPassword.removeKeyListener(this.enterListener);
//        this.jPasswordFieldPasswordConfrim.removeKeyListener(this.enterListener);
//        this.jComboBoxVersion.removeKeyListener(this.enterListener);
//        this.smFileChooserControl.removeKeyListener(this.enterListener);
//        this.smFileChooserControl.removeKeyListener(this.enterListener);
//        this.jCheckBoxSymbolSample.removeActionListener(this.symbolSampleListener);
//    }
//
//    public JComboBox<String> getjComboBoxVersion() {
//        return this.jComboBoxVersion;
//    }
//
//    public JPasswordField getjPasswordFieldPassword() {
//        return this.jPasswordFieldPassword;
//    }
//
//    public JPasswordField getjPasswordFieldPasswordConfrim() {
//        return this.jPasswordFieldPasswordConfrim;
//    }
//
//    public Boolean getSymbolSample() {
//        return this.isSymbolSample;
//    }
//
//    public String getFileName() {
//        return this.fileName;
//    }
//
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
//
//    public SmFileChooserControl getSmFileChooserControl() {
//        return this.smFileChooserControl;
//    }
//}
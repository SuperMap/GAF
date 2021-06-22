package com.supermap.desktop.develop.ui;

import com.supermap.desktop.controls.ui.controls.textFields.SmTextFieldLegit;
import com.supermap.desktop.core.Interface.ISmTextFieldLegit;
import com.supermap.desktop.core.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.core.utilties.StringUtilities;
import com.supermap.desktop.develop.GAFProperties;
import com.supermap.desktop.develop.entity.GafGlobalEnvironments;
import com.supermap.desktop.frame.ctrlAction.settings.BaseSettingPanel;

import javax.swing.*;
import java.awt.*;

public class GafSettings extends BaseSettingPanel {
    private JPanel panelGafSetting;
    private JLabel labelGafServer;
    private SmTextFieldLegit smTextFieldGafServer;
    private JLabel labelGafAlias;
    private SmTextFieldLegit smTextFieldGafAlias;
    
    @Override
    protected void initComponents() {
        this.labelGafServer = new JLabel();
        this.smTextFieldGafServer = new SmTextFieldLegit();
        this.labelGafAlias = new JLabel();
        this.smTextFieldGafAlias = new SmTextFieldLegit();
        this.panelGafSetting = new JPanel();
        this.panelGafSetting.setBorder(BorderFactory.createTitledBorder(GAFProperties.getString("String_BaseSetting")));

    }
    @Override
    protected void initListeners() {
        this.smTextFieldGafServer.setSmTextFieldLegit(new ISmTextFieldLegit() {
            @Override
            public boolean isTextFieldValueLegit(String textFieldValue) {
                if (StringUtilities.isNullOrEmpty(textFieldValue)) {
                    return false;
                } else {
                    if (!changedValues.contains(smTextFieldGafServer)) {
                        changedValues.add(smTextFieldGafServer);
                    }
                    return true;
                }
            }
            @Override
            public String getLegitValue(String currentValue, String backUpValue) {
                return backUpValue;
            }
        });
        this.smTextFieldGafAlias.setSmTextFieldLegit(new ISmTextFieldLegit() {
            @Override
            public boolean isTextFieldValueLegit(String textFieldValue) {
                if (StringUtilities.isNullOrEmpty(textFieldValue)) {
                    return false;
                } else {
                    if (!changedValues.contains(smTextFieldGafAlias)) {
                        changedValues.add(smTextFieldGafAlias);
                    }
                    return true;
                }
            }
            @Override
            public String getLegitValue(String currentValue, String backUpValue) {
                return backUpValue;
            }
        });

        this.panelGafSetting.addPropertyChangeListener((evt) -> {
            if (evt.getPropertyName().equals("ancestor") && evt.getOldValue() != null && evt.getNewValue() == null) {
                this.apply();
                GafGlobalEnvironments.save();
            }

        });
    }

    @Override
    protected void initLayout() {
        setLayout(new GridBagLayout());
        this.initPanelGafSetting();
        this.add(this.panelGafSetting, (new GridBagConstraintsHelper(0, 0, 1, 1)).setWeight(1.0D, 1.0D).setAnchor(GridBagConstraints.NORTHWEST)
                .setFill(GridBagConstraints.HORIZONTAL).setInsets(20,20).setIpad(50,0));

    }

    private void initPanelGafSetting() {
        this.panelGafSetting.setLayout(new GridBagLayout());
        this.panelGafSetting.add(this.labelGafServer, (new GridBagConstraintsHelper(0, 0, 1, 1)).setWeight(0.0D, 0.0D).setInsets(5, 5, 0, 0).setAnchor(17).setFill(1));
        this.panelGafSetting.add(this.smTextFieldGafServer, (new GridBagConstraintsHelper(1, 0, 2, 1)).setWeight(0.6D, 0.0D).setInsets(5, 20, 0, 0).setAnchor(17).setFill(2));
        this.panelGafSetting.add(this.labelGafAlias, (new GridBagConstraintsHelper(0, 1, 1, 1)).setWeight(0.0D, 0.0D).setInsets(5, 5, 0, 0).setAnchor(17).setFill(1));
        this.panelGafSetting.add(this.smTextFieldGafAlias, (new GridBagConstraintsHelper(1, 1, 2, 1)).setWeight(0.6D, 0.0D).setInsets(5, 20, 0, 0).setAnchor(17).setFill(2));

    }

    @Override
    protected void initResources() {
        this.labelGafServer.setText(GAFProperties.getString("String_GafServerSetting"));
        this.smTextFieldGafServer.setText(GafGlobalEnvironments.getServer());
        this.labelGafAlias.setText(GAFProperties.getString("String_GafAliasSetting"));
        this.smTextFieldGafAlias.setText(GafGlobalEnvironments.getAlias());
    }

    @Override
    protected void apply() {
        for(Component changedValue:changedValues){
            if (changedValue == this.smTextFieldGafServer){
                GafGlobalEnvironments.setServer(this.smTextFieldGafServer.getText());
            }else if(changedValue == this.smTextFieldGafAlias ){
                GafGlobalEnvironments.setAlias(this.smTextFieldGafAlias.getText());
            }
        }
        this.changedValues.clear();
    }

    @Override
    protected void dispose() {

    }
}

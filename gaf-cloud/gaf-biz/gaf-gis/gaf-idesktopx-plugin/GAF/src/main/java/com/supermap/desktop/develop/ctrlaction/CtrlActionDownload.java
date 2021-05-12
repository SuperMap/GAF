/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop.ctrlaction;

import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.desktop.controls.ui.controls.SmFileChoose;
import com.supermap.desktop.core.FileChooseMode;
import com.supermap.desktop.core.Interface.IBaseItem;
import com.supermap.desktop.core.implement.CtrlAction;
import com.supermap.desktop.core.utilties.JOptionPaneUtilities;
import com.supermap.desktop.develop.exception.FileDownloadException;
import com.supermap.desktop.develop.exception.FileUploadException;
import com.supermap.desktop.develop.utils.ApplicationContextUtils;
import com.supermap.desktop.develop.utils.CommonUtils;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @date:2021/3/25
 * @author SuperMap
 */
public class CtrlActionDownload extends CtrlAction {
    private Object conn;
    private SmFileChoose smFileChoose;


    public Object getConn() {
        return conn;
    }

    public void setConn(Object conn) {
        this.conn = conn;
        if(this.smFileChoose == null) initFileChoose();
        if(this.conn instanceof DatasourceConnectionInfo){
            this.smFileChoose.setTitle("下载数据源到");
        }else if(this.conn instanceof WorkspaceConnectionInfo){
            this.smFileChoose.setTitle("下载工作空间到");
        }
    }

    @Override
    public boolean enable() {
       return CommonUtils.isFileTypeSource(conn);
    }

    public CtrlActionDownload(IBaseItem caller) {
		super(caller);
	}
    private void initFileChoose(){
        this.smFileChoose = new SmFileChoose("", FileChooseMode.SAVE_ONE,".");
        this.smFileChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }
    @Override
	public void run() {
        if(enable()){

            Optional<String> fileName = CommonUtils.getFileName(conn);
            if(!fileName.isPresent()){
                JOptionPaneUtilities.showErrorMessageDialog("无法下载该文件");
                return;
            }
            if(CommonUtils.tipIfNotExist("/datas/"+fileName.get())){
                return;
            }
            int re = CommonUtils.dirChoose(smFileChoose,fileName.get());
            if(re == 0){
                try{
                    Path downloadPath = Paths.get(smFileChoose.getFilePath()+"/"+fileName.get());
                    // todo: 执行下载
                    CommonUtils.downloadAsync("/datas/"+downloadPath.getFileName(),downloadPath,evt -> {
                        if (evt.getPropertyName().equals("progress")) {
                            Integer progress = (Integer) evt.getNewValue();
                            if(progress%10==0){
                                ApplicationContextUtils.getOutput().output("已下载"+downloadPath.getFileName().toString()+":"+progress+"%");
                            }
                            if(progress==100){
                                ApplicationContextUtils.getOutput().output(downloadPath.getFileName().toString()+"下载成功");
                            }
                        }
                    });
                    Path downloadPath2 = downloadPath;
                    if(downloadPath.toString().endsWith(".udb")) {
                        String uddFileName = fileName.get().substring(0,fileName.get().length()-1)+"d";
                        Path uddDownloadPath = downloadPath2.getParent().resolve(uddFileName);
                        CommonUtils.downloadAsync("/datas/"+uddDownloadPath.getFileName(),uddDownloadPath,evt -> {
                            if (evt.getPropertyName().equals("progress")) {
                                Integer progress = (Integer) evt.getNewValue();
                                if(progress%10==0){
                                    ApplicationContextUtils.getOutput().output("已下载"+downloadPath.getFileName().toString()+":"+progress+"%");
                                }
                                if(progress==100){
                                    ApplicationContextUtils.getOutput().output(uddDownloadPath.getFileName().toString()+"下载成功");
                                }
                            }
                        });
                    }
                }catch (FileDownloadException e) {
                    ApplicationContextUtils.getOutput().output(e.getMessage()+"下载失败");
                } catch (Exception e) {
                    ApplicationContextUtils.getOutput().output(e.getMessage());
                }
            }

        }

	}
}

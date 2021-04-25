/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop.utils;

import com.alibaba.fastjson.JSONObject;
import com.supermap.data.*;
import com.supermap.desktop.controls.ui.UICommonToolkit;
import com.supermap.desktop.controls.ui.controls.DockbarManager;
import com.supermap.desktop.controls.ui.controls.SmFileChoose;
import com.supermap.desktop.core.Interface.IBaseItem;
import com.supermap.desktop.core.Interface.IContextMenuManager;
import com.supermap.desktop.core.Interface.IDockbar;
import com.supermap.desktop.core.utilties.*;
import com.supermap.desktop.develop.entity.PresignUploadRequest;
import com.supermap.desktop.develop.client.GafClient;
import com.supermap.desktop.develop.ctrlaction.CtrlActionLoginGaf;
import com.supermap.desktop.develop.ctrlaction.CtrlActionLogoutGaf;
import com.supermap.desktop.develop.ctrlaction.CtrlActionRegistryWorkspace;
import com.supermap.desktop.develop.ctrlaction.CtrlActionSynchronizeWorkspace;
import com.supermap.desktop.develop.exception.FileDownloadException;
import com.supermap.desktop.develop.exception.FileUploadException;
import com.supermap.desktop.develop.ui.GafDatasourceManager;
import com.supermap.desktop.develop.ui.GafTree;
import com.supermap.desktop.develop.ui.GafWorkspaceManager;
import com.supermap.desktop.develop.work.FileDownloadWork;
import com.supermap.desktop.develop.work.FileUploadWork;
import com.supermap.desktop.develop.work.ProgressListener;
import org.apache.commons.lang3.StringUtils;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.util.*;

/**
 * @date:2021/3/25
 * @author heykb
 */
public class CommonUtils {
    public static boolean online = false;
    public static void checkGafPopMenuStatus(){
        Workspace workspace = ApplicationContextUtils.getWorkspace();
        IContextMenuManager manager =ApplicationContextUtils.getContextMenuManager();
        for(IBaseItem iBaseItem: manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuWorkspace").items()){
            if(iBaseItem!=null && "gafWorkspaceUpload".equals(iBaseItem.getID())){
                boolean enable = online; /*&& (workspace.getType().equals(WorkspaceType.SMWU)||workspace.getType().equals(WorkspaceType.SXWU));*/
                iBaseItem.setEnabled(enable);
                iBaseItem.setVisible(true);
//                CtrlActionReleaseWorkspace
            }
        }
//        for(IBaseItem iBaseItem: manager.get("SuperMap.Desktop.UI.WorkspaceControlManager.ContextMenuDatasource").items()){
//            if(iBaseItem!=null && "gafDatasourceUpload".equals(iBaseItem.getID())){
//                boolean enable = online;
//                iBaseItem.setEnabled(enable);
//                iBaseItem.setVisible(true);
//            }
//
//        }

    }
    public static void checkGafStatus(boolean online){
        CommonUtils.online = online;
        if(!online){
            // 注销
            checkGafPopMenuStatus();
            CtrlActionLogoutGaf.enable = false;
            CtrlActionLoginGaf.enable = true;
            CtrlActionRegistryWorkspace.enable= false;
            CtrlActionSynchronizeWorkspace.enable = false;
            GafClient.logout();
        }else{
            //登录
            checkGafPopMenuStatus();
            CtrlActionLogoutGaf.enable = true;
            CtrlActionLoginGaf.enable = false;
            CtrlActionRegistryWorkspace.enable= true;
            CtrlActionSynchronizeWorkspace.enable = true;
        }

    }
    public static boolean isFileTypeSource(Object source){
        if(source == null) return false;
        if(source instanceof WorkspaceConnectionInfo){
            WorkspaceConnectionInfo connectionInfo = (WorkspaceConnectionInfo)source;
            return WorkspaceType.SMWU.equals(connectionInfo.getType())
                    || WorkspaceType.SXWU.equals(connectionInfo.getType())
                    || WorkspaceType.SMW.equals(connectionInfo.getType())
                    || WorkspaceType.SXW.equals(connectionInfo.getType());
        }
        if(source instanceof DatasourceConnectionInfo){
            DatasourceConnectionInfo connectionInfo = (DatasourceConnectionInfo)source;
            return EngineType.UDB.equals(connectionInfo.getEngineType()) || EngineType.UDBX.equals(connectionInfo.getEngineType());
        }
        return false;
    }
    public static WorkspaceConnectionInfo copyWorkspaceConn(WorkspaceConnectionInfo source){
        WorkspaceConnectionInfo wConn = new WorkspaceConnectionInfo();
        wConn.setType(source.getType());
        wConn.setUser(source.getUser());
        wConn.setServer(source.getServer());
        wConn.setPassword(source.getPassword());
        wConn.setDatabase(source.getDatabase());
        wConn.setDriver(source.getDriver());
        wConn.setName(source.getName());
        return wConn;
    }
    public static String getBase64Md5(Path path) throws Exception {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        try(InputStream in  = Files.newInputStream(path)){
            byte[] buffer = new byte[8192];
            int length;
            while((length=in.read(buffer))!=-1){
                //md5计算
                md5.update(buffer,0,length);
            }

        }
        String base64Md5 = Base64.getEncoder().encodeToString(md5.digest());
        return base64Md5;
    }

    public static void uploadAsync(String targetPath, Path source, PropertyChangeListener listener)throws FileUploadException{
        uploadAsync(targetPath,source,true,listener);
    }
    public static void uploadAsync(String targetPath, Path source, boolean quickUploadEnable, PropertyChangeListener listener) throws FileUploadException {
        try{
            String base64Md5=null;
            if(quickUploadEnable){
                try{
                    base64Md5 =  getBase64Md5(source);
                    JSONObject re = GafClient.instance().objectMetadata(targetPath);
                    JSONObject data = re.getJSONObject("data");
                    if(404!=re.getInteger("status") && data!=null){
                        String md5 = data.getJSONObject("userMetadata").getString("base64md5");
                        if(base64Md5.equals(md5)){
                            ApplicationContextUtils.getOutput().output(source.getFileName().toString()+"快速上传");
                            return;
                        }
                    }
                }catch (Exception e){
                }
            }
            PresignUploadRequest uploadRequest = GafClient.instance().uploadPresignUrl(targetPath,base64Md5);
            FileUploadWork workspaceUpload = new FileUploadWork(uploadRequest,source);
            workspaceUpload.addPropertyChangeListener(listener);
            workspaceUpload.execute();
        }catch (Exception e){
            throw new FileUploadException(source.getFileName().toString());
        }
    }


    public static void uploadByPreSignedUrl(PresignUploadRequest uploadRequest, Path path) throws FileUploadException  {
        uploadByPreSignedUrl(uploadRequest,path,null);
    }

    public static void uploadByPreSignedUrl(PresignUploadRequest uploadRequest, Path path, ProgressListener listener) throws FileUploadException {
        HttpURLConnection connection = null;
        OutputStream out = null;
        BufferedReader reader = null;
        try{
            String preSignedUrl = uploadRequest.getPresignUrl();
            String base64Md5 = uploadRequest.getContentMd5();
            URL url = new URL(preSignedUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type","application/octet-stream");
            if(!StringUtils.isEmpty(base64Md5)){
                connection.setRequestProperty("Content-MD5",base64Md5);
                connection.setRequestProperty("x-amz-meta-base64md5",base64Md5);
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("PUT");
            connection.connect();
            out = new DataOutputStream(connection.getOutputStream());
            long fileSize = path.toFile().length();

            try(InputStream in = Files.newInputStream(path)){
                long nread = 0L;
                byte[] buf = new byte[8192];
                int n;
                while ((n = in.read(buf)) > 0) {
                    out.write(buf, 0, n);
                    nread += n;
                    if(listener!=null){
                        int progress = (int) Math.round(((double) nread / (double) fileSize) * 100d);
                        progress = progress==100?99:progress;
                        listener.progress(progress,connection);
                    }
                }
            }
            int code = connection.getResponseCode();
            if(code!=200){
                throw new RuntimeException(code+" 检查MD5");
            }
            if(listener!=null){
                listener.progress(100,connection);
            }

        }catch (Exception e){
            throw new FileUploadException(path.getFileName().toString());
        }finally {
            try {
                reader.close();
                out.flush();
                out.close();
                connection.disconnect();
            } catch (IOException e) {
            }
        }
    }
    public static void downloadAsync(String serverPath, Path localPath,PropertyChangeListener listener) throws FileDownloadException,FileNotFoundException {
        String preSignedUrl = GafClient.instance().downloadPresignUrl(serverPath);
        FileDownloadWork downloadWork = new FileDownloadWork(preSignedUrl,localPath);
        downloadWork.addPropertyChangeListener(listener);
        downloadWork.execute();
    }
    public static void downloadByPreSignedUrl(String preSignedUrl, Path path) throws FileDownloadException,FileNotFoundException{
        downloadByPreSignedUrl(preSignedUrl,path,null);
    }
    public static void downloadByPreSignedUrl(String preSignedUrl, Path path, ProgressListener listener) throws FileDownloadException,FileNotFoundException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(preSignedUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.connect();
//        connection.getContentLength();
            DataInputStream in = new DataInputStream(connection.getInputStream());
            long fileSize = connection.getContentLength();
            try(OutputStream out = Files.newOutputStream(path)){
                long nread = 0L;
                byte[] buf = new byte[8192];
                int n;
                while ((n = in.read(buf)) > 0) {
                    out.write(buf, 0, n);
                    nread += n;
                    if (listener != null) {
                        int progress = (int) Math.round(((double) nread / (double) fileSize) * 100d);
                        progress = progress == 100 ? 99 : progress;
                        listener.progress(progress, connection);
                    }
                }
            }

            int code = connection.getResponseCode();
            if (code != 200) {
                throw new RuntimeException("下载失败");
            }
            if (listener != null) {
                listener.progress(100, connection);
            }
        }catch (FileNotFoundException e){
            JOptionPaneUtilities.showErrorMessageDialog("文件不存在");
            throw e;
        }catch (Exception e){
            ApplicationContextUtils.getOutput().output(path.getFileName().toString()+"下载失败");
            throw new FileDownloadException(path.getFileName().toString());
        }finally {
            connection.disconnect();

        }
    }

    public static void deletePath(Path path) throws IOException {
        if(!Files.isDirectory(path)){
            Files.deleteIfExists(path);
        }else{
            Files.walkFileTree(path,
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file,
                                                     BasicFileAttributes attrs) throws IOException {
                        Files.deleteIfExists(file);
                        return FileVisitResult.CONTINUE;
                    }
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir,
                                                              IOException exc) throws IOException {
                        Files.deleteIfExists(dir);
                        return FileVisitResult.CONTINUE;
                    }

                }
            );
        }
    }

    public static void refreshWorkspaceTree() throws Exception {
        DockbarManager dockbarManager = ApplicationContextUtils.getDockbarManager();
        IDockbar workspace = dockbarManager.get(GafWorkspaceManager.class);
        workspace.setVisible(true);
        ((GafWorkspaceManager)workspace.getInnerComponent()).updateTree(new GafTree(GafClient.instance().workspaceList(), GafTree.GafTreeType.WORKSPACE_TREE));
    }
    public static void refreshDatasourceTree() throws Exception {
        DockbarManager dockbarManager = ApplicationContextUtils.getDockbarManager();
        IDockbar datasource = dockbarManager.get(GafDatasourceManager.class);
        datasource.setVisible(true);
        ((GafDatasourceManager)datasource.getInnerComponent()).updateTree(new GafTree(GafClient.instance().datasourceTree(), GafTree.GafTreeType.DATASOURCE_TREE));
    }

    public static DefaultMutableTreeNode searchTree(TreeModel treeModel,TreeSearchVisitor visitor){
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treeModel.getRoot();
        List<DefaultMutableTreeNode> needFind = new ArrayList<>();
        needFind.add(treeNode);
        while(true){
            if(needFind.isEmpty()){
                return null;
            }
            List<DefaultMutableTreeNode> tmp = new ArrayList<>();
            for(DefaultMutableTreeNode parent:needFind){
                if(visitor.search(parent)){
                    return parent;
                }
                for(int i=0;i<parent.getChildCount();++i){
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getChildAt(i);
                    tmp.add(node);
                }
            }
            needFind = tmp;
        }
    }

    public static ResizableIcon getICon(WorkspaceType workspaceType){
        String iconPath = "";
        iconPath = "/coreresources/WorkspaceManager/Image_RecentUseWorkspace.png";
        return CoreResources.getIcon(iconPath);
    }
    public static ResizableIcon getICon(EngineType engineType){
        return DatasourceImageUtilities.getEngineIcon(engineType);
    }

    public static int dirChoose(SmFileChoose smFileChoose,String conflictFileName){
        if(smFileChoose.showOpenDialog(null)!=0){
            return -1;
        }
        Path downloadPath = null;
        if(conflictFileName!=null){
            downloadPath = Paths.get(smFileChoose.getFilePath()+"/"+conflictFileName);
            if(Files.exists(downloadPath)){
                if(UICommonToolkit.showConfirmDialogYesNo("目录下已存在同名文件，是否覆盖？")!=0){
                    return 1;
                }
            }
        }
        return 0;
    }
    public static Optional<String> getFileName(Object source){
        if(source == null){
            return Optional.empty();
        }
        try{
            if(isFileTypeSource(source)){
                if(source instanceof WorkspaceConnectionInfo){
                    WorkspaceConnectionInfo connectionInfo = (WorkspaceConnectionInfo)source;
                    return Optional.of(Paths.get(connectionInfo.getServer()).getFileName().toString());
                }else if(source instanceof DatasourceConnectionInfo){
                    DatasourceConnectionInfo connectionInfo = (DatasourceConnectionInfo)source;
                    return Optional.of(Paths.get(connectionInfo.getServer()).getFileName().toString());
                }
            }
        }catch (Exception e){

        }
        return Optional.empty();
    }

    public static interface TreeSearchVisitor{
        boolean search(DefaultMutableTreeNode node);
    }

    public static void uploadWorkspace(Workspace oldWorkspace){
        try{
            WorkspaceConnectionInfo connectionInfo = oldWorkspace.getConnectionInfo();
            if(CommonUtils.isFileTypeSource(connectionInfo)){
                Path wPath = CommonUtils.handlerWorkspaceForUpload(oldWorkspace);
                // todo: 上传工作空间
                final Path tmp = wPath;
                CommonUtils.uploadAsync("/datas/"+wPath.getFileName().toString(),wPath,false,evt -> {
                    if (evt.getPropertyName().equals("progress")) {
                        Integer progress = (Integer) evt.getNewValue();
                        if(progress==100){
                            try {
                                ApplicationContextUtils.getOutput().output(tmp.getFileName().toString()+"上传完成");
                                CommonUtils.deletePath(tmp.getParent());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        } catch (FileUploadException e) {
            ApplicationContextUtils.getOutput().output(e.getMessage()+"上传失败");
        } catch (Exception e) {
            ApplicationContextUtils.getOutput().output(e.getMessage());
        }

    }

    public static Path handlerWorkspaceForUpload(Workspace oldWorkspace) throws IOException {
        // 复制工作空间修改数据源连接
        Workspace workspace = new Workspace();
        Datasources oldDatasources = oldWorkspace.getDatasources();
        WorkspaceConnectionInfo wOldConn = oldWorkspace.getConnectionInfo();
        Path wOldPath = Paths.get(wOldConn.getServer());
        Path tmpPath = Files.createTempDirectory("gaf_");
        Path wPath= tmpPath.resolve(wOldPath.getFileName().toString());
        Files.createDirectories(wPath.getParent());
        Files.copy(wOldPath,wPath,StandardCopyOption.REPLACE_EXISTING);
        WorkspaceConnectionInfo wConn = CommonUtils.copyWorkspaceConn(wOldConn);
        wConn.setServer(wPath.toString());
        workspace.open(wConn);
        Datasources datasources = workspace.getDatasources();
        Map<Integer,Datasource> fileSources = new HashMap<>();
        for(int i=0;i<oldDatasources.getCount();++i){
            Datasource datasource = oldDatasources.get(i);
            DatasourceConnectionInfo dConn = datasource.getConnectionInfo();
            if(CommonUtils.isFileTypeSource(dConn)){
                fileSources.put(i,datasources.get(i));
            }
        }
        // 关闭文件型数据源
        fileSources.values().stream().forEach(datasource->datasource.close());
        // 重新指定路径打开
        for(Integer i:fileSources.keySet()){
            Datasource datasource = oldDatasources.get(i);
            DatasourceConnectionInfo dConn = datasource.getConnectionInfo();
            Path dPath = Paths.get(dConn.getServer());
            Path dNewPath = wPath.resolveSibling(dPath.getFileName().toString());
            DatasourceConnectionInfo dNewConn = DatasourceUtilities.copyDatasourceConnectionInfo(dConn);
            dNewConn.setServer(dNewPath.toString());
            datasources.create(dNewConn);
        }
        // 保存
        workspace.save();
        workspace.close();
        // 上传工作空间
        return wPath;
    }

    public static void uploadDatasource(Datasource datasource){
        DatasourceConnectionInfo connectionInfo = datasource.getConnectionInfo();
        if(isFileTypeSource(connectionInfo)){
            Path path = Paths.get(connectionInfo.getServer());
            if (!Files.exists(path)){
                return;
            }

            CommonUtils.uploadAsync("/datas/"+path.getFileName().toString(),path,evt -> {
                if (evt.getPropertyName().equals("progress")) {
                    Integer progress = (Integer) evt.getNewValue();
                    if(progress==100){
                        ApplicationContextUtils.getOutput().output(path.getFileName().toString()+"上传完成");
                    }
                }
            });
            if(connectionInfo.getServer().endsWith(".udb")) {
                String uddPathStr = connectionInfo.getServer();
                uddPathStr = uddPathStr.substring(0, uddPathStr.length() - 1) + "d";
                Path uddPath = Paths.get(uddPathStr);
                if (Files.exists(uddPath)) {
                    CommonUtils.uploadAsync("/datas/" + uddPath.getFileName().toString(), uddPath, evt -> {
                        if (evt.getPropertyName().equals("progress")) {
                            Integer progress = (Integer) evt.getNewValue();
                            if (progress == 100) {
                                ApplicationContextUtils.getOutput().output(uddPath.getFileName().toString() + "上传完成");
                            }
                        }
                    });
                }
            }
        }
    }


    public static boolean tipIfNotExist(String path){
        try{
            JSONObject reJson = GafClient.instance().objectMetadata(path);
            JSONObject data = reJson.getJSONObject("data");
            if(404==reJson.getInteger("status")){
                JOptionPaneUtilities.showErrorMessageDialog("文件不存在");
                return true;
            }
        }catch (Exception e){}
        return false;
    }

}

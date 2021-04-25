/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.desktop.develop.work;

import com.supermap.desktop.develop.entity.PresignUploadRequest;
import com.supermap.desktop.develop.exception.FileUploadException;
import com.supermap.desktop.develop.utils.CommonUtils;

import javax.swing.*;
import java.nio.file.Path;

/**
 * @date:2021/3/25
 * @author heykb
 */
public class FileUploadWork extends SwingWorker<Object, Object> {
    private PresignUploadRequest uploadRequest;
    private Path path;

    public FileUploadWork(PresignUploadRequest uploadRequest, Path path) {
        this.uploadRequest = uploadRequest;
        this.path = path;
    }

    @Override
    protected Object doInBackground() throws FileUploadException {
        CommonUtils.uploadByPreSignedUrl(uploadRequest,path,(progress,conn) -> {
            // todo: 更新进度条
            setProgress(progress);
        });
        return null;
    }
}

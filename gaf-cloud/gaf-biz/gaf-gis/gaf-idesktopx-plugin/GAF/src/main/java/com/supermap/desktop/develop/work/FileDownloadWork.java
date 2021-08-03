/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.desktop.develop.work;

import com.supermap.desktop.develop.exception.FileDownloadException;
import com.supermap.desktop.develop.utils.CommonUtils;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.nio.file.Path;

/**
 * @author heykb
 * @date:2021/3/25
 */
public class FileDownloadWork extends SwingWorker<Object, Object> {
    private String preSignedUrl;
    private Path path;

    public FileDownloadWork(String preSignedUrl, Path path) {
        this.preSignedUrl = preSignedUrl;
        this.path = path;
    }

    @Override
    protected Object doInBackground() throws FileDownloadException, FileNotFoundException {
        CommonUtils.downloadByPreSignedUrl(preSignedUrl, path, (progress, conn) -> {
            // todo: 更新进度条
            setProgress(progress);
        });
        return null;
    }
}

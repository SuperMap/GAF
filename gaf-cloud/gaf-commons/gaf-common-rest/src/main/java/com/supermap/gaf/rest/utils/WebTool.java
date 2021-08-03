/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.rest.utils;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class WebTool {
    private static String WEB_PATH = "";

    /**
     * <p>
     * 设置web工程根路径，对应webapp目录
     * </p>
     *
     * @param webPath
     * @since 1.0.0
     */
    public static void setWebConfigPath(String webPath) {
        WEB_PATH = webPath;
    }

    /**
     * <p>
     * 获取web工程根路径
     * </p>
     *
     * @return
     * @since 1.0.0
     */
    public static String getWebConfigPath() {
        return WEB_PATH;
    }

    /**
     * <p>
     * 获取文件在web中的真实路径。如果参数file绝对文件，则返回去绝对路径。如果file是相对web工程的相对路径，则返回其完整文件路径
     * </p>
     *
     * @param filePath
     * @return
     * @since 1.0.0
     */
    public static String getWebFilePath(String filePath) {
        if (filePath == null || "".equals(filePath)) {
            return filePath;
        }
        String path = filePath;
        File file = new File(filePath);
        if (!file.isAbsolute()) {
            String webRootPath = getWebConfigPath();
            File resultFile = new File(webRootPath, filePath);
            try {
                path = resultFile.getCanonicalPath();
            } catch (IOException e) {
                path = resultFile.getAbsolutePath();
            }
        }
        return path;
    }


    public static <T> T parseQueryParam(HttpServletRequest request, Class<T> clazz) {
        return null;
    }


}

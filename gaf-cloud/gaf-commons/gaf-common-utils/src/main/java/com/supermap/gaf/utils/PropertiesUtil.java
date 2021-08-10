/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * <p>
 * properties文件解析工具，提供加载，读取属性，修改保存等工具
 * </p>
 *
 * @author ${Author}
 * @version ${Version}
 * @date:2021/3/25
 * @since 1.0.0
 */
public class PropertiesUtil {

    /**
     * <p>
     * 加载指定properties文件
     * </p>
     *
     * @param filePath
     * @return
     * @since 1.0.0
     */
    public static Properties parseProperties(String propFile) {
        Properties props = new Properties();
        File Propertyfile = new File(propFile);
        if (Propertyfile.exists()) {
            try (InputStream in = new FileInputStream(Propertyfile)) {
                props.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props;
    }

    /**
     * <p>
     * 获取属性值
     * </p>
     *
     * @param filePath
     * @return
     * @since 1.0.0
     */
    public static boolean saveProperties(Properties props, String filePath, String comments) {
        File propFile = new File(filePath);
        if (!propFile.exists()) {
            propFile.mkdirs();
            try {
                propFile.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }
        try (OutputStream outs = new FileOutputStream(propFile)) {
            props.store(outs, comments);
            return true;
        } catch (IOException e) {
        }
        return false;
    }
}

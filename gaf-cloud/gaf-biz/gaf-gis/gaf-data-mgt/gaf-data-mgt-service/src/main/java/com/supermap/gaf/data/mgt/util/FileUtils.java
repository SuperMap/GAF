/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.util;

import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:yd
 * @Date 2021-3-02
 */
public final class FileUtils {
    private static Logger logger = LogUtil.getLocLogger(FileUtils.class);

    private FileUtils() {
    }

    /**
     * 删除一个文件夹下的所有文件(包括子目录内的文件)
     *
     * @param file
     */
    public static boolean deleteFile(File file) {
        try {
            //判断文件不为null或文件目录存在
            if (file == null || !file.exists()) {
                logger.error("文件删除失败,请检查文件路径是否正确");
                return false;
            }
            //取得这个目录下的所有子文件对象
            File[] files = file.listFiles();
            //遍历该目录下的文件对象
            for (File f : files) {
                //判断子目录是否存在子目录,如果是文件则删除
                if (f.isDirectory()) {
                    deleteFile(f);
                } else {
                    f.delete();
                }
            }
            //删除空文件夹  for循环已经把上一层节点的目录清空。
            file.delete();
            return true;
        } catch (Exception e) {
            logger.error("文件删除失败", e);
        }
        return false;
    }

    /**
     * 获取文件夹下第一级子文件路径
     *
     * @param file
     * @return
     */
    public static List<String> listFileName(File file) {
        List<String> names = new ArrayList<>(8);
        try {
            //判断文件不为null或文件目录存在
            if (file == null || !file.exists()) {
                logger.error("查询失败，请检查文件路径是否正确");
                return names;
            }
            //取得这个目录下的所有子文件对象
            File[] files = file.listFiles();
            //遍历该目录下的文件对象
            for (File f : files) {
                //判断子目录是否存在子目录,如果是文件则删除
                if (f.isDirectory()) {
                    String name = f.getName();
                    names.add(name);
                }
            }
            return names;
        } catch (Exception e) {
            logger.error("查询文件子文件名称列表异常", e);
        }
        return names;
    }
}

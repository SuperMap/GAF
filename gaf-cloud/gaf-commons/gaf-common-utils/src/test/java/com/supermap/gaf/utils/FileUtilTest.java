/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class FileUtilTest {

    @Test
    public void testCombine() {
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            assertEquals("/myfile/test", FileUtil.combine("/myfile/", "test"));
            assertEquals("/myfile/test", FileUtil.combine("/myfile/", "/test"));
            assertEquals("/myfile/test", FileUtil.combine("/myfile", "test"));
        }
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            //windows
            assertEquals("C:\\myfile\\test", FileUtil.combine("C://myfile/", "test"));
            assertEquals("C:\\myfile\\test", FileUtil.combine("C://myfile/", "/test"));
            assertEquals("C:\\myfile\\test", FileUtil.combine("C://myfile", "test"));
        }
    }

    @Test
    public void copyFileTest() {
        System.out.println("== copy文件测试 ==");
        String oldPath = "src/test/resources/copyTest.txt";
        String newPath = "src/test/resources/Test01.txt";
        File file = new File(newPath);
        if (file.exists()) {
            file.delete();
        }
        FileUtil.copyFile(oldPath, newPath);
        File copyFile = new File(newPath);
        Assert.assertTrue("测试失败", copyFile.exists());
    }

    @Test
    public void getAllFileTest() {
        System.out.println("== 获取指定路径下的所有文件测试 ==");
        File file = new File("src/main/java/com/supermap/gaf/utils");
        ArrayList<File> files = new ArrayList<>();
        List<File> fileList = FileUtil.getAllFile(file, files);
        for (File file1 : fileList) {
            System.out.println(file1.getName());
        }
        Assert.assertTrue("测试失败", !fileList.isEmpty());
    }

    @Test
    public void getAllFolderTest() {
        System.out.println("== 获取指定路径下的所有目录测试 ==");
        File file = new File("src/main");
        ArrayList<File> files = new ArrayList<>();
        List<File> folder = FileUtil.getAllFolder(file, files);
        for (File file1 : folder) {
            System.out.println(file1.getName());
        }
        Assert.assertTrue("测试失败", !folder.isEmpty());
    }
}

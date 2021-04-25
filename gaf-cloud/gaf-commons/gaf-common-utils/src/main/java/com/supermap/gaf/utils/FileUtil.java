/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.utils;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @program: app-landuse
 * @description:
 * @author: lidong
 * @date:2021/3/25
 * @create: 2018/09/27
 */
public class FileUtil {

    private static Logger logger = LogUtil.getLocLogger(FileUtil.class);
    public static String tempDirRoot = "/";

    public static String getSysTempDir() {
        return new File(tempDirRoot).getAbsolutePath();
    }

    /**
     * 获取某文件夹下所有的文件
     *
     * @param file
     * @param allfilelist
     * @return
     */
    public static List<File> getAllFile(File file, List<File> allfilelist) {
        if (file.exists()) { // 判断文件是否是文件夹，如果是，开始递归
            if (file.isDirectory()) {
                File f[] = file.listFiles();
                for (File file2 : f) {
                    getAllFile(file2, allfilelist);
                }
            } else {
                allfilelist.add(file);
            }
        }
        return allfilelist;
    }

    /**
     * 获取某文件夹下所有的文件夹
     *
     * @param file
     * @param allfolderlist
     * @return
     */
    public static List<File> getAllFolder(File file, List<File> allfolderlist) {
        if (file.exists()) { // 判断文件是否是文件夹，如果是，开始递归
            if (file.isDirectory()) {
                allfolderlist.add(file);
                File f[] = file.listFiles();
                for (File file2 : f) {
                    getAllFolder(file2, allfolderlist);
                }
            }
        }
        return allfolderlist;
    }

    /**
     * 组装文件路径，返回完整的绝对路径
     *
     * @param parentPath
     * @param subPath
     * @return
     */
    public static String combine(String parentPath, String subPath) {
        return new File(parentPath, subPath).getAbsolutePath();
    }

    /**
     * 将文件字节大小转为KB\MB\GB等
     *
     * @param size
     * @return
     */
    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * 解压
     *
     * @param zipPath    压缩文件
     * @param targetpath 解压结果目录
     */
    public static void decompressing(String zipPath, String targetpath) throws IOException {
        File targetDir = new File(targetpath);
        try (ZipArchiveInputStream inputStream = new ZipArchiveInputStream(new FileInputStream(zipPath));) {
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            ZipArchiveEntry entry = null;
            while ((entry = inputStream.getNextZipEntry()) != null) {
                if (entry.isDirectory()) {
                    File directory = new File(targetDir, entry.getName());
                    directory.mkdirs();
                } else {
                    try (OutputStream os = new BufferedOutputStream(new FileOutputStream(new File(targetDir, entry.getName())));) {
                        //输出文件路径信息
                        IOUtils.copy(inputStream, os);
                    } catch (IOException e) {
                        logger.debug(e.getMessage(), e);
                    }
                }
            }
        } catch (IOException e) {
            e.getStackTrace();
            throw new IOException(e);
        }
    }

    /**
     * 压缩
     *
     * @param zipFileName 压缩包路径
     * @param fl          要压缩的文件
     */
    public static void compressing(String zipFileName, File[] fl) throws IOException {
        if (fl == null || fl.length == 0) {
            return;
        }
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));) {
            for (int i = 0; i < fl.length; i++) {
                out.putNextEntry(new ZipEntry(fl[i].getName()));
                try (FileInputStream in = new FileInputStream(fl[i]);) {
                    IOUtils.copy(in, out);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw e;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

//    public static void main(String[] args) {
//        String filePath = "C:\\Users\\h_sha\\Desktop\\2Efmaq";
////        File srcDir = new File(filePath);
////        FileUtil.compressing(filePath + "\\tearget.zip", srcDir.listFiles());
//        FileUtil.decompressing(filePath + "\\2Efmaq.zip", filePath + "\\tetout1");
//    }

    /**
     * 复制文件
     *
     * @param oldPath 源文件绝对路径
     * @param newPath 目标文件绝对路径
     */
    public static void copyFile(String oldPath, String newPath) {
        try (InputStream inStream = new FileInputStream(oldPath); // 读入原文件  
             FileOutputStream fs = new FileOutputStream(newPath);) {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时 ;
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小  
                    // System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将输入流写入到输出流实现文件拷贝
     *
     * @param inputStream
     * @param outputStream
     */
    public static void copyFileByStream(InputStream inputStream, OutputStream outputStream) {
        try {
            int bytesum = 0;
            int byteread = 0;
            byte[] buffer = new byte[1444];
            while ((byteread = inputStream.read(buffer)) != -1) {
                bytesum += byteread;
                outputStream.write(buffer, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件 可以根据正则表达式查找
     *
     * @param dir String 文件夹名称
     * @param s   String 查找文件名，可带*.?进行模糊查询
     * @return File[] 找到的文件
     */
    public static File[] getFiles(String dir, String s) {
        // 开始的文件夹
        File file = new File(dir);
        s = s.replace('.', '#');
        s = s.replaceAll("#", "\\\\.");
        s = s.replace('*', '#');
        s = s.replaceAll("#", ".*");
        s = s.replace('?', '#');
        s = s.replaceAll("#", ".?");
        s = "^" + s + "$";

        Pattern p = Pattern.compile(s);
        ArrayList<File> list = filePattern(file, p);

        File[] rtn = new File[list.size()];
        list.toArray(rtn);
        return rtn;
    }

    /**
     * @param file File 起始文件夹
     * @param p    Pattern 匹配类型
     * @return ArrayList 其文件夹下的文件夹
     */
    private static ArrayList<File> filePattern(File file, Pattern p) {
        if (file == null) {
            return null;
        } else if (file.isFile()) {
            Matcher fMatcher = p.matcher(file.getName());
            if (fMatcher.matches()) {
                ArrayList<File> list = new ArrayList<File>();
                list.add(file);
                return list;
            }
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                ArrayList<File> list = new ArrayList<File>();
                for (int i = 0; i < files.length; i++) {
                    ArrayList<File> rlist = filePattern(files[i], p);
                    if (rlist != null) {
                        list.addAll(rlist);
                    }
                }
                return list;
            }
        }
        return null;
    }

    /**
     * 从url获取文件内容并保存到指定位置
     *
     * @param urlPath     url路径
     * @param downloadDir 下载路径
     * @param fileName    文件名
     */
    public static void download(String urlPath, String downloadDir, String fileName) throws IOException {
        OutputStream out = null;
        BufferedInputStream bin = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            //设置超时
            httpURLConnection.setConnectTimeout(1000 * 5);
            //设置请求方式，默认是GET
//            httpURLConnection.setRequestMethod("POST");
            // 打开到此 URL引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();

            // 建立链接从请求中获取数据
            bin = new BufferedInputStream(httpURLConnection.getInputStream());
            // 指定文件名称
            String path = downloadDir + File.separatorChar + fileName;
            File file = new File(path);
            // 校验文件夹目录是否存在，不存在就创建一个目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[2048];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != bin) {
                bin.close();
            }
            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * 从url获取文件内容
     *
     * @param urlPath url路径
     */
    public static String getFileContentFromUrl(String urlPath) throws IOException {
        String content = "";
        BufferedInputStream bin = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            //设置超时
            httpURLConnection.setConnectTimeout(1000 * 5);
            //设置请求方式，默认是GET
//            httpURLConnection.setRequestMethod("POST");
            // 打开到此 URL引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();
            // 建立链接从请求中获取数据
            bin = new BufferedInputStream(httpURLConnection.getInputStream());
            int size = 0;
            byte[] buf = new byte[2048];
            while ((size = bin.read(buf)) != -1) {
                content += new String(buf, 0, size);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != bin) {
                bin.close();
            }
        }
        return content;
    }

    /**
     * 删除文件夹
     * 需要注意的是当删除某一目录时，必须保证该目录下没有其他文件才能正确删除，否则将删除失败。
     *
     * @param folder
     * @throws Exception
     */
    public static void deleteFolder(File folder) throws Exception {
        if (!folder.exists()) {
            throw new Exception("文件不存在");
        }
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    //递归直到目录下没有文件
                    deleteFolder(file);
                } else {
                    //删除
                    file.delete();
                }
            }
        }
        //删除
        folder.delete();
    }
}

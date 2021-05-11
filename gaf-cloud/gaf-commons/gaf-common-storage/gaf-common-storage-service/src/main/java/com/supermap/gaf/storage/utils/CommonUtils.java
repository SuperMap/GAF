/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.storage.utils;

import com.supermap.gaf.storage.entity.PresignUploadRequest;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * @date:2021/3/25
 * @author heykb
 */
public class CommonUtils {
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

    public static void uploadByPreSignedUrl(PresignUploadRequest uploadRequest, InputStream in) throws FileUploadException {
        HttpURLConnection connection = null;
        OutputStream out = null;
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
            long nread = 0L;
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) > 0) {
                out.write(buf, 0, n);
                nread += n;

            }
            int code = connection.getResponseCode();
            if(code!=200){
                throw new RuntimeException(code+" 检查MD5");
            }

        }catch (Exception e){
            throw new FileUploadException();
        }finally {
            try {
                out.flush();
                out.close();
                connection.disconnect();
            } catch (IOException e) {
            }
        }
    }
}

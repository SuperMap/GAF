/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.common.storage.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.policy.*;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.auth.policy.conditions.StringCondition;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.supermap.gaf.common.storage.config.StorageFileDownloadException;
import com.supermap.gaf.common.storage.config.StorageFileUploadException;
import com.supermap.gaf.common.storage.entity.MinioConfig;
import com.supermap.gaf.common.storage.entity.PresignUploadRequest;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.amazonaws.auth.policy.conditions.StringCondition.StringComparisonType.StringEquals;

/**
 * @author heykb
 * @date:2021/3/25
 */
public class CommonStorageUtils {

    private static final Map<String, Long> INITED_BUCKETS_CACHE = new ConcurrentHashMap<>();
    public static void initBucket(AmazonS3 s3Client, MinioConfig minioConfig) {
        String cacheKey = minioConfig.getServiceEndpoint() + "_" + minioConfig.getBucketName();
        if (!containsKey(cacheKey)) {
            List<Bucket> buckets = s3Client.listBuckets();
            boolean has = false;
            long now = System.currentTimeMillis();
            for (Bucket bucket : buckets) {
                INITED_BUCKETS_CACHE.put(minioConfig.getServiceEndpoint() + "_" + bucket.getName(), now);
                has = has ? true : bucket.getName().equals(minioConfig.getBucketName());
            }
            if (!has) {
                try {
                    s3Client.createBucket(minioConfig.getBucketName());
                    INITED_BUCKETS_CACHE.put(cacheKey, now);
                    initBucketPolicy(s3Client, minioConfig.getBucketName());
                } catch (AmazonS3Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    static boolean containsKey(String bucketKey) {
        Long start = INITED_BUCKETS_CACHE.get(bucketKey);
        boolean re = false;
        if (start != null) {
            long now = System.currentTimeMillis();
            if (now - start > 1000 * 60) {
                re = false;
                INITED_BUCKETS_CACHE.remove(bucketKey);
            } else {
                re = true;
            }
        }
        return re;
    }
    static public void initBucketPolicy(AmazonS3 s3Client, String bucketName) {
        List<String> prefixs = Arrays.asList("**/public/", "public/");
        Statement one = new Statement(Statement.Effect.Allow)
                .withPrincipals(Principal.AllUsers)
                .withActions(S3Actions.GetBucketLocation)
                .withResources(new Resource(
                        "arn:aws:s3:::" + bucketName));
        Statement two = new Statement(Statement.Effect.Allow)
                .withPrincipals(Principal.AllUsers)
                .withActions(S3Actions.ListObjects)
                .withResources(new Resource(
                        "arn:aws:s3:::" + bucketName))
                .withConditions(prefixs.stream().map(prefix -> new StringCondition(StringEquals, "s3:prefix", prefix)).toArray(Condition[]::new));
        Statement three = new Statement(Statement.Effect.Allow)
                .withPrincipals(Principal.AllUsers)
                .withActions(S3Actions.GetObject)
                .withResources(prefixs.stream().map(prefix -> new Resource(
                        "arn:aws:s3:::" + bucketName + "/" + prefix + "*")).toArray(Resource[]::new));
        Policy bucket_policy = new Policy().withStatements(one, two, three);
        s3Client.setBucketPolicy(bucketName, bucket_policy.toJson());
    }
    public static AmazonS3 createClient(MinioConfig minioConfig) {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(minioConfig.getServiceEndpoint(), Regions.DEFAULT_REGION.getName()))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())))
                .enablePathStyleAccess()
                .build();
        return s3Client;
    }

    public static String getBase64Md5(Path path) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        try (InputStream in = Files.newInputStream(path)) {
            return getBase64Md5(in);
        }
    }

    public static String getBase64Md5(InputStream in) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[8192];
        int length;
        while ((length = in.read(buffer)) != -1) {
            //md5计算
            md5.update(buffer, 0, length);
        }
        String base64Md5 = Base64.getEncoder().encodeToString(md5.digest());
        return base64Md5;
    }

    public static void uploadByPreSignedUrl(PresignUploadRequest uploadRequest, File file) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(mediaType, file);
        Request.Builder builder = new Request.Builder()
                .url(uploadRequest.getPresignUrl())
                .method("PUT", body)
                .addHeader("Content-Type", "application/octet-stream");

        if (!StringUtils.isEmpty(uploadRequest.getContentMd5())) {
            builder.addHeader("Content-MD5", uploadRequest.getContentMd5())
                    .addHeader("x-amz-meta-base64md5", uploadRequest.getContentMd5());
        }
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new StorageFileUploadException(response.code() + " " + response.body().string());
            }
        } catch (IOException e) {
            throw new StorageFileUploadException(e);
        }

    }

    public static void downloadByPreSignedUrl(String preSignedUrl, Path path) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(preSignedUrl)
                .method("GET", null)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new StorageFileDownloadException(response.code() + " " + response.body().string());
            }
            Files.deleteIfExists(path);
            long contentLength = response.body().contentLength();
            if (contentLength > 0) {
                try (FileOutputStream out = new FileOutputStream(path.toFile());
                     InputStream in = new BufferedInputStream(response.body().byteStream())) {
                    byte[] buf = new byte[8192];
                    int n;
                    long nread = 0L;
                    long lastBytes;
                    while ((lastBytes = contentLength - nread) > 0 && (n = in.read(buf, 0, lastBytes < buf.length ? (int) lastBytes : buf.length)) > 0) {
                        out.write(buf, 0, n);
                        nread += n;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void uploadByPreSignedUrl(PresignUploadRequest uploadRequest, InputStream in) {
        HttpURLConnection connection = null;
        OutputStream out = null;
        try {
            String preSignedUrl = uploadRequest.getPresignUrl();
            String base64Md5 = uploadRequest.getContentMd5();
            URL url = new URL(preSignedUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            connection.setUseCaches(false);
            if (!StringUtils.isEmpty(base64Md5)) {
                connection.setRequestProperty("Content-MD5", base64Md5);
                connection.setRequestProperty("x-amz-meta-base64md5", base64Md5);
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
            if (code != 200) {
                String res = StreamUtils.copyToString(connection.getErrorStream(), StandardCharsets.UTF_8);
                throw new RuntimeException(code + " " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new StorageFileUploadException();
        } finally {
            try {
                out.flush();
                out.close();
                connection.disconnect();
            } catch (IOException e) {
            }
        }
    }


    public static void downloadByPreSignedUrl(String preSignedUrl, OutputStream out) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(preSignedUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            if (code != 200) {
                String res = StreamUtils.copyToString(connection.getErrorStream(), StandardCharsets.UTF_8);
                throw new RuntimeException(code + " " + res);
            }
            DataInputStream in = new DataInputStream(connection.getInputStream());
            long fileSize = connection.getContentLength();
            long nread = 0L;
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) > 0) {
                out.write(buf, 0, n);
                nread += n;
            }
        } catch (Exception e) {
            throw new StorageFileDownloadException(e.getMessage());
        } finally {
            connection.disconnect();

        }
    }

    public static void main(String[] args) throws IOException {
        downloadByPreSignedUrl("http://192.168.11.118:9000/gaf/tenant0/1.gif?response-content-disposition=attachment&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20210802T092330Z&X-Amz-SignedHeaders=host&X-Amz-Expires=900&X-Amz-Credential=admin%2F20210802%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Signature=39458d3d3b75124be304036b2e1be6215ded62dc08cf5fbbd32ba4666cef58c2", Paths.get("C:\\Users\\kb\\Documents\\ZZDS\\2.gif"));
    }
}

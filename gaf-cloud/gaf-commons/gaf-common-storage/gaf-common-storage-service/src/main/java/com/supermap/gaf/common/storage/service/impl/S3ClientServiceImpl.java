/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.common.storage.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.policy.*;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.auth.policy.conditions.StringCondition;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.*;
import com.supermap.gaf.common.storage.entity.MinioConfig;
import com.supermap.gaf.common.storage.entity.ObjectInfo;
import com.supermap.gaf.common.storage.entity.VolumePathReturn;
import com.supermap.gaf.common.storage.handler.MinioConfigHandlerI;
import com.supermap.gaf.common.storage.service.S3ClientService;
import com.supermap.gaf.common.storage.utils.CommonStorageUtils;
import com.supermap.gaf.common.storage.web.SelectModeI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.amazonaws.auth.policy.conditions.StringCondition.StringComparisonType.StringEquals;


/**
 * @author heykb
 * @date:2021/3/25
 */
public class S3ClientServiceImpl implements S3ClientService {

    public static final String EMPTY_DIR_FILE = ".createDir";
    private static final Logger logger = LoggerFactory.getLogger(S3ClientServiceImpl.class);
    private static final Map<String, Long> initedBuckets = new ConcurrentHashMap<>();

    private MinioConfigHandlerI minioConfigHandlerI;

    public S3ClientServiceImpl(MinioConfigHandlerI minioConfigHandlerI) {

        this.minioConfigHandlerI = minioConfigHandlerI;
    }

    @Override
    public AmazonS3 s3Client(String configName, SelectModeI selectMode) {
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        return s3Client;
    }


    @Override
    public URL getUrl(String configName, String keyName, SelectModeI selectMode) {
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        initBucket(s3Client, minioConfig);
        return s3Client.getUrl(minioConfig.getBucketName(), minioConfigHandlerI.encodeKeyName(minioConfig, keyName));
    }

    @Override
    public void putObject(String configName, String keyName, File file, SelectModeI selectMode) {
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        initBucket(s3Client, minioConfig);
        s3Client.putObject(minioConfig.getBucketName(), minioConfigHandlerI.encodeKeyName(minioConfig, keyName), file);
    }

    @Override
    public String getUploadSignUrl(String configName, String keyName, String contentMd5, SelectModeI selectMode) {
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        initBucket(s3Client, minioConfig);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(minioConfig.getBucketName(), minioConfigHandlerI.encodeKeyName(minioConfig, keyName), HttpMethod.PUT);
        /**
         * private | public-read | public-read-write | authenticated-read | aws-exec-read | bucket-owner-read | bucket-owner-full-control
         */
        if (!StringUtils.isEmpty(contentMd5)) {
            request.putCustomRequestHeader(Headers.S3_USER_METADATA_PREFIX + "base64md5", contentMd5);
            request.putCustomRequestHeader(Headers.CONTENT_MD5, contentMd5);
        }
        //默认15分钟过期
//        request.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * MinioConfig.linkExpiredMinutes));
        URL url = s3Client.generatePresignedUrl(request);
        return url.toString();
    }

    @Override
    public String share(String configName, String keyName, long minute, String secret, SelectModeI selectMode) {
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        initBucket(s3Client, minioConfig);
        return downloadSignUrl(s3Client, minioConfig, minioConfigHandlerI.encodeKeyName(minioConfig, keyName), minute, secret, false);
    }

    String downloadSignUrl(AmazonS3 s3Client, MinioConfig minioConfig, String keyName, long minute, String secret, boolean isPreview) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(minioConfig.getBucketName(), keyName, HttpMethod.GET);
        //默认15分钟过期
        if (!StringUtils.isEmpty(secret)) {
            request.addRequestParameter("secret", secret);
        }
        if (!isPreview) {
            request.setResponseHeaders(new ResponseHeaderOverrides().withContentDisposition("attachment"));
        } else {
            request.setResponseHeaders(new ResponseHeaderOverrides().withContentDisposition("inline"));
        }
        request.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * minute));
        URL url = s3Client.generatePresignedUrl(request);
        return url.toString();
    }

    @Override
    public String getDownloadSignUrl(String configName, String keyName, boolean isPreview, SelectModeI selectMode) {
        List<String> re = new ArrayList<>();
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        initBucket(s3Client, minioConfig);
        return downloadSignUrl(s3Client, minioConfig, minioConfigHandlerI.encodeKeyName(minioConfig, keyName), 15, null, isPreview);
    }

    @Override
    public String createMultiUpload(String configName, String keyName, Map<String, String> userObjectMetadata, SelectModeI selectMode) {
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        initBucket(s3Client, minioConfig);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setUserMetadata(userObjectMetadata);
        InitiateMultipartUploadResult result = s3Client.initiateMultipartUpload(new InitiateMultipartUploadRequest(minioConfig.getBucketName(), minioConfigHandlerI.encodeKeyName(minioConfig, keyName), metadata));
        return result.getUploadId();
    }

    @Override
    public Map<Integer, String> multiUploadSignUrl(String configName, String keyName, String uploadId, int maxPartNum, SelectModeI selectMode) {
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        initBucket(s3Client, minioConfig);
        Map<Integer, String> re = new HashMap<>();
        ListPartsRequest request = new ListPartsRequest(minioConfig.getBucketName(), minioConfigHandlerI.encodeKeyName(minioConfig, keyName), uploadId);
        PartListing listing = s3Client.listParts(request);
        List<PartSummary> partSummaries = listing.getParts();
        Set<Integer> uploadedPart = new HashSet<>();
        for (PartSummary partSummary : partSummaries) {
            uploadedPart.add(partSummary.getPartNumber());
        }
        // 构建剩余分片上传url
        for (int i = 1; i <= maxPartNum; ++i) {
            if (uploadedPart.contains(i)) {
                continue;
            }
            re.put(i, uploadPartSignUrl(s3Client, minioConfig.getBucketName(), minioConfigHandlerI.encodeKeyName(minioConfig, keyName), uploadId, i).toString());
        }
        return re;
    }

    @Override
    public String completeMultiUpload(String configName, String keyName, String uploadId, SelectModeI selectMode) {
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        initBucket(s3Client, minioConfig);
        ListPartsRequest request = new ListPartsRequest(minioConfig.getBucketName(), minioConfigHandlerI.encodeKeyName(minioConfig, keyName), uploadId);
        PartListing listing = s3Client.listParts(request);
        CompleteMultipartUploadRequest request2 = new CompleteMultipartUploadRequest();
        request2.withBucketName(minioConfig.getBucketName()).withKey(minioConfigHandlerI.encodeKeyName(minioConfig, keyName)).withUploadId(uploadId);
        List<PartETag> partETags = listing.getParts().stream().map(part -> new PartETag(part.getPartNumber(), part.getETag())).collect(Collectors.toList());
        request2.setPartETags(partETags);
        s3Client.completeMultipartUpload(request2);
        return s3Client.getUrl(minioConfig.getBucketName(), minioConfigHandlerI.encodeKeyName(minioConfig, keyName)).toString();

    }

    @Override
    public String completeMultiUpload(String configName, String keyName, String uploadId, List<PartETag> partETags, SelectModeI selectMode) {
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        initBucket(s3Client, minioConfig);
        CompleteMultipartUploadRequest request2 = new CompleteMultipartUploadRequest();
        request2.withBucketName(minioConfig.getBucketName()).withKey(minioConfigHandlerI.encodeKeyName(minioConfig, keyName)).withUploadId(uploadId);
        request2.setPartETags(partETags);
        s3Client.completeMultipartUpload(request2);
        return s3Client.getUrl(minioConfig.getBucketName(), minioConfigHandlerI.encodeKeyName(minioConfig, keyName)).toString();
    }

    @Override
    public Map<Integer, String> uploadPartsSignUrl(String configName, String keyName, String uploadId, List<Integer> partNums, SelectModeI selectMode) {
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        initBucket(s3Client, minioConfig);
        Map<Integer, String> re = new HashMap<>();
        for (Integer partNum : partNums) {
            re.put(partNum, uploadPartSignUrl(s3Client, minioConfig.getBucketName(), minioConfigHandlerI.encodeKeyName(minioConfig, keyName), uploadId, partNum).toString());
        }
        return re;
    }

    @Override
    public void abortMultiUpload(String configName, String keyName, String uploadId, SelectModeI selectMode) {
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        initBucket(s3Client, minioConfig);
        AbortMultipartUploadRequest request = new AbortMultipartUploadRequest(minioConfig.getBucketName(), minioConfigHandlerI.encodeKeyName(minioConfig, keyName), uploadId);
        try {
            s3Client.abortMultipartUpload(request);
        } catch (AmazonS3Exception e) {
            if (!e.getErrorCode().equals("NoSuchUpload")) {
                throw e;
            }
        }
    }


    @Override
    public ObjectMetadata getObjectMetadata(String configName, String keyName, SelectModeI selectMode) {
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        initBucket(s3Client, minioConfig);
        ObjectMetadata re = s3Client.getObjectMetadata(minioConfig.getBucketName(), minioConfigHandlerI.encodeKeyName(minioConfig, keyName));
        re.getUserMetadata().put("url", s3Client.getUrl(minioConfig.getBucketName(), minioConfigHandlerI.encodeKeyName(minioConfig, keyName)).toString());
        return re;
    }

    @Override
    public List<ObjectInfo> listObjects(String configName, String prefix, boolean recursion, SelectModeI selectMode) {
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        initBucket(s3Client, minioConfig);
        List<ObjectInfo> re = new ArrayList<>();
        ListObjectsRequest req = new ListObjectsRequest().withBucketName(minioConfig.getBucketName()).withPrefix(minioConfigHandlerI.encodeKeyName(minioConfig, prefix));
        if (!recursion) {
            req.withDelimiter("/");
        }
        boolean isTruncated = true;
        while (isTruncated) {
            ObjectListing listing = s3Client.listObjects(req);
            isTruncated = listing.isTruncated();
            req.withMarker(listing.getNextMarker());
            for (String commonPrefix : listing.getCommonPrefixes()) {
                String name = minioConfigHandlerI.decodeKeyName(minioConfig, commonPrefix);
                ObjectInfo info = ObjectInfo.dirInfo(name);
                re.add(info);
            }
            for (S3ObjectSummary summary : listing.getObjectSummaries()) {
                String name = minioConfigHandlerI.decodeKeyName(minioConfig, summary.getKey());
                if (name.endsWith("/" + EMPTY_DIR_FILE)) {
                    continue;
                }
                ObjectInfo info = ObjectInfo.fileInfo(name, summary.getSize(), summary.getLastModified());
                re.add(info);
            }
        }

        return re;
    }
    //    @Override
//    public void deleteObjects(String configName,List<String> keyNames){
//        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
//        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
//        initBucket(s3Client,minioConfig);
//        DeleteObjectsRequest request= new DeleteObjectsRequest(minioConfig.getBucketName());
//        List<DeleteObjectsRequest.KeyVersion> keyVersions = new ArrayList<>();
//        for(String keyName:keyNames){
//            keyVersions.add(new DeleteObjectsRequest.KeyVersion(minioConfigHandlerI.encodeKeyName(minioConfig, keyName)));
//        }
//        request.setKeys(keyVersions);
//        s3Client.deleteObjects(request);
//    }
//
//    @Override
//    public void deleteObjects(List<String> keyNames){
//        deleteObjects(null,keyNames);
//    }

    @Override
    public int deleteObject(String configName, String prefix, SelectModeI selectMode) {
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        initBucket(s3Client, minioConfig);
        DeleteObjectsRequest request = new DeleteObjectsRequest(minioConfig.getBucketName());
        ListObjectsRequest req = new ListObjectsRequest().withBucketName(minioConfig.getBucketName()).withPrefix(minioConfigHandlerI.encodeKeyName(minioConfig, prefix));
        ObjectListing listing = s3Client.listObjects(req);
        List<DeleteObjectsRequest.KeyVersion> keyVersions = new ArrayList<>();
        for (S3ObjectSummary summary : listing.getObjectSummaries()) {
            keyVersions.add(new DeleteObjectsRequest.KeyVersion(summary.getKey()));
        }
        request.setKeys(keyVersions);
        if (!CollectionUtils.isEmpty(keyVersions)) {
            s3Client.deleteObjects(request);
        }

        return keyVersions.size();
    }

    @Override
    public void createEmptyDir(String configName, String path, SelectModeI selectMode) {
        MinioConfig minioConfig = minioConfigHandlerI.getConfig(configName, selectMode);
        AmazonS3 s3Client = CommonStorageUtils.createClient(minioConfig);
        initBucket(s3Client, minioConfig);
        path = path.endsWith("/") ? path + EMPTY_DIR_FILE : path + "/" + EMPTY_DIR_FILE;
        s3Client.putObject(minioConfig.getBucketName(), minioConfigHandlerI.encodeKeyName(minioConfig, path), "");
    }

    @Override
    public BigInteger totalSize(String configName, String prefix, SelectModeI selectMode) {
        BigInteger re = BigInteger.valueOf(0L);
        Long sum = 0L;
        List<ObjectInfo> list = listObjects(configName, prefix, true, selectMode);
        for (ObjectInfo objectInfo : list) {
            if (objectInfo.getObjectType() == ObjectInfo.ObjectType.object) {
                try {
                    sum = Math.multiplyExact(sum, objectInfo.getSize());
                } catch (ArithmeticException e) {
                    re.add(BigInteger.valueOf(sum));
                    sum = objectInfo.getSize();
                }
            }
        }
        re.add(BigInteger.valueOf(sum));
        return re;
    }

    @Override
    public VolumePathReturn getVolumePath(String configName, String path, boolean returnUrl, SelectModeI selectModeI) {
        return minioConfigHandlerI.getVolumePath(configName, path, returnUrl, selectModeI);
    }


    private URL uploadPartSignUrl(AmazonS3 s3Client, String bucketName, String keyName, String uploadId, Integer partNum) {
        GeneratePresignedUrlRequest uploadPartRequest = new GeneratePresignedUrlRequest(bucketName, keyName, HttpMethod.PUT);
        uploadPartRequest.addRequestParameter("uploadId", uploadId);
        uploadPartRequest.addRequestParameter("partNumber", Integer.toString(partNum));
        URL url = s3Client.generatePresignedUrl(uploadPartRequest);
        return url;
    }

    private void initBucketPolicy(AmazonS3 s3Client, String bucketName) {
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


    boolean containsKey(String bucketKey) {
        Long start = initedBuckets.get(bucketKey);
        boolean re = false;
        if (start != null) {
            long now = System.currentTimeMillis();
            if (now - start > 1000 * 60) {
                re = false;
                initedBuckets.remove(bucketKey);
            } else {
                re = true;
            }
        }
        return re;
    }

    private void initBucket(AmazonS3 s3Client, MinioConfig minioConfig) {
        String cacheKey = minioConfig.getServiceEndpoint() + "_" + minioConfig.getBucketName();
        if (!containsKey(cacheKey)) {
            List<Bucket> buckets = s3Client.listBuckets();
            boolean has = false;
            long now = System.currentTimeMillis();
            for (Bucket bucket : buckets) {
                initedBuckets.put(minioConfig.getServiceEndpoint() + "_" + bucket.getName(), now);
                has = has ? true : bucket.getName().equals(minioConfig.getBucketName());
            }
            if (!has) {
                try {
                    s3Client.createBucket(minioConfig.getBucketName());
                    initedBuckets.put(cacheKey, now);
                    initBucketPolicy(s3Client, minioConfig.getBucketName());
                } catch (AmazonS3Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

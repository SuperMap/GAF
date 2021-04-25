/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.storage.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.policy.*;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.auth.policy.conditions.StringCondition;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.*;
import com.supermap.gaf.storage.config.MinioConfig;
import com.supermap.gaf.storage.service.MinioConfigHandlerI;
import com.supermap.gaf.storage.service.S3ClientService;
import com.supermap.gaf.storage.service.TenantMinioConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.security.sasl.AuthenticationException;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.amazonaws.auth.policy.conditions.StringCondition.StringComparisonType.StringEquals;
import static com.supermap.gaf.storage.enums.TenantMode.MULTI_NODE;
import static com.supermap.gaf.storage.enums.TenantMode.SINGLE_NODE_MULTI_BUCKET;


/**
 * @date:2021/3/25
 * @author heykb
 */
@Service
public class S3ClientServiceImpl implements S3ClientService {

    @Autowired
    private MinioConfigHandlerI minioConfigHandlerI;

    @Autowired
    private TenantMinioConfigService tenantMinioConfigService;

    @Override
    public AmazonS3 s3Client(){
        MinioConfig minioConfig = minioConfigHandlerI.getConfig();
        return createClient(minioConfig);
    }

    private AmazonS3 createClient(MinioConfig minioConfig){
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(minioConfig.getServiceEndpoint(), Regions.DEFAULT_REGION.getName()))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())))
                .build();
        return s3Client;
    }

    @PostConstruct
    @Override
    public void initBucket() throws AuthenticationException {
        List<MinioConfig> configs = new ArrayList<>();
        if(tenantMinioConfigService.getMode() == MULTI_NODE || tenantMinioConfigService.getMode()==SINGLE_NODE_MULTI_BUCKET){
            configs.addAll(tenantMinioConfigService.getConfig().values());
        }else{
            configs.add(tenantMinioConfigService.getSingleConfig());
        }
        for(MinioConfig minioConfig:configs){
            AmazonS3 s3Client = createClient(minioConfig);
            try{
                s3Client.createBucket(minioConfig.getBucketName());
                initBucketPolicy(s3Client,minioConfig.getBucketName());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void initBucketPolicy(AmazonS3 s3Client,String bucketName){
        List<String> prefixs = Arrays.asList("**/public/","public/");
        Statement one = new Statement(Statement.Effect.Allow)
                .withPrincipals(Principal.AllUsers)
                .withActions(S3Actions.GetBucketLocation)
                .withResources(new Resource(
                        "arn:aws:s3:::"+bucketName));
        Statement two = new Statement(Statement.Effect.Allow)
                .withPrincipals(Principal.AllUsers)
                .withActions(S3Actions.ListObjects)
                .withResources(new Resource(
                        "arn:aws:s3:::"+bucketName))
                .withConditions(prefixs.stream().map(prefix->new StringCondition(StringEquals,"s3:prefix",prefix)).toArray(Condition[]::new));
        Statement three = new Statement(Statement.Effect.Allow)
                .withPrincipals(Principal.AllUsers)
                .withActions(S3Actions.GetObject)
                .withResources(prefixs.stream().map(prefix->new Resource(
                        "arn:aws:s3:::"+bucketName+"/"+prefix+"*")).toArray(Resource[]::new));
        Policy bucket_policy = new Policy().withStatements(one,two,three);
        s3Client.setBucketPolicy(bucketName,bucket_policy.toJson());
    }


    @Override
    public URL getUrl(String keyName) throws AuthenticationException {
        return s3Client().getUrl(minioConfigHandlerI.getConfig().getBucketName(),minioConfigHandlerI.encodeKeyName(keyName));
    }

    @Override
    public void putObject(String keyName, File file) throws AuthenticationException {
        s3Client().putObject(minioConfigHandlerI.getConfig().getBucketName(),minioConfigHandlerI.encodeKeyName(keyName),file);
    }
    @Override
    public String getUploadSignUrl(String keyName, String contentMd5) throws AuthenticationException {
        
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(minioConfigHandlerI.getConfig().getBucketName(),minioConfigHandlerI.encodeKeyName(keyName), HttpMethod.PUT);
        /**
         * private | public-read | public-read-write | authenticated-read | aws-exec-read | bucket-owner-read | bucket-owner-full-control
         */
        if(!StringUtils.isEmpty(contentMd5)){
            request.putCustomRequestHeader(Headers.S3_USER_METADATA_PREFIX+"base64md5",contentMd5);
            request.putCustomRequestHeader(Headers.CONTENT_MD5,contentMd5);
        }
        //默认15分钟过期
//        request.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * MinioConfig.linkExpiredMinutes));
        URL url = s3Client().generatePresignedUrl(request);
        return url.toString();
    }


    @Override
    public String getDownloadSignUrl(String keyName) throws AuthenticationException {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(minioConfigHandlerI.getConfig().getBucketName(),minioConfigHandlerI.encodeKeyName(keyName), HttpMethod.GET);
        //默认15分钟过期
//        request.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *  MinioConfig.linkExpiredMinutes));
        URL url = s3Client().generatePresignedUrl(request);
        return url.toString();
    }

    @Override
    public String createMultiUpload(String keyName,Map<String,String> userObjectMetadata) throws AuthenticationException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setUserMetadata(userObjectMetadata);
        InitiateMultipartUploadResult result = s3Client().initiateMultipartUpload(new InitiateMultipartUploadRequest(minioConfigHandlerI.getConfig().getBucketName(),minioConfigHandlerI.encodeKeyName(keyName),metadata));
        return result.getUploadId();
    }

    @Override
    public Map<Integer,String> multiUploadSignUrl(String keyName, String uploadId, int maxPartNum) throws AuthenticationException {

        Map<Integer,String> re = new HashMap<>();
        ListPartsRequest request = new ListPartsRequest(minioConfigHandlerI.getConfig().getBucketName(),minioConfigHandlerI.encodeKeyName(keyName),uploadId);
        PartListing listing = s3Client().listParts(request);
        List<PartSummary> partSummaries = listing.getParts();
        Set<Integer> uploadedPart = new HashSet<>();
        for(PartSummary partSummary:partSummaries){
            uploadedPart.add(partSummary.getPartNumber());
        }
        // 构建剩余分片上传url
        for(int i = 1;i<=maxPartNum;++i){
            if(uploadedPart.contains(i)){
                continue;
            }
            re.put(i,uploadPartSignUrl(minioConfigHandlerI.encodeKeyName(keyName),uploadId,i).toString());
        }
        return re;
    }



    @Override
    public void completeMultiUpload(String keyName, String uploadId) throws AuthenticationException {
        ListPartsRequest request = new ListPartsRequest(minioConfigHandlerI.getConfig().getBucketName(),minioConfigHandlerI.encodeKeyName(keyName),uploadId);
        PartListing listing = s3Client().listParts(request);
        CompleteMultipartUploadRequest request2 = new CompleteMultipartUploadRequest();
        request2.withBucketName(minioConfigHandlerI.getConfig().getBucketName()).withKey(minioConfigHandlerI.encodeKeyName(keyName)).withUploadId(uploadId);
        List<PartETag> partETags = listing.getParts().stream().map(part->new PartETag(part.getPartNumber(),part.getETag())).collect(Collectors.toList());
        request2.setPartETags(partETags);
        s3Client().completeMultipartUpload(request2);
    }

    @Override
    public Map<Integer, String> uploadPartsSignUrl(String keyName, String uploadId, List<Integer> partNums) throws AuthenticationException {
        Map<Integer,String> re = new HashMap<>();
        for(Integer partNum: partNums){
            re.put(partNum,uploadPartSignUrl(minioConfigHandlerI.encodeKeyName(keyName),uploadId,partNum).toString());
        }
        return re;
    }

    @Override
    public void abortMultiUpload(String keyName, String uploadId) throws AuthenticationException {
        AbortMultipartUploadRequest request = new AbortMultipartUploadRequest(minioConfigHandlerI.getConfig().getBucketName(),minioConfigHandlerI.encodeKeyName(keyName),uploadId);
        try{
            s3Client().abortMultipartUpload(request);
        }catch (AmazonS3Exception e){
            if(!e.getErrorCode().equals("NoSuchUpload")){
                throw e;
            }
        }
    }

    @Override
    public ObjectMetadata getObjectMetadata(String keyName) throws AmazonS3Exception, AuthenticationException {
        return s3Client().getObjectMetadata(minioConfigHandlerI.getConfig().getBucketName(),minioConfigHandlerI.encodeKeyName(keyName));
    }

    @Override
    public List<Map<String, Object>> listObjects(String prefix) throws AuthenticationException{
        List<Map<String,Object>> re = new ArrayList<>();
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(minioConfigHandlerI.getConfig().getBucketName()).withPrefix(minioConfigHandlerI.encodeKeyName(prefix)).withDelimiter("/");
        ListObjectsV2Result listing =  s3Client().listObjectsV2(req);
        for (String commonPrefix : listing.getCommonPrefixes()) {
            Map<String,Object> dir = new HashMap<>();
            dir.put("type","commonPrefix");
            dir.put("name",minioConfigHandlerI.decodeKeyName(commonPrefix));
            re.add(dir);
        }
        for (S3ObjectSummary summary: listing.getObjectSummaries()) {
            Map<String,Object> file = new HashMap<>();
            String name = minioConfigHandlerI.decodeKeyName(summary.getKey());
            file.put("type","object");
            file.put("name",name);
            file.put("size",summary.getSize()/1024);
            file.put("lastModified",summary.getLastModified());
            re.add(file);
        }
        return re;
    }

    @Override
    public void deleteObjects(List<String> keyNames) throws AuthenticationException {
        DeleteObjectsRequest request= new DeleteObjectsRequest(minioConfigHandlerI.getConfig().getBucketName());
        List<DeleteObjectsRequest.KeyVersion> keyVersions = new ArrayList<>();
        for(String keyName:keyNames){
            keyVersions.add(new DeleteObjectsRequest.KeyVersion(minioConfigHandlerI.encodeKeyName(keyName)));
        }
        request.setKeys(keyVersions);
        s3Client().deleteObjects(request);
    }

    @Override
    public int deleteObject(String prefix) throws AuthenticationException {
        DeleteObjectsRequest request= new DeleteObjectsRequest(minioConfigHandlerI.getConfig().getBucketName());
        ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(minioConfigHandlerI.getConfig().getBucketName()).withPrefix(minioConfigHandlerI.encodeKeyName(prefix));
        ListObjectsV2Result listing =  s3Client().listObjectsV2(req);
        List<DeleteObjectsRequest.KeyVersion> keyVersions = new ArrayList<>();
        for (S3ObjectSummary summary: listing.getObjectSummaries()) {
            keyVersions.add(new DeleteObjectsRequest.KeyVersion(summary.getKey()));
        }
        request.setKeys(keyVersions);
        if(!CollectionUtils.isEmpty(keyVersions)){
            s3Client().deleteObjects(request);
        }
        return keyVersions.size();
    }

    private URL uploadPartSignUrl(String keyName, String uploadId,Integer partNum) throws AuthenticationException {
        GeneratePresignedUrlRequest uploadPartRequest = new GeneratePresignedUrlRequest(minioConfigHandlerI.getConfig().getBucketName(),keyName, HttpMethod.
                PUT);
        uploadPartRequest.addRequestParameter("uploadId", uploadId);
        uploadPartRequest.addRequestParameter("partNumber", Integer.toString(partNum));
        URL url = s3Client().generatePresignedUrl(uploadPartRequest);
        return url;
    }




    public static void main(String[] args) {


//        s3Client().setBucketPolicy(minioConfigHandlerI.getConfig().getBucketName(),"{\n" +
//                "    \"Rules\": [\n" +
//                "        {\n" +
//                "            \"ID\": \"AbortIncompleteMultipartUpload Rule\",\n" +
//                "            \"Status\": \"Enabled\",\n" +
//                "            \"Filter\": {\n" +
//                "                \"Prefix\": \"\"\n" +
//                "            },\n" +
//                "            \"AbortIncompleteMultipartUpload\": {\n" +
//                "                \"DaysAfterInitiation\": 7\n" +
//                "            }\n" +
//                "        }\n" +
//                "    ]\n" +
//                "}");
//        String keyName = "/img/05.png";
//        AccessControlList acl = s3Client().getObjectAcl(bucketName, keyName);
//
//        // Clear the existing list of grants.
//        acl.getGrantsAsList().clear();
//
//        // Grant a sample set of permissions, using the existing ACL owner for Full Control permissions.
//        acl.grantPermission(new CanonicalGrantee(acl.getOwner().getId()), Permission.Read);
//        s3Client().setObjectAcl(bucketName,minioConfigHandlerI.encodeKeyName(keyName),acl);
    }
}

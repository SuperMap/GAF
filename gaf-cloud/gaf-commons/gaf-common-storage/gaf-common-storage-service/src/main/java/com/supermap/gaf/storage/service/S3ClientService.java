/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.storage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;

import javax.security.sasl.AuthenticationException;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * The interface S 3 client service.
 *
 * @date:2021/3/25
 * @author heykb
 */
public interface S3ClientService {

    /**
     * S 3 client amazon s 3.
     *
     * @return the amazon s 3
     */
    AmazonS3 s3Client();

    /**
     * 创建并初始化平台所有仓库，仅当仓库不存在时
     *
     * @throws AuthenticationException the authentication exception
     */
    void initBucket() throws AuthenticationException;

    /**
     * Gets url.
     *
     * @param keyName the key name
     * @return the url
     * @throws AuthenticationException the authentication exception
     */
    URL getUrl(String keyName) throws AuthenticationException;

    /**
     * Put object.
     *
     * @param keyName the key name
     * @param file    the file
     * @throws AuthenticationException the authentication exception
     */
    void putObject(String keyName, File file) throws AuthenticationException;

    /**
     * Gets upload sign url.
     *
     * @param keyName    the key name
     * @param contentMd5 the content md 5
     * @return the upload sign url
     * @throws AuthenticationException the authentication exception
     */
    String getUploadSignUrl(String keyName,String contentMd5) throws AuthenticationException;

    /**
     * Gets download sign url.
     *
     * @param keyName the key name
     * @return the download sign url
     * @throws AuthenticationException the authentication exception
     */
    String getDownloadSignUrl(String keyName) throws AuthenticationException;

    /**
     * Create multi upload string.
     *
     * @param keyName            the key name
     * @param userObjectMetadata the user object metadata
     * @return the string
     * @throws AuthenticationException the authentication exception
     */
    String createMultiUpload(String keyName,Map<String,String> userObjectMetadata) throws AuthenticationException;

    /**
     * Multi upload sign url map.
     *
     * @param keyName    the key name
     * @param uploadId   the upload id
     * @param maxPartNum the max part num
     * @return the map
     * @throws AuthenticationException the authentication exception
     */
    Map<Integer,String> multiUploadSignUrl(String keyName, String uploadId, int maxPartNum) throws AuthenticationException;

    /**
     * Complete multi upload.
     *
     * @param keyName  the key name
     * @param uploadId the upload id
     * @throws AuthenticationException the authentication exception
     */
    void completeMultiUpload(String keyName, String uploadId) throws AuthenticationException;

    /**
     * Upload parts sign url map.
     *
     * @param path     the path
     * @param uploadId the upload id
     * @param partNums the part nums
     * @return the map
     * @throws AuthenticationException the authentication exception
     */
    Map<Integer,String> uploadPartsSignUrl(String path, String uploadId, List<Integer> partNums) throws AuthenticationException;

    /**
     * Abort multi upload.
     *
     * @param path     the path
     * @param uploadId the upload id
     * @throws AuthenticationException the authentication exception
     */
    void abortMultiUpload(String path, String uploadId) throws AuthenticationException;

    /**
     * Gets object metadata.
     *
     * @param keyName the key name
     * @return the object metadata
     * @throws AmazonS3Exception       the amazon s 3 exception
     * @throws AuthenticationException the authentication exception
     */
    ObjectMetadata getObjectMetadata(String keyName) throws AmazonS3Exception, AuthenticationException;

    /**
     * List objects list.
     *
     * @param prefix the prefix
     * @return the list
     * @throws AuthenticationException the authentication exception
     */
    List<Map<String, Object>> listObjects(String prefix)throws AuthenticationException;

    /**
     * Delete objects.
     *
     * @param keyNames the key names
     * @throws AuthenticationException the authentication exception
     */
    void deleteObjects(List<String> keyNames) throws AuthenticationException;

    /**
     * Delete object int.
     *
     * @param prefix the prefix
     * @return the int
     * @throws AuthenticationException the authentication exception
     */
    int deleteObject(String prefix) throws AuthenticationException;
}

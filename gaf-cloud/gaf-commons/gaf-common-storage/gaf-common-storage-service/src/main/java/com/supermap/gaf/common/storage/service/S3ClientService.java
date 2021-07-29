/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.common.storage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.supermap.gaf.common.storage.entity.ObjectInfo;
import com.supermap.gaf.common.storage.entity.VolumePathReturn;
import com.supermap.gaf.common.storage.web.SelectModeI;
import org.apache.commons.lang3.NotImplementedException;

import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * The interface S 3 client service.
 *
 * @author heykb
 * @date:2021/3/25
 */
public interface S3ClientService {
    default int deleteObjects(List<String> prefixs){
        throw new NotImplementedException("deleteObject()");
    }

    /**
     * S 3 client amazon s 3.
     *
     * @param configName the config name
     * @return the amazon s 3
     */
    AmazonS3 s3Client(String configName, SelectModeI selectMode);

    ;

    /**
     * Gets url.
     *
     * @param configName the config name
     * @param keyName    the key name
     * @return the url
     */
    URL getUrl(String configName, String keyName, SelectModeI selectMode);


    default URL getUrl(String keyName){
        throw new NotImplementedException("deleteObject()");
    }

    /**
     * Put object.
     *
     * @param configName the config name
     * @param keyName    the key name
     * @param file       the file
     */
    void putObject(String configName, String keyName, File file, SelectModeI selectMode);


    /**
     * Gets upload sign url.
     *
     * @param configName the config name
     * @param keyName    the key name
     * @param contentMd5 the content md 5
     * @return the upload sign url
     */
    String getUploadSignUrl(String configName, String keyName,String contentMd5, SelectModeI selectMode);


    /**
     * 分享文件.
     *
     * @param configName the config name
     * @param keyName    the key name
     * @param minute     the minute
     * @return the string
     */
    String share(String configName, String keyName,long minute,String secret, SelectModeI selectMode);

    /**
     * Gets download sign url.
     *
     * @param configName the config name
     * @param keyName    the key name
     * @return the download sign url
     */
    String getDownloadSignUrl(String configName, String keyName, boolean isPreview, SelectModeI selectMode);


    /**
     * Create multi upload string.
     *
     * @param configName         the config name
     * @param keyName            the key name
     * @param userObjectMetadata the user object metadata
     * @return the string
     */
    String createMultiUpload(String configName, String keyName,Map<String,String> userObjectMetadata, SelectModeI selectMode);


    /**
     * Multi upload sign url map.
     *
     * @param configName the config name
     * @param keyName    the key name
     * @param uploadId   the upload id
     * @param maxPartNum the max part num
     * @return the map
     */
    Map<Integer,String> multiUploadSignUrl(String configName, String keyName, String uploadId, int maxPartNum, SelectModeI selectMode);


    /**
     * Complete multi upload.
     *
     * @param configName the config name
     * @param keyName    the key name
     * @param uploadId   the upload id
     */
    @Deprecated
    String completeMultiUpload(String configName, String keyName, String uploadId, SelectModeI selectMode);


    /**
     * Complete multi upload.
     *
     * @param configName the config name
     * @param keyName    the key name
     * @param uploadId   the upload id
     * @param partETags  the part e tags
     */
    String completeMultiUpload(String configName, String keyName, String uploadId, List<PartETag> partETags, SelectModeI selectMode);


    /**
     * Upload parts sign url map.
     *
     * @param configName the config name
     * @param path       the path
     * @param uploadId   the upload id
     * @param partNums   the part nums
     * @return the map
     */
    Map<Integer,String> uploadPartsSignUrl(String configName, String path, String uploadId, List<Integer> partNums, SelectModeI selectMode);


    /**
     * Abort multi upload.
     *
     * @param configName the config name
     * @param path       the path
     * @param uploadId   the upload id
     */
    void abortMultiUpload(String configName, String path, String uploadId, SelectModeI selectMode);


    /**
     * Gets object metadata.
     *
     * @param configName the config name
     * @param keyName    the key name
     * @return the object metadata
     */
    ObjectMetadata getObjectMetadata(String configName, String keyName, SelectModeI selectMode);

    /**
     * List objects list.
     *
     * @param configName the config name
     * @param prefix     the prefix
     * @return the list
     */
    List<ObjectInfo> listObjects(String configName, String prefix, boolean recursion, SelectModeI selectMode);

//    /**
//     * Delete objects.
//     *
//     * @param keyNames the key names
//     */
//    void deleteObjects(List<String> keyNames, SelectModeI selectMode);
//
//    /**
//     * Delete objects.
//     *
//     * @param configName the config name
//     * @param keyNames   the key names
//     */
//    void deleteObjects(String configName, List<String> keyNames, SelectModeI selectMode);


    /**
     * Delete object int.
     *
     * @param configName the config name
     * @param prefix     the prefix
     * @return the int
     */
    int deleteObject(String configName, String prefix, SelectModeI selectMode);

    void createEmptyDir(String configName, String path, SelectModeI selectMode);

    BigInteger totalSize(String configName, String prefix, SelectModeI selectMode);

    VolumePathReturn getVolumePath(String configName, String path, boolean returnUrl, SelectModeI selectModeI);
}

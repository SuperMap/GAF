/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.storage.resource;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.storage.service.S3ClientService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.security.sasl.AuthenticationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * @author:yj
 * @date:2021/3/25
 * The type Minio service resource.
 */
@Component
public class FileStorageResource {

    @Autowired
    protected S3ClientService s3ClientService;

    /**
     * 获取上传文件签名url
     *
     * @param path the path
     * @param contentMd5  上传文件的base64 md5,如果设置上传时将会校验文件，并且设置x-amz-meta-base64md5元数据。
     *                    如果要实现秒传功能请设置此参数，并且在上传之前检查x-amz-meta-base64md5
     * @return the string
     */
    @PUT
    @Path("/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",value = "path",paramType = "path",dataType = "string",required = true ),
            @ApiImplicitParam(name = "contentMd5",value = "contentMd5",paramType = "header",dataType = "string",required = false )})
    public MessageResult<String> uploadSignUrl(@PathParam("path") String path,@HeaderParam("contentMd5") String contentMd5) throws AuthenticationException {
        return MessageResult.successe(String.class).data(s3ClientService.getUploadSignUrl(path,contentMd5)).build();
    }

    /**
     * 获取下载文件签名url
     *
     * @param path the path
     * @return the string
     */
    @GET
    @Path("/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("获取下载文件签名url")
    @ApiImplicitParam(name = "path",value = "文件路径",paramType = "path",dataType = "string",required = true )
    public MessageResult<String> downloadSignUrl(@PathParam("path") String path) throws AuthenticationException {
        return MessageResult.successe(String.class).data(s3ClientService.getDownloadSignUrl(path)).build();
    }
    /**
     * 获取文件信息
     *
     * @param path the path
     * @return the string
     */
    @GET
    @Path("/metadata/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("获取文件元数据")
    @ApiImplicitParam(name = "path",value = "文件路径",paramType = "path",dataType = "string",required = true )
    public MessageResult<Object> getObjectMetadata(@PathParam("path") String path) throws Exception{
        try{
            ObjectMetadata objectMetadata = s3ClientService.getObjectMetadata(path);
            return MessageResult.successe(Object.class).data(objectMetadata).build();
        }catch (AmazonS3Exception amazonS3Exception){
            return MessageResult.successe(Object.class).status(amazonS3Exception.getStatusCode())
                    .message(amazonS3Exception.getErrorCode()).build();
        } catch (AuthenticationException e) {
            throw e;
        }
    }

    /**
     * 创建分片上传
     *
     * @param path       the path
     * @return the string
     */
    @POST
    @Path("/multi-upload/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("创建分片上传")
    @ApiImplicitParam(name = "path",value = "文件路径",paramType = "path",dataType = "string",required = true )
    public MessageResult<String> createMultiUpload(@PathParam("path") String path,Map<String,String> userObjectMetadata) throws AuthenticationException {
        return MessageResult.successe(String.class).data(s3ClientService.createMultiUpload(path,userObjectMetadata)).build();
    }

    /**
     * 获取剩余分片上传url列表
     *
     * @param uploadId   the upload id
     * @param maxPartNum the max part num
     * @return the list
     */
    @PUT
    @Path("/multi-upload/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("获取剩余分片上传url列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",value = "文件路径",paramType = "path",dataType = "string",required = true ),
            @ApiImplicitParam(name = "uploadId",value = "uploadId",paramType = "query",dataType = "string",required = true ),
            @ApiImplicitParam(name = "maxPartNum",value = "最大切片数",paramType = "query",dataType = "string",required = true )})
    public MessageResult<Map> multiUploadSignUrl(@PathParam("path") String path, @QueryParam("uploadId") String uploadId, @QueryParam("maxPartNum") int maxPartNum) throws AuthenticationException {
        return MessageResult.successe(Map.class).data(s3ClientService.multiUploadSignUrl(path,uploadId,maxPartNum)).build();
    }

    /**
     * 完成分片上传，合并分片
     *
     * @param uploadId the upload id
     */
    @POST
    @Path("/complete-multi-upload/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("完成分片上传，合并分片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",value = "文件路径",paramType = "path",dataType = "string",required = true ),
            @ApiImplicitParam(name = "uploadId",value = "uploadId",paramType = "query",dataType = "string",required = true )})
    public MessageResult<Void> completeMultiUpload(@PathParam("path") String path, @QueryParam("uploadId") String uploadId) throws AuthenticationException {
        s3ClientService.completeMultiUpload(path,uploadId);
        return MessageResult.successe(Void.class).build();
    }
    /**
     * 中止分片上传
     *
     * @param uploadId the upload id
     */
    @POST
    @Path("/abort-multi-upload/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("中止分片上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",value = "文件路径",paramType = "path",dataType = "string",required = true ),
            @ApiImplicitParam(name = "uploadId",value = "uploadId",paramType = "query",dataType = "string",required = true )})
    public MessageResult<Void> abortMultiUpload(@PathParam("path") String path, @QueryParam("uploadId") String uploadId) throws AuthenticationException {
        s3ClientService.abortMultiUpload(path,uploadId);
        return MessageResult.successe(Void.class).build();
    }

    /**
     * 获取指定分片上传签名url
     *
     * @param path the path
     * @return the string
     */
    @PUT
    @Path("/parts-upload/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResult<Map> uploadPartsSignUrl(@PathParam("path") String path, @QueryParam("uploadId") String uploadId, List<Integer> partNums) throws AuthenticationException {
        return MessageResult.successe(Map.class).data(s3ClientService.uploadPartsSignUrl(path,uploadId,partNums)).build();
    }

    /**
     * 查询子目录与文件
     * @return the string
     */
    @GET
    @Path("/list-objects")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResult<List<Map<String, Object>>> listObjects(@DefaultValue("")@QueryParam("prefix") String prefix) throws AuthenticationException {
        return MessageResult.data(s3ClientService.listObjects(prefix)).build();
    }

    /**
     * 批量删除对象
     * @param keyNames
     * @return
     * @throws AuthenticationException
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResult deleteObjects(List<String> keyNames)throws AuthenticationException {
        s3ClientService.deleteObjects(keyNames);
        return MessageResult.successe(Void.class).build();
    }

    /**
     * 删除对象
     * @param prefix
     * @return
     * @throws AuthenticationException
     */
    @DELETE
    @Path("/{prefix:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResult<Integer> deleteObject(@PathParam("prefix") String prefix)throws AuthenticationException {
        return MessageResult.data(s3ClientService.deleteObject(prefix)).build();
    }

}

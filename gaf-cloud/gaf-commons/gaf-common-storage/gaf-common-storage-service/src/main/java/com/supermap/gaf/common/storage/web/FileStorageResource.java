/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.common.storage.web;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.supermap.gaf.common.storage.entity.ObjectInfo;
import com.supermap.gaf.common.storage.entity.ObjectPartETag;
import com.supermap.gaf.common.storage.entity.VolumePathReturn;
import com.supermap.gaf.common.storage.service.S3ClientService;
import com.supermap.gaf.commontypes.MessageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:yj
 * @date:2021/3/25 The type Minio service resource.
 */
@Api("文件操作接口")
public class FileStorageResource {

    protected S3ClientService s3ClientService;

    protected SelectModeI selectModeI;

    public FileStorageResource(S3ClientService s3ClientService, SelectModeI selectModeI) {
        this.s3ClientService = s3ClientService;
        this.selectModeI = selectModeI;
    }

    /**
     * 获取上传文件签名url
     *
     * @param path       the path
     * @param contentMd5 上传文件的base64 md5,如果设置上传时将会校验文件，并且设置x-amz-meta-base64md5元数据。
     *                   如果要实现秒传功能请设置此参数，并且在上传之前检查x-amz-meta-base64md5
     * @return the string
     */
    @PUT
    @Path("/upload/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取上传文件签名url", notes = "根据id查询部门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "文件路径", paramType = "path", dataType = "string", required = true),
            @ApiImplicitParam(name = "contentMd5", value = "上传文件的base64 md5,如果设置上传时将会校验文件，并且设置x-amz-meta-base64md5元数据。如果要实现秒传功能请设置此参数，并且在上传之前检查x-amz-meta-base64md5", paramType = "header", dataType = "string", required = false)})
    public MessageResult<String> uploadSignUrl(@PathParam("path") String path, @HeaderParam("contentMd5") String contentMd5,
                                               @PathParam("configName") String configName) {
        return MessageResult.successe(String.class).data(s3ClientService.getUploadSignUrl(configName, path, contentMd5, selectModeI)).build();
    }

    /**
     * 创建空目录
     *
     * @param path the path
     * @return the string
     */
    @POST
    @Path("/create-empty-dir/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "获取上传文件签名url", notes = "根据id查询部门")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "path", value = "文件路径", paramType = "path", dataType = "string", required = true))
    public MessageResult<Void> createEmptyDir(@PathParam("path") String path, @PathParam("configName") String configName) {
        s3ClientService.createEmptyDir(configName, path, selectModeI);
        return MessageResult.successe(Void.class).build();
    }


    /**
     * 获取下载文件签名url
     *
     * @param path the path
     * @return the string
     */
    @GET
    @Path("/download/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("获取下载文件签名url")
    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "path", dataType = "string", required = true)
    public MessageResult<String> downloadSignUrl(@PathParam("path") String path, @PathParam("configName") String configName,
                                                 @DefaultValue("false") @QueryParam("isPreview") boolean isPreview) {
        return MessageResult.successe(String.class).data(s3ClientService.getDownloadSignUrl(configName, path, isPreview, selectModeI)).build();
    }

    /**
     * 分享文件返回链接
     *
     * @param path
     * @param configName
     * @param minute
     * @return
     */
    @GET
    @Path("/share/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("分享文件返回链接")
    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "path", dataType = "string", required = true)
    public MessageResult<Map<String, Object>> share(@PathParam("path") String path, @PathParam("configName") String configName,
                                                    @DefaultValue("5") @QueryParam("expiration") long minute,
                                                    @QueryParam("encryption") boolean encryption) throws Exception {

        ObjectMetadata objectMetadata = s3ClientService.getObjectMetadata(configName, path, selectModeI);
        String secret = null;
        Map<String, Object> re = new HashMap<>();
        if (encryption) {
            secret = RandomStringUtils.randomAlphanumeric(4);
            re.put("secret", secret);
        }
        String url = s3ClientService.share(configName, path, minute, secret, selectModeI);

        re.put("size", objectMetadata.getContentLength());
        re.put("contentType", objectMetadata.getContentType());
        if (!StringUtils.isEmpty(secret)) {
            if (url.endsWith("secret=" + secret)) {
                url = url.replace("&secret=" + secret, "");
            } else {
                url = url.replace("secret=" + secret + "&", "");
            }
        }
        re.put("download", url);
        return MessageResult.data(re).build();
    }

    /**
     * 获取文件元数据
     *
     * @param path the path
     * @return the string
     */
    @GET
    @Path("/metadata/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("获取文件元数据")
    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "path", dataType = "string", required = true)
    public MessageResult<ObjectMetadata> getObjectMetadata(@PathParam("path") String path, @PathParam("configName") String configName) {
        ObjectMetadata objectMetadata = s3ClientService.getObjectMetadata(configName, path, selectModeI);
        return MessageResult.successe(ObjectMetadata.class).data(objectMetadata).build();
    }

    /**
     * 创建分片上传
     *
     * @param path the path
     * @return the string
     */
    @POST
    @Path("/multi-upload/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("创建分片上传")
    @ApiImplicitParam(name = "path", value = "文件路径", paramType = "path", dataType = "string", required = true)
    public MessageResult<String> createMultiUpload(@PathParam("path") String path, Map<String, String> userObjectMetadata, @PathParam("configName") String configName) {
        return MessageResult.successe(String.class).data(s3ClientService.createMultiUpload(configName, path, userObjectMetadata, selectModeI)).build();
    }

    /**
     * 完成分片上传，合并分片（要求客户端传递已上传的分片etag，客户端需要本地保存上传的分片etag）
     *
     * @param uploadId the upload id
     */
    @POST
    @Path("/complete-multi-upload/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("完成分片上传，合并分片（要求客户端传递已上传的分片etag，客户端需要本地保存上传的分片etag）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "文件路径", paramType = "path", dataType = "string", required = true),
            @ApiImplicitParam(name = "uploadId", value = "uploadId", paramType = "query", dataType = "string", required = true)})
    public MessageResult<String> completeMultiUpload(@PathParam("path") String path, @QueryParam("uploadId") String uploadId, List<ObjectPartETag> partETags, @PathParam("configName") String configName) {
        List<PartETag> list = partETags.stream().map(item -> new PartETag(item.getPartNumber(), item.geteTag())).collect(Collectors.toList());
        String publicUrl = s3ClientService.completeMultiUpload(configName, path, uploadId, list, selectModeI);
        return MessageResult.data(publicUrl).build();
    }

    /**
     * 中止分片上传
     *
     * @param uploadId the upload id
     */
    @DELETE
    @Path("/multi-upload/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("中止分片上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "文件路径", paramType = "path", dataType = "string", required = true),
            @ApiImplicitParam(name = "uploadId", value = "uploadId", paramType = "query", dataType = "string", required = true)})
    public MessageResult<Void> abortMultiUpload(@PathParam("path") String path, @QueryParam("uploadId") String uploadId, @PathParam("configName") String configName) {
        s3ClientService.abortMultiUpload(configName, path, uploadId, selectModeI);
        return MessageResult.successe(Void.class).build();
    }

    /**
     * 获取指定分片上传签名url
     *
     * @param path the path
     * @return the string
     */
    @PUT
    @Path("/multi-upload/{path:.*}")
    @ApiOperation("获取指定分片上传签名url")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResult<Map> uploadPartsSignUrl(@PathParam("path") String path, @QueryParam("uploadId") String uploadId, List<Integer> partNums, @PathParam("configName") String configName) {
        return MessageResult.successe(Map.class).data(s3ClientService.uploadPartsSignUrl(configName, path, uploadId, partNums, selectModeI)).build();
    }

    /**
     * 查询子目录与文件
     *
     * @return the string
     */
    @GET
    @Path("/list-objects/{prefix:.*}")
    @ApiOperation("查询子目录与子文件")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResult<List<ObjectInfo>> listObjects(@PathParam("prefix") String prefix, @PathParam("configName") String configName,
                                                       @DefaultValue("false") @QueryParam("recursion") boolean recursion) {
        return MessageResult.data(s3ClientService.listObjects(configName, prefix, recursion, selectModeI)).build();
    }

    /**
     * 查询当前配置已使用的总大小
     *
     * @return the string
     */
    @GET
    @Path("/total-size/{prefix:.*}")
    @ApiOperation("查询目录总大小")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResult<String> totalSize(@PathParam("prefix") String prefix, @PathParam("configName") String configName) {
        BigInteger re = s3ClientService.totalSize(configName, prefix, selectModeI);
        return MessageResult.data(re.toString(10)).build();
    }

    /**
     * 删除对象
     *
     * @param prefix
     * @return
     */
    @DELETE
    @Path("/{prefix:.*}")
    @ApiOperation("删除目录或文件")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResult<Integer> deleteObject(@PathParam("prefix") String prefix, @PathParam("configName") String configName) {
        return MessageResult.data(s3ClientService.deleteObject(configName, prefix, selectModeI)).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "获取挂载路径", notes = "获取挂载路径")
    @Path("/volume-path/{path:.*}")
    public MessageResult<VolumePathReturn> getVolumePath(@PathParam("path") String path, @PathParam("configName") String configName, @DefaultValue("false") @QueryParam("returnUrl") boolean returnUrl) {
        return MessageResult.successe(VolumePathReturn.class).data(s3ClientService.getVolumePath(configName, path, returnUrl, selectModeI)).status(200).message("查询成功").build();
    }

}

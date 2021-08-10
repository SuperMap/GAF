/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.rest.jersey;

import com.supermap.gaf.rest.utils.HttpUtil;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 基于 JAX-RS 标准的静态文件资源实现类。
 * 用于将静态文件发布为 REST 资源。（处理Springboot jersey设置默认路径后会屏蔽Spring Web静态资源的问题）
 * </p>
 * <p>
 * 文件可以是存放在 Jar 包中，也可以是存放在 文件系统中。如果是在 Jar 包中，则放在“staticFiles”目录下，
 * 也可以在static目录下，即兼容springboot的默认设置
 * </p>
 *
 * @author ${Author}
 * @version ${Version}
 * @date:2021/3/25
 */
//@Path("/static/{path:.*}")
public class JaxrsStaticResource {


    private static final String STATIC_FILES_PATH = "static";
//    private static final String responseLastModifiedStr = "Last-Modified";
//    private static final String requestIfModifiedSinceStr = "If-Modified-Since";
//    private static final String requestIfUnmodifiedSinceStr = "If-Unmodified-Since";
//    private static final String responseETagStr = "ETag";
//    private static final String requestIfNoneMatchStr = "If-None-Match";
//    private static final String requestIfMatchStr = "If-Match";
//    private static IMessageConveyor messageConveyor = new MessageConveyor(Locale.getDefault());
//    private static LocLoggerFactory llFactoryzhCN = new LocLoggerFactory(messageConveyor);
//    private static LocLogger locLogger = llFactoryzhCN.getLocLogger(JaxrsStaticResource.class);
//    private ResourceManager message = new ResourceManager("resource/rest");

    /**
     * <p>
     * 返回资源的内容。
     * </p>
     *
     * @param path    静态文件的路径。
     * @param requset HTTP 请求信息。
     * @return 资源内容的字节流。
     */
    @GET
    @Produces({"text/html", "text/javascript", "application/javascript", "text/css", "image/png", "image/gif", "image/bmp", "image/jpeg", "application/x-woff", "application/vnd.ms-fontobject", "image/svg+xml", "application/x-font-ttf", "application/octet-stream"})
    public Response get(@PathParam("path") String path, @Context HttpServletRequest requset, @Context HttpServletResponse response) {
        String relativelyPath = STATIC_FILES_PATH + "/" + path;
//缓存逻辑需要重新调整
//        if (useHeaderCache(requset, response, relativelyPath)) {
//             ResponseBuilder rBuilder = Response.ok().header("Content-Type", HttpUtil.getAcceptMediaType(requset)).status(HttpStatus.NOT_MODIFIED.value());
//             rBuilder.build();
//        }

//        response.setContentType(HttpUtil.getAcceptMediaType(requset).toString());
//        response.addHeader("Content-Type", HttpUtil.getAcceptMediaType(requset).toString());
        InputStream ins = getFileInputStream(relativelyPath);
        if (ins == null) {
            throw new NotFoundException();
        }
        return Response.ok(ins, HttpUtil.getAcceptMediaType(requset)).build();
    }

    @GET
    @Produces({"text/html", "text/javascript", "application/javascript", "text/css", "image/png", "image/gif", "image/bmp", "image/jpeg", "application/x-woff", "application/vnd.ms-fontobject", "image/svg+xml", "application/x-font-ttf", "application/octet-stream"})
    public Response getSwagger(@PathParam("path") String path, @Context HttpServletRequest requset, @Context HttpServletResponse response) {
        String relativelyPath = STATIC_FILES_PATH + "/swagger/" + path;
        InputStream ins = getFileInputStream(relativelyPath, JaxrsStaticResource.class.getClassLoader());
        if (ins == null) {
            throw new NotFoundException();
        }
        Object re = ins;
        try {
            if (path.equals("index.html")) {
                re = StreamUtils.copyToString(ins, StandardCharsets.UTF_8);
                re = ((String) re).replaceAll("swaggerUrl", requset.getServletPath() + "/swagger.json");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok(re, HttpUtil.getAcceptMediaType(requset)).build();
    }

    private InputStream getFileInputStream(String relativelyPath) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return getFileInputStream(relativelyPath, loader);
    }

    private InputStream getFileInputStream(String relativelyPath, ClassLoader loader) {
        InputStream inputStream = loader.getResourceAsStream(relativelyPath);
        if (inputStream == null) {
            File file = new File(STATIC_FILES_PATH);
            File resourceFile = new File(file, relativelyPath);
            if (resourceFile.exists()) {
                try {
                    inputStream = new FileInputStream(resourceFile);
                } catch (FileNotFoundException e) {
//                    locLogger.warn(message.getMessage("newFileInputStream.failed", resourceFile.getName()));

                }
            }
        }
        return inputStream;
    }

    private File getFile(String relativelyPath) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL fileUrl = loader.getResource(relativelyPath);
        File resourceFile = null;
        if (fileUrl != null) {
            resourceFile = new File(fileUrl.getFile());
            if (resourceFile.exists()) {
                return resourceFile;
            }
        }
        File file = new File(STATIC_FILES_PATH);
        resourceFile = new File(file, relativelyPath);
        if (resourceFile.exists()) {
            return resourceFile;
        }
        return null;
    }

//    /**
//     *
//     * 判断是否使用缓存
//     *
//     * @param requset
//     * @param response
//     * @param relativelyPath
//     * @return
//     */
//    private boolean useHeaderCache(HttpServletRequest requset, HttpServletResponse response, String relativelyPath) {
//        try {
//            if (relativelyPath == null) {
//                return false;
//            }
//            /**
//             * 可以取到sourceFile的话则添加Last-Modified字段到response,
//             * 取不到的话只设置ETag字段
//             */
//            byte[] fileBytes = null;
//            Date fileLastModified = null;
//            File resourceFile = getFile(relativelyPath);
//            if (resourceFile != null) {
//                fileBytes = FileUtils.readFileToByteArray(resourceFile);
//                if (resourceFile.lastModified() > 0) {
//                    fileLastModified = new Date(resourceFile.lastModified());
//                    response.setHeader(responseLastModifiedStr, String.valueOf(resourceFile.lastModified()));
//                }
//            } else {
//                // 取到的file和流都为空的话就不使用head cache
//                InputStream inputStream = getFileInputStream(relativelyPath);
//                if (inputStream == null) {
//                    return false;
//                }
//                fileBytes = new byte[inputStream.available()];
//                inputStream.read(fileBytes);
//            }
//
//            String fileTag = Tool.computeObjectDigest((java.io.Serializable) fileBytes);
//            Tag eTag = null;
//            if (!StringUtils.isEmpty(fileTag)) {
//                eTag = new Tag(fileTag);
//                response.setHeader(responseETagStr, fileTag);
//            }
//
//            Conditions conditions = new Conditions();
//
//            String returnIfNoneMatchStr = requset.getHeader(requestIfNoneMatchStr);
//            String returnIfMatchStr = requset.getHeader(requestIfMatchStr);
//            String returnIfModifiedSinceStr = requset.getHeader(requestIfModifiedSinceStr);
//            String returnIfUnmodifiedSinceStr = requset.getHeader(requestIfUnmodifiedSinceStr);
//
//            if (!StringUtils.isEmpty(returnIfNoneMatchStr)) {
//                Tag noneMatchTag = new Tag(returnIfNoneMatchStr);
//                List<Tag> tags = new ArrayList<Tag>();
//                tags.add(noneMatchTag);
//                conditions.setNoneMatch(tags);
//            }
//            if (!StringUtils.isEmpty(returnIfMatchStr)) {
//                Tag noneMatchTag = new Tag(returnIfMatchStr);
//                List<Tag> tags = new ArrayList<Tag>();
//                tags.add(noneMatchTag);
//                conditions.setMatch(tags);
//            }
//
//            if (!StringUtils.isEmpty(returnIfModifiedSinceStr)) {
//                long ifModifiedSinceStamp = NumberUtils.toLong(returnIfModifiedSinceStr);
//                Date modifiedSinceDate = null;
//                if (ifModifiedSinceStamp > 0) {
//                    modifiedSinceDate = new Date(ifModifiedSinceStamp);
//                } else {
//                    modifiedSinceDate = new Date(returnIfModifiedSinceStr);
//                }
//                conditions.setModifiedSince(modifiedSinceDate);
//            }
//
//            if (!StringUtils.isEmpty(returnIfUnmodifiedSinceStr)) {
//                long ifUnmodifiedSinceStamp = NumberUtils.toLong(returnIfUnmodifiedSinceStr);
//                Date unmodifiedSinceDate = null;
//                if (ifUnmodifiedSinceStamp > 0) {
//                    unmodifiedSinceDate = new Date(ifUnmodifiedSinceStamp);
//                } else {
//                    unmodifiedSinceDate = new Date(returnIfModifiedSinceStr);
//                }
//                conditions.setUnmodifiedSince(unmodifiedSinceDate);
//            }
//
//            Status status = conditions.getStatus(Method.GET, true, eTag, fileLastModified);
//            return status != null && status.equals(Status.REDIRECTION_NOT_MODIFIED);
////        } catch (IOException e) {
//            locLogger.debug(e.getMessage(), e);
//            return false;
//        }
//    }
}

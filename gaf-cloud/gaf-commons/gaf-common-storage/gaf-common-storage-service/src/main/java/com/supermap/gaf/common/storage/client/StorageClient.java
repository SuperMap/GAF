package com.supermap.gaf.common.storage.client;


import com.amazonaws.services.s3.model.ObjectMetadata;
import com.supermap.gaf.common.storage.entity.PresignUploadRequest;
import com.supermap.gaf.common.storage.entity.VolumePathReturn;
import com.supermap.gaf.common.storage.utils.CommonStorageUtils;
import com.supermap.gaf.commontypes.MessageResult;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.OutputStream;

@Setter
public class StorageClient {
    private final static Logger log = LoggerFactory.getLogger(StorageClient.class);
    private String preUrl;
    private RestTemplate restTemplate;
    private String tenantIdHeaderName = "TENANT_ID";
    private String permissionHeaderName = "PERMISSION";
    private String configName;
    private String superOwer = "admin";
    public static final String UPLOAD_SIGN_URL = "%s/%s/upload/%s";
    public static final String DOWNLOAD_SIGN_URL = "%s/%s/download/%s";
    public static final String MEATADATA_URL = "%s/%s/metadata/%s";
    public static final String VOLUME_PATH_URL = "%s/%s/volume-path/%s?returnUrl=%s";
    public static final String DELETE_OBJECT_URL = "%s/%s/%s";

    public StorageClient(String preUrl, String configName) {
        this.preUrl = normalizePreUrl(preUrl);
        this.configName = configName;
        this.restTemplate = new RestTemplate();
    }

    public StorageClient(String preUrl, String configName, RestTemplate restTemplate) {
        this.preUrl = normalizePreUrl(preUrl);
        this.configName = configName;
        this.restTemplate = restTemplate;
    }

    public void setPreUrl(String preUrl) {
        this.preUrl = normalizePreUrl(preUrl);
    }

    public void uploadFlie(String path, @Nullable String tenantId, InputStream in) {
        String signUrl = getUploadSignUrl(path, tenantId, null);
        CommonStorageUtils.uploadByPreSignedUrl(new PresignUploadRequest(signUrl), in);
    }

    public void quickUploadFlie(String path, @Nullable String tenantId, InputStream in) throws Exception {
        String md5 = CommonStorageUtils.getBase64Md5(in);
        boolean needUpload = true;
        try {
            ObjectMetadata objectMetadata = getObjectMetadata(path, tenantId);
            needUpload = !md5.equals(objectMetadata.getUserMetadata().get("base64md5"));
        } catch (Exception e) {
            log.warn("getObjectMetadata error,{}", e.getMessage());
        }
        if (needUpload) {
            String signUrl = getUploadSignUrl(path, tenantId, md5);
            CommonStorageUtils.uploadByPreSignedUrl(new PresignUploadRequest(signUrl, md5), in);
        }
    }

    public void downloadFile(String path, @Nullable String tenantId, OutputStream outputStream) {
        String downloadUrl = getDownloadSignUrl(path, tenantId);
        CommonStorageUtils.downloadByPreSignedUrl(downloadUrl, outputStream);
    }

    public VolumePathReturn getVolumePath(String path, @Nullable String tenantId, boolean returnUrl) {
        HttpHeaders headers = new HttpHeaders();
        if(tenantId!=null){
            headers.set(tenantIdHeaderName, tenantId);
        }
        headers.set(permissionHeaderName, superOwer);
        HttpEntity httpEntity = new HttpEntity(headers);
        ParameterizedTypeReference<MessageResult<VolumePathReturn>> typeRef = new ParameterizedTypeReference<MessageResult<VolumePathReturn>>() {};
        MessageResult<VolumePathReturn> body = restTemplate.exchange(String.format(VOLUME_PATH_URL, preUrl, configName, normalizePath(path),returnUrl), HttpMethod.GET,
                httpEntity,typeRef).getBody();
        if (!body.isSuccessed()) {
            throw new RestClientResponseException(body.getMessage(), body.getStatus(), body.getMessage(), null, null, null);
        }
        return body.getData();
    }

    public Integer delete(String path, @Nullable String tenantId) {
        HttpHeaders headers = new HttpHeaders();
        if(tenantId!=null){
            headers.set(tenantIdHeaderName, tenantId);
        }
        headers.set(permissionHeaderName, superOwer);
        HttpEntity httpEntity = new HttpEntity(headers);
        MessageResult<Integer> body = restTemplate.exchange(String.format(DELETE_OBJECT_URL, preUrl, configName, normalizePath(path)), HttpMethod.DELETE,
                httpEntity, MessageResult.class).getBody();
        if (!body.isSuccessed()) {
            throw new RestClientResponseException(body.getMessage(), body.getStatus(), body.getMessage(), null, null, null);
        }
        return body.getData();
    }

    private String getDownloadSignUrl(String path, @Nullable String tenantId) {
        HttpHeaders headers = new HttpHeaders();
        if(tenantId!=null){
            headers.set(tenantIdHeaderName, tenantId);
        }
        headers.set(permissionHeaderName, superOwer);
        HttpEntity httpEntity = new HttpEntity(headers);
        MessageResult<String> body = restTemplate.exchange(String.format(DOWNLOAD_SIGN_URL, preUrl, configName, normalizePath(path)), HttpMethod.GET,
                httpEntity, MessageResult.class).getBody();
        if (!body.isSuccessed()) {
            throw new RestClientResponseException(body.getMessage(), body.getStatus(), body.getMessage(), null, null, null);
        }
        return body.getData();
    }

    private String getUploadSignUrl(String path, @Nullable String tenantId, @Nullable String md5) {
        HttpHeaders headers = new HttpHeaders();
        if(tenantId!=null){
            headers.set(tenantIdHeaderName, tenantId);
        }
        if (md5 != null) {
            headers.set("contentMd5", md5);
        }
        headers.set(permissionHeaderName, superOwer);
        HttpEntity httpEntity = new HttpEntity(headers);
        MessageResult<String> body = restTemplate.exchange(String.format(UPLOAD_SIGN_URL, preUrl, configName, normalizePath(path)), HttpMethod.GET,
                httpEntity, MessageResult.class).getBody();
        if (!body.isSuccessed()) {
            throw new RestClientResponseException(body.getMessage(), body.getStatus(), body.getMessage(), null, null, null);
        }
        return body.getData();
    }

    private ObjectMetadata getObjectMetadata(String path, @Nullable String tenantId) {
        HttpHeaders headers = new HttpHeaders();
        if(tenantId!=null){
            headers.set(tenantIdHeaderName, tenantId);
        }
        headers.set(permissionHeaderName, superOwer);
        HttpEntity httpEntity = new HttpEntity(headers);
        MessageResult<ObjectMetadata> body = restTemplate.exchange(String.format(MEATADATA_URL, preUrl, configName, normalizePath(path)), HttpMethod.GET,
                httpEntity, MessageResult.class).getBody();
        if (!body.isSuccessed()) {
            throw new RestClientResponseException(body.getMessage(), body.getStatus(), body.getMessage(), null, null, null);
        }
        return body.getData();
    }

    String normalizePath(String path){
        return path.replaceFirst("^/*","");
    }
    String normalizePreUrl(String preUrl){
        return preUrl.endsWith("/")? preUrl.replaceAll("/+$","/"):preUrl+"/";
    }

    public static void main(String[] args) {
        StorageClient client = new StorageClient("http://localhost:8888/storage/api/platform/","yctest123");
//        VolumePathReturn path = client.getVolumePath("test/",null,true);
//        System.out.println(path);
        String path = client.normalizePreUrl("////fdfds/fdfdsf/ss/////");
        System.out.println(path);
    }
}

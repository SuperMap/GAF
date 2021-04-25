package com.supermap.desktop.develop.entity;

/**
 * @author heykb
 */
public class PresignUploadRequest {
    private String contentMd5;
    private String presignUrl;


    public PresignUploadRequest(String presignUrl) {
        this.presignUrl = presignUrl;
    }

    public PresignUploadRequest(String presignUrl, String contentMd5) {
        this.contentMd5 = contentMd5;
        this.presignUrl = presignUrl;
    }

    public String getContentMd5() {
        return contentMd5;
    }

    public void setContentMd5(String contentMd5) {
        this.contentMd5 = contentMd5;
    }

    public String getPresignUrl() {
        return presignUrl;
    }

    public void setPresignUrl(String presignUrl) {
        this.presignUrl = presignUrl;
    }
}

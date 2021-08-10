/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.common.storage.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author heykb
 * @date:2021/3/25
 */
@Data
public class PresignUploadRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String contentMd5;
    private String presignUrl;


    public PresignUploadRequest(String presignUrl) {
        this.presignUrl = presignUrl;
    }

    public PresignUploadRequest(String presignUrl, String contentMd5) {
        this.contentMd5 = contentMd5;
        this.presignUrl = presignUrl;
    }
}

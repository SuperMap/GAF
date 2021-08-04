/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.webgis.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author heykb
 * @date:2021/3/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("服务批量注册结果")
public class BatchRegistryServiceResult implements Serializable {
    public static final String RUNNING = "running";
    public static final String DONE = "done";
    public static final String ERROR = "error";
    private Integer succeeded = 0;
    private Integer failed = 0;
    private Integer existed = 0;
    private String status = RUNNING;

    public void success() {
        if (RUNNING.equals(status)) {
            succeeded += 1;
        }
    }

    public void fail() {
        if (RUNNING.equals(status)) {
            failed += 1;
        }
    }

    public void exist() {
        if (RUNNING.equals(status)) {
            existed += 1;
        }
    }

    public void done() {
        if (RUNNING.equals(status)) {
            status = DONE;
        }
    }

    public void error() {
        if (RUNNING.equals(status)) {
            status = ERROR;
        }
    }

}

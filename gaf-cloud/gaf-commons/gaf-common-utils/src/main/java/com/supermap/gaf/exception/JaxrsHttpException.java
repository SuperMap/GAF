/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.exception;

import javax.ws.rs.core.Response.Status;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class JaxrsHttpException extends RuntimeException {
    private static final long serialVersionUID = -3286032107078353210L;
    private transient Status errorStatus;

    public JaxrsHttpException() {
    }

    public JaxrsHttpException(String errorMsg) {
        super(errorMsg);
    }

    public JaxrsHttpException(Status errorStatus, String errorMsg) {
        super(errorMsg);
        this.errorStatus = errorStatus;
    }

    public JaxrsHttpException(Status errorStatus, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorStatus = errorStatus;
    }

    public JaxrsHttpException(int code, String errorMsg) {
        super(errorMsg);
        this.errorStatus = parseStatus(code);
    }

    public JaxrsHttpException(int code, String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorStatus = parseStatus(code);
    }

    public String getErrorMsg() {
        return this.getMessage();
    }

    public Status getErrorStatus() {
        return this.errorStatus;
    }

    public static Status parseStatus(int code) {
        for (Status e : Status.values()) {
            if (code == e.getStatusCode()) {
                return e;
            }
        }
        return null;
    }
}

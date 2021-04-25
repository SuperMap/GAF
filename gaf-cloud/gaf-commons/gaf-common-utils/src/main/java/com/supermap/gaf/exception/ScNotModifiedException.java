package com.supermap.gaf.exception;

public class ScNotModifiedException extends RuntimeException {
    private static final long serialVersionUID = -1649529031399765478L;
    private static final int HTTPSTATUS = 304;

    public int getHttpStatus() {
        return HTTPSTATUS;
    }
}
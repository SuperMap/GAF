package com.supermap.gaf.exception;

public class ShiroException extends RuntimeException {
    private static final long serialVersionUID = 313210707835335L;
    private static final int HTTPSTATUS = 401;

    public static int getHTTPSTATUS() {
        return HTTPSTATUS;
    }
}



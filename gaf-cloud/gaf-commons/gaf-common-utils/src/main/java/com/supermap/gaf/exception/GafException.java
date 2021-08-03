package com.supermap.gaf.exception;

/**
 * 自定义异常
 *
 * @author wxl
 */
public class GafException extends RuntimeException {
    public static final int CONFLICT = 409;

    private int code = 500;

    public GafException() {
        super();
    }

    public GafException(String message) {
        super(message);
    }

    public GafException(String message, int code) {
        super(message);
        this.code = code;
    }

    public GafException(Throwable cause) {
        super(cause);
    }

    public GafException(String message, Throwable cause) {
        super(message, cause);
    }

    public GafException(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }


    public GafException(int code) {
        this.code = code;
    }

    public static GafException message(String message) {
        return new GafException(message);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

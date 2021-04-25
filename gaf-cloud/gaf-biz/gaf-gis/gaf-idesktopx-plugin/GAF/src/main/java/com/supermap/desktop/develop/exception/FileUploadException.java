package com.supermap.desktop.develop.exception;

/**
 * @author heykb
 */
public class FileUploadException extends RuntimeException {
    public FileUploadException(Throwable cause) {
        super(cause);
    }
    public FileUploadException() {
    }
    public FileUploadException(String fileName) {
        super(fileName);
    }
}

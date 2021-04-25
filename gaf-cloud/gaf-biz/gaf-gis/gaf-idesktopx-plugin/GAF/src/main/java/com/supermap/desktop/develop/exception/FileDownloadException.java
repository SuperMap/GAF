package com.supermap.desktop.develop.exception;

/**
 * @author heykb
 */
public class FileDownloadException extends RuntimeException {
    public FileDownloadException(Throwable cause) {
        super(cause);
    }

    public FileDownloadException() {
    }
    public FileDownloadException(String fileName) {
        super(fileName);
    }
}

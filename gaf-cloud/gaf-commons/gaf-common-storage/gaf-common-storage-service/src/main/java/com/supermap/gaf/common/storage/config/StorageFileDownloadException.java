package com.supermap.gaf.common.storage.config;

public class StorageFileDownloadException extends RuntimeException {

    public StorageFileDownloadException() {
    }

    public StorageFileDownloadException(String message) {
        super(message);
    }

    public StorageFileDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageFileDownloadException(Throwable cause) {
        super(cause);
    }

    public StorageFileDownloadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

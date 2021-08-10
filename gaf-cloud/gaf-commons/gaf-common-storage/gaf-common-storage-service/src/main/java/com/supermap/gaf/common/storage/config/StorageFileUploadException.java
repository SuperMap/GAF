package com.supermap.gaf.common.storage.config;

public class StorageFileUploadException extends RuntimeException {

    public StorageFileUploadException() {
    }

    public StorageFileUploadException(String message) {
        super(message);
    }

    public StorageFileUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageFileUploadException(Throwable cause) {
        super(cause);
    }

    public StorageFileUploadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

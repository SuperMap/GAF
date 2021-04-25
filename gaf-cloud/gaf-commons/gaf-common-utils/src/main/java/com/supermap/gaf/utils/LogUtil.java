package com.supermap.gaf.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

import com.supermap.gaf.services.resource.EmptyResource;

public final class LogUtil {
    
    private static final String OPERATION_LOGTYPE_NAME = "operation";
    
    private static final String ACCESS_LOGTYPE_NAME = "access";
    
    public static LocLogger getLocLogger(Class<?> clazz) {
        return getLocLogger(clazz, null);
    }
    
    public static LocLogger getLocLogger(Class<?> clazz, ResourceManager rm) {
        if(rm == null) {
            rm = new ResourceManager(EmptyResource.class);
        }
        LocLoggerFactory localLocLoggerFactory = new LocLoggerFactory(rm);
        return localLocLoggerFactory.getLocLogger(clazz);
    }
    
    public static LocLogger getOperationLogger() {
        return getOperationLogger(null);
    }
    
    public static LocLogger getOperationLogger(ResourceManager rm) {
        if(rm == null) {
            rm = new ResourceManager(EmptyResource.class);
        }
        LocLoggerFactory localLocLoggerFactory = new LocLoggerFactory(rm);
        return localLocLoggerFactory.getLocLogger(OPERATION_LOGTYPE_NAME);
    }
    
    public static Logger getAccessLogger() {
        return LoggerFactory.getLogger(ACCESS_LOGTYPE_NAME);
    }

    public static void logException(LocLogger locLogger, Throwable e) {
        locLogger.debug("Exception:", e);
    }
}

package com.supermap.gaf.utils;

//import org.slf4j.cal10n.LocLogger;
//import org.slf4j.cal10n.LocLoggerFactory;
//
//import ch.qos.cal10n.MessageConveyor;

public final class ClassCastUtil {
    
//    private static MessageConveyor messageConveyor = new MessageConveyor(Locale.getDefault());
//    private static LocLoggerFactory factory = new LocLoggerFactory(messageConveyor);
//    private static LocLogger logger = factory.getLocLogger(ClassCastUtil.class);
    
    public static <K, T extends K> T cast(K object, Class<T> clz) {
        try {
            return clz.cast(object);
        } catch (ClassCastException e) {
//            logger.debug(e.getMessage(), e);
            return null;
        }
    }
}

package com.supermap.gaf.srv.governance.entity.rest.configserver;

import com.supermap.gaf.utils.LogUtil;
import org.slf4j.Logger;

/**
 * @program: gaf-microservice-mgt
 * @description:
 * @author: lidong
 * @create: 2019/08/22
 */
public enum InstanceStatus {

    /**
     * Ready to receive traffic
     */
    UP,
    /**
     * Do not send traffic- healthcheck callback failed
     */
    DOWN,
    /**
     * Just about starting- initializations to be done - do not
     */
    STARTING,
    /**
     * send traffic
     * Intentionally shutdown for traffic
     */
    OUT_OF_SERVICE,
    UNKNOWN;

    public static InstanceStatus toEnum(String s) {
        Logger logger = LogUtil.getLocLogger(InstanceStatus.class);
        if (s != null) {
            try {
                return InstanceStatus.valueOf(s.toUpperCase());
            } catch (IllegalArgumentException e) {
                // ignore and fall through to unknown
                logger.debug("illegal argument supplied to InstanceStatus.valueOf: {}, defaulting to {}", s, UNKNOWN);
            }
        }
        return UNKNOWN;
    }
}

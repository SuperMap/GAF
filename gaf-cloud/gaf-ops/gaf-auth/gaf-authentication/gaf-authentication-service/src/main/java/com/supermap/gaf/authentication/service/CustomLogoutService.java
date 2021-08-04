package com.supermap.gaf.authentication.service;


/**
 * @author : duke
 * @since 2020/11/17 3:33 PM
 */
public interface CustomLogoutService {

    /**
     * 使用username手动创建oauth2 token
     *
     * @param customSessionId
     * @return
     */
    boolean logout(String customSessionId);

}

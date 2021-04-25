/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.utils;



import com.supermap.gaf.authentication.entity.entity.AuthenticationParam;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.supermap.gaf.authentication.entity.constant.LoginConstant.AUTHORIZATION;
import static com.supermap.gaf.authentication.entity.constant.LoginConstant.BEARER;
import static com.supermap.gaf.authentication.entity.constant.LoginConstant.CUSTOM_LOGIN_SESSION_NAME;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/16 5:01 PM
 */
public class HttpRequestUtils {



    public static AuthenticationParam getJwtOrSession(HttpServletRequest request){
        String authorization = request.getHeader(AUTHORIZATION);
        List<Cookie> cookies = null == request.getCookies()?null:Arrays.asList(request.getCookies());

        Cookie cookie = null;
        if (null != cookies && !CollectionUtils.isEmpty(cookies)){
            cookies = cookies.stream().filter(item -> CUSTOM_LOGIN_SESSION_NAME.equalsIgnoreCase(item.getName())).collect(Collectors.toList());
        }

        if (!CollectionUtils.isEmpty(cookies)){
            cookie = cookies.get(0);
        }

        AuthenticationParam authenticationParam = new AuthenticationParam();
        authenticationParam.setAuthorization(removeTokenBeareHead(authorization));
        authenticationParam.setCustomSessionId(null == cookie?null:cookie.getValue());

        return authenticationParam;
    }

    public static boolean containsJwtOrSession(HttpServletRequest request){
        boolean result = false;
        AuthenticationParam authenticationParam = HttpRequestUtils.getJwtOrSession(request);
        String sessionId = authenticationParam.getCustomSessionId();
        String authorization =  authenticationParam.getAuthorization();

        if (!StringUtils.isEmpty(sessionId) || !StringUtils.isEmpty(authorization)){
            result = true;
        }
        return result;
    }

    public static String removeTokenBeareHead(String token){
        if (token != null && token.startsWith(BEARER)){
            token = token.substring(BEARER.length()).trim();
        }
        return token;
    }

}

/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.shiro;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/16 5:01 PM
 */
public class JJWTUtils {
    public static String getUserNameFromJwsUntrusted(String jws, String userNameAttribute) {
        int i = jws.lastIndexOf('.');
        String withoutSignature = jws.substring(0, i + 1);
        Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);
        return untrusted.getBody().get(userNameAttribute).toString();
    }
}

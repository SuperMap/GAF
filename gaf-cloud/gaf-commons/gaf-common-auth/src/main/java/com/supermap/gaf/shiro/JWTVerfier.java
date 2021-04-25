/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.shiro;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.oauth2.sdk.assertions.jwt.JWTAssertionDetailsVerifier;
import com.nimbusds.oauth2.sdk.id.Audience;


/**
 * @author:yj
 * @date:2021/3/25
*/
public class JWTVerfier extends JWTAssertionDetailsVerifier {

    private String expectedIssuer;

    public JWTVerfier(String expectedIssuer, Set<Audience> expectedAudience, int maxClockSkewSeconds) {
        super(expectedAudience);
        this.expectedIssuer = expectedIssuer;
        this.setMaxClockSkew(maxClockSkewSeconds);
    }

    @Override
    public void verify(final JWTClaimsSet claimsSet)
        throws BadJWTException {
        super.verify(claimsSet);
        
        String tokenIssuer = claimsSet.getIssuer();
        if (StringUtils.isNotBlank(tokenIssuer)) {
            if (! expectedIssuer.equals(tokenIssuer)) {
                throw new BadJWTException("Unexpected JWT issuer: " + tokenIssuer);
            }
        }
    }
}

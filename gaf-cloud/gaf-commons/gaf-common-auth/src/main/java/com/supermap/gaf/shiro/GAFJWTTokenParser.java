/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.shiro;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.openid.connect.sdk.claims.IDTokenClaimsSet;
import com.supermap.gaf.shiro.config.KeycloakConfig;
import org.apache.shiro.authc.AuthenticationException;
import org.pac4j.core.authorization.generator.AuthorizationGenerator;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.profile.ProfileHelper;
import org.pac4j.core.profile.definition.ProfileDefinition;
import org.pac4j.core.profile.jwt.JwtClaims;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.oauth.profile.OAuth20Profile;
import org.pac4j.oidc.profile.OidcProfile;
import org.pac4j.oidc.profile.OidcProfileDefinition;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static org.pac4j.core.profile.AttributeLocation.PROFILE_ATTRIBUTE;
import static org.pac4j.core.util.CommonHelper.assertNotNull;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class GAFJWTTokenParser {

    private KeycloakConfig configuration;
    private JWTOidcValidator idTokenValidator;
    private ProfileDefinition<OidcProfile> profileDefinition = new OidcProfileDefinition<>();
    private List<AuthorizationGenerator<OidcProfile>> authorizationGenerators;

    public OAuth20Profile parseOauthJWTToken(String token) throws Exception {
        OAuth20Profile profile = new OAuth20Profile() {
            @Override
            public void setAccessToken(String accessToken) {
                super.setAccessToken(accessToken);
            }
        };
        profile.setAccessToken(token);
        return profile;
    }

    public OidcProfile parseToken(String accessToken) throws Exception {
        try {
            if (this.idTokenValidator == null) {
                this.internalInit();
            }
            JWT idToken = JWTParser.parse(accessToken);
            IDTokenClaimsSet claimsSet = this.getIdTokenValidator().validate(idToken, null);
            OidcProfile profile = profileDefinition.newProfile();
            profile.setIdTokenString(idToken.getParsedString());
            profile.setId(ProfileHelper.sanitizeIdentifier(profile, claimsSet.getSubject()));
            // add attributes of the ID token if they don't already exist
            for (final Map.Entry<String, Object> entry : idToken.getJWTClaimsSet().getClaims().entrySet()) {
                final String key = entry.getKey();
                final Object value = entry.getValue();
                // it's not the subject and this attribute does not already exist, add it
                if (!JwtClaims.SUBJECT.equals(key) && profile.getAttribute(key) == null) {
                    profileDefinition.convertAndAdd(profile, PROFILE_ATTRIBUTE, key, value);
                }
            }
            if (this.getAuthorizationGenerators() != null) {
                for (AuthorizationGenerator<OidcProfile> authorizationGenerator : this.getAuthorizationGenerators()) {
                    profile = authorizationGenerator.generate(null, profile);
                }
            }
            return profile;
        } catch (ParseException e) {
            throw new AuthenticationException("JWT 令牌无效:" + e.getMessage());
        } catch (BadJOSEException e) {
            throw new AuthenticationException("JWT 令牌无效:" + e.getMessage());
        } catch (JOSEException e) {
            throw new AuthenticationException("JWT 令牌无效:" + e.getMessage());
        }
    }

    protected void internalInit() {
        assertNotNull("configuration", getConfiguration());
        // check algorithms
        final List<JWSAlgorithm> metadataAlgorithms = getConfiguration().findProviderMetadata().getIDTokenJWSAlgs();
        CommonHelper.assertTrue(CommonHelper.isNotEmpty(metadataAlgorithms),
                "There must at least one JWS algorithm supported on the OpenID Connect provider side");
        JWSAlgorithm jwsAlgorithm;
        final JWSAlgorithm preferredAlgorithm = getConfiguration().getPreferredJwsAlgorithm();
        if (metadataAlgorithms.contains(preferredAlgorithm)) {
            jwsAlgorithm = preferredAlgorithm;
        } else {
            jwsAlgorithm = metadataAlgorithms.get(0);
        }
        if ("none".equals(jwsAlgorithm.getName())) {
            jwsAlgorithm = null;
        }
        final ClientID clientID = new ClientID(getConfiguration().getClientId());
        final Secret secret = new Secret(getConfiguration().getSecret());
        // Init IDTokenVerifier
        if (jwsAlgorithm == null) {
            this.setIdTokenValidator(new JWTOidcValidator(getConfiguration().findProviderMetadata().getIssuer(), clientID));
        } else if (CommonHelper.isNotBlank(getConfiguration().getSecret()) && (JWSAlgorithm.HS256.equals(jwsAlgorithm) ||
                JWSAlgorithm.HS384.equals(jwsAlgorithm) || JWSAlgorithm.HS512.equals(jwsAlgorithm))) {
            this.setIdTokenValidator(createHMACTokenValidator(jwsAlgorithm, clientID, secret));
        } else {
            this.setIdTokenValidator(createRSATokenValidator(jwsAlgorithm, clientID));
        }
        this.getIdTokenValidator().setMaxClockSkew(getConfiguration().getMaxClockSkew());

    }

    protected JWTOidcValidator createRSATokenValidator(final JWSAlgorithm jwsAlgorithm, final ClientID clientID) {
        try {
            return new JWTOidcValidator(getConfiguration().findProviderMetadata().getIssuer(), clientID, jwsAlgorithm,
                    getConfiguration().findProviderMetadata().getJWKSetURI().toURL(), getConfiguration().findResourceRetriever());
        } catch (final MalformedURLException e) {
            throw new TechnicalException(e);
        }
    }

    protected JWTOidcValidator createHMACTokenValidator(final JWSAlgorithm jwsAlgorithm, final ClientID clientID, final Secret secret) {
        return new JWTOidcValidator(getConfiguration().findProviderMetadata().getIssuer(), clientID, jwsAlgorithm, secret);
    }

    public KeycloakConfig getConfiguration() {
        return configuration;
    }

    public void setConfiguration(KeycloakConfig configuration) {
        this.configuration = configuration;
    }

    public List<AuthorizationGenerator<OidcProfile>> getAuthorizationGenerators() {
        return authorizationGenerators;
    }

    public void setAuthorizationGenerators(List<AuthorizationGenerator<OidcProfile>> authorizationGenerators) {
        this.authorizationGenerators = authorizationGenerators;
    }

    public JWTOidcValidator getIdTokenValidator() {
        return idTokenValidator;
    }

    public void setIdTokenValidator(JWTOidcValidator idTokenValidator) {
        this.idTokenValidator = idTokenValidator;
    }

}

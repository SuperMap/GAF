/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.shiro;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWEDecryptionKeySelector;
import com.nimbusds.jose.proc.JWEKeySelector;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTClaimsSetVerifier;
import com.nimbusds.oauth2.sdk.GeneralException;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.id.Audience;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.openid.connect.sdk.Nonce;
import com.nimbusds.openid.connect.sdk.claims.IDTokenClaimsSet;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientInformation;
@SuppressWarnings({"rawtypes", "unchecked"})

/**
 * @author:yj
 * @date:2021/3/25
*/
public class JWTOidcValidator {

    public static final int DEFAULT_MAX_CLOCK_SKEW = 60;

    private Issuer expectedIssuer;

    private ClientID clientID;
   
    private JWSKeySelector jwsKeySelector;

    private JWEKeySelector jweKeySelector;

    private int maxClockSkew = DEFAULT_MAX_CLOCK_SKEW;

    public JWTOidcValidator(final Issuer expectedIssuer, final ClientID clientID) {

        this(expectedIssuer, clientID, (JWSKeySelector) null, null);
    }

    public JWTOidcValidator(final Issuer expectedIssuer, final ClientID clientID, final JWSAlgorithm expectedJWSAlg, final JWKSet jwkSet) {

        this(expectedIssuer, clientID, new JWSVerificationKeySelector(expectedJWSAlg, new ImmutableJWKSet(jwkSet)), null);
    }

    public JWTOidcValidator(final Issuer expectedIssuer, final ClientID clientID, final JWSAlgorithm expectedJWSAlg, final URL jwkSetURI) {

        this(expectedIssuer, clientID, expectedJWSAlg, jwkSetURI, null);
    }

    public JWTOidcValidator(final Issuer expectedIssuer, final ClientID clientID, final JWSAlgorithm expectedJWSAlg, final URL jwkSetURI,
            final ResourceRetriever resourceRetriever) {

        this(expectedIssuer, clientID, new JWSVerificationKeySelector(expectedJWSAlg, new RemoteJWKSet(jwkSetURI, resourceRetriever)), null);
    }

    public JWTOidcValidator(final Issuer expectedIssuer, final ClientID clientID, final JWSAlgorithm expectedJWSAlg, final Secret clientSecret) {

        this(expectedIssuer, clientID, new JWSVerificationKeySelector(expectedJWSAlg, new ImmutableSecret(clientSecret.getValueBytes())), null);
    }

    public JWTOidcValidator(final Issuer expectedIssuer, final ClientID clientID, final JWSKeySelector jwsKeySelector, final JWEKeySelector jweKeySelector) {

        this.expectedIssuer = expectedIssuer;
        this.clientID = clientID;
        this.jwsKeySelector = jwsKeySelector;
        this.jweKeySelector = jweKeySelector;
    }

    public IDTokenClaimsSet validate(final JWT idToken, final Nonce expectedNonce) throws BadJOSEException, JOSEException {

        if (idToken instanceof PlainJWT) {
            return validate((PlainJWT) idToken, expectedNonce);
        } else if (idToken instanceof SignedJWT) {
            return validate((SignedJWT) idToken, expectedNonce);
        } else if (idToken instanceof EncryptedJWT) {
            return validate((EncryptedJWT) idToken, expectedNonce);
        } else {
            throw new JOSEException("Unexpected JWT type: " + idToken.getClass());
        }
    }

    private IDTokenClaimsSet validate(final PlainJWT idToken, final Nonce expectedNonce) throws BadJOSEException, JOSEException {

        if (jwsKeySelector == null) {
            throw new BadJWTException("Signed ID token expected");
        }

        JWTClaimsSet jwtClaimsSet;

        try {
            jwtClaimsSet = idToken.getJWTClaimsSet();
        } catch (java.text.ParseException e) {
            throw new BadJWTException(e.getMessage(), e);
        }

        Set<Audience> auds = new HashSet<>();
        auds.add(new Audience("account"));
        auds.add(new Audience(this.clientID));
        JWTClaimsSetVerifier<?> claimsVerifier = new JWTVerfier(expectedIssuer.getValue(), auds, this.getMaxClockSkew());
        claimsVerifier.verify(jwtClaimsSet, null);
        return toIDTokenClaimsSet(jwtClaimsSet);
    }

    private IDTokenClaimsSet validate(final SignedJWT idToken, final Nonce expectedNonce) throws BadJOSEException, JOSEException {

        if (jwsKeySelector == null) {
            throw new BadJWTException("Verification of signed JWTs not configured");
        }

        ConfigurableJWTProcessor<?> jwtProcessor = new DefaultJWTProcessor();
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        Set<Audience> auds = new HashSet<>();
        auds.add(new Audience("account"));
        auds.add(new Audience(this.clientID));
        JWTClaimsSetVerifier<?> claimsVerifier = new JWTVerfier(expectedIssuer.getValue(), auds, this.getMaxClockSkew());
        JWTClaimsSet jwtClaimsSet = jwtProcessor.process(idToken, null);
        return toIDTokenClaimsSet(jwtClaimsSet);
    }

    private IDTokenClaimsSet validate(final EncryptedJWT idToken, final Nonce expectedNonce) throws BadJOSEException, JOSEException {

        if (jwsKeySelector == null) {
            throw new BadJWTException("Decryption of JWTs not configured");
        }
        if (jweKeySelector == null) {
            throw new BadJWTException("Verification of signed JWTs not configured");
        }

        ConfigurableJWTProcessor<?> jwtProcessor = new DefaultJWTProcessor();
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        jwtProcessor.setJWEKeySelector(jweKeySelector);
        
        Set<Audience> auds = new HashSet<>();
        auds.add(new Audience("account"));
        auds.add(new Audience(this.clientID));
        JWTClaimsSetVerifier<?> claimsVerifier = new JWTVerfier(expectedIssuer.getValue(), auds, this.getMaxClockSkew());

        JWTClaimsSet jwtClaimsSet = jwtProcessor.process(idToken, null);

        return toIDTokenClaimsSet(jwtClaimsSet);
    }

    private static IDTokenClaimsSet toIDTokenClaimsSet(final JWTClaimsSet jwtClaimsSet) throws JOSEException {

        try {
            return new IDTokenClaimsSet(jwtClaimsSet);
        } catch (ParseException e) {
            // Claims set must be verified at this point
            throw new JOSEException(e.getMessage(), e);
        }
    }

    protected static JWSKeySelector createJWSKeySelector(final OIDCProviderMetadata opMetadata, final OIDCClientInformation clientInfo)
            throws GeneralException {

        final JWSAlgorithm expectedJWSAlg = clientInfo.getOIDCMetadata().getIDTokenJWSAlg();

        if (opMetadata.getIDTokenJWSAlgs() == null) {
            throw new GeneralException("Missing OpenID Provider id_token_signing_alg_values_supported parameter");
        }

        if (!opMetadata.getIDTokenJWSAlgs().contains(expectedJWSAlg)) {
            throw new GeneralException("The OpenID Provider doesn't support " + expectedJWSAlg + " ID tokens");
        }

        if (Algorithm.NONE.equals(expectedJWSAlg)) {
            // Skip creation of JWS key selector, plain ID tokens expected
            return null;

        } else if (JWSAlgorithm.Family.RSA.contains(expectedJWSAlg) || JWSAlgorithm.Family.EC.contains(expectedJWSAlg)) {

            URL jwkSetURL;
            try {
                jwkSetURL = opMetadata.getJWKSetURI().toURL();
            } catch (MalformedURLException e) {
                throw new GeneralException("Invalid jwk set URI: " + e.getMessage(), e);
            }
            JWKSource jwkSource = new RemoteJWKSet(jwkSetURL); // TODO specify HTTP response limits

            return new JWSVerificationKeySelector(expectedJWSAlg, jwkSource);

        } else if (JWSAlgorithm.Family.HMAC_SHA.contains(expectedJWSAlg)) {

            Secret clientSecret = clientInfo.getSecret();
            if (clientSecret == null) {
                throw new GeneralException("Missing client secret");
            }
            return new JWSVerificationKeySelector(expectedJWSAlg, new ImmutableSecret(clientSecret.getValueBytes()));

        } else {
            throw new GeneralException("Unsupported JWS algorithm: " + expectedJWSAlg);
        }
    }

    protected static JWEKeySelector createJWEKeySelector(final OIDCProviderMetadata opMetadata, final OIDCClientInformation clientInfo,
            final JWKSource clientJWKSource) throws GeneralException {

        final JWEAlgorithm expectedJWEAlg = clientInfo.getOIDCMetadata().getIDTokenJWEAlg();
        final EncryptionMethod expectedJWEEnc = clientInfo.getOIDCMetadata().getIDTokenJWEEnc();

        if (expectedJWEAlg == null) {
            // Encrypted ID tokens not expected
            return null;
        }

        if (expectedJWEEnc == null) {
            throw new GeneralException("Missing required ID token JWE encryption method for " + expectedJWEAlg);
        }

        if (opMetadata.getIDTokenJWEAlgs() == null || !opMetadata.getIDTokenJWEAlgs().contains(expectedJWEAlg)) {
            throw new GeneralException("The OpenID Provider doesn't support " + expectedJWEAlg + " ID tokens");
        }

        if (opMetadata.getIDTokenJWEEncs() == null || !opMetadata.getIDTokenJWEEncs().contains(expectedJWEEnc)) {
            throw new GeneralException("The OpenID Provider doesn't support " + expectedJWEAlg + " / " + expectedJWEEnc + " ID tokens");
        }

        return new JWEDecryptionKeySelector(expectedJWEAlg, expectedJWEEnc, clientJWKSource);
    }

    public static JWTOidcValidator create(final OIDCProviderMetadata opMetadata, final OIDCClientInformation clientInfo, final JWKSource clientJWKSource)
            throws GeneralException {

        // Create JWS key selector, unless id_token alg = none
        final JWSKeySelector jwsKeySelector = createJWSKeySelector(opMetadata, clientInfo);

        // Create JWE key selector if encrypted ID tokens are expected
        final JWEKeySelector jweKeySelector = createJWEKeySelector(opMetadata, clientInfo, clientJWKSource);

        return new JWTOidcValidator(opMetadata.getIssuer(), clientInfo.getID(), jwsKeySelector, jweKeySelector);
    }

    public static JWTOidcValidator create(final OIDCProviderMetadata opMetadata, final OIDCClientInformation clientInfo) throws GeneralException {

        return create(opMetadata, clientInfo, null);
    }

    public static JWTOidcValidator create(final Issuer opIssuer, final OIDCClientInformation clientInfo) throws GeneralException, IOException {

        return create(opIssuer, clientInfo, null, 0, 0);
    }

    public static JWTOidcValidator create(final Issuer opIssuer, final OIDCClientInformation clientInfo, final JWKSource clientJWKSource,
            final int connectTimeout, final int readTimeout) throws GeneralException, IOException {

        OIDCProviderMetadata opMetadata = OIDCProviderMetadata.resolve(opIssuer, connectTimeout, readTimeout);

        return create(opMetadata, clientInfo, clientJWKSource);
    }

    public int getMaxClockSkew() {
        return maxClockSkew;
    }

    public void setMaxClockSkew(int maxClockSkew) {
        this.maxClockSkew = maxClockSkew;
    }
}

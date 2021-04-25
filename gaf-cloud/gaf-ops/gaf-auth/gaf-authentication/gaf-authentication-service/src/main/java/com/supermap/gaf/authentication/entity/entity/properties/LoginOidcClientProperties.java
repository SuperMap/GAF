/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authentication.entity.entity.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author : duke
 * @date:2021/3/25
 * @since 2020/11/16 10:57 AM
 */
@ConfigurationProperties(prefix = "login.third-party")
@Component
public class LoginOidcClientProperties {

    private final Map<String, OidcClientInfo> oidc = new HashMap();

    public Map<String, OidcClientInfo> getOidc() {
        return oidc;
    }

    @PostConstruct
    public void validate() {
        this.getOidc().values().forEach(this::validateRegistration);
    }

    private void validateRegistration(LoginOidcClientProperties.OidcClientInfo oidcClientInfo) {
        if (!StringUtils.hasText(oidcClientInfo.getRegistration().getClientId())) {
            throw new IllegalStateException("Client id must not be empty.");
        }
    }


    public static class OidcClientInfo {
        private Provider provider;
        private Registration registration;

        public Provider getProvider() {
            return provider;
        }
        public void setProvider(Provider provider) {
            this.provider = provider;
        }
        public Registration getRegistration() {
            return registration;
        }
        public void setRegistration(Registration registration) {
            this.registration = registration;
        }
    }


    public static class Provider {
        private String authorizationUri;
        private String tokenUri;
        private String userInfoUri;
        private String userNameAttribute;


        public String getAuthorizationUri() {
            return authorizationUri;
        }
        public void setAuthorizationUri(String authorizationUri) {
            this.authorizationUri = authorizationUri;
        }
        public String getTokenUri() {
            return tokenUri;
        }
        public void setTokenUri(String tokenUri) {
            this.tokenUri = tokenUri;
        }
        public String getUserInfoUri() {
            return userInfoUri;
        }
        public void setUserInfoUri(String userInfoUri) {
            this.userInfoUri = userInfoUri;
        }
        public String getUserNameAttribute() {
            return userNameAttribute;
        }
        public void setUserNameAttribute(String userNameAttribute) {
            this.userNameAttribute = userNameAttribute;
        }
    }

    public static class Registration {
        private String clientId;
        private String clientSecret;

        private String authorizationGrantType;
        private String redirectUri;
        private Set<String> scope;

        public static String getScopeString(Set<String> scopeSet){
            StringBuilder stringBuilder = new StringBuilder();
            scopeSet.forEach(scope -> stringBuilder.append(scope+ " "));
            String scopes =  stringBuilder.toString();
            scopes = scopes.substring(0,scopes.length()-1);
            return scopes;
        }

        public String getClientId() {
            return clientId;
        }
        public void setClientId(String clientId) {
            this.clientId = clientId;
        }
        public String getClientSecret() {
            return clientSecret;
        }
        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }
        public String getAuthorizationGrantType() {
            return authorizationGrantType;
        }
        public void setAuthorizationGrantType(String authorizationGrantType) {
            this.authorizationGrantType = authorizationGrantType;
        }
        public String getRedirectUri() {
            return redirectUri;
        }
        public void setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
        }
        public Set<String> getScope() {
            return scope;
        }
        public void setScope(Set<String> scope) {
            this.scope = scope;
        }
    }

}

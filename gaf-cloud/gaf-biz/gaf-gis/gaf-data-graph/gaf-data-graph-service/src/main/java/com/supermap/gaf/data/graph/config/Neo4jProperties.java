package com.supermap.gaf.data.graph.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : duke
 * @since 2021/5/7 2:28 PM
 */
@Component
@ConfigurationProperties("neo4j")
public class Neo4jProperties {
    private String uri;
    private String username;
    private String password;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

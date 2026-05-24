package com.springboot.proyectospring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dni.api")
public class DniProperties {

    private String token;
    private String url;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}

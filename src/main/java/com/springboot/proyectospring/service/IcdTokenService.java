package com.springboot.proyectospring.service;

import com.springboot.proyectospring.config.IcdProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Map;

@Service
public class IcdTokenService {

    private final IcdProperties props;
    private final RestClient restClient;

    private String cachedToken;
    private Instant tokenExpiry = Instant.MIN;

    public IcdTokenService(IcdProperties props) {
        this.props = props;
        this.restClient = RestClient.create();
    }

    public String getToken() {
        if (cachedToken != null && Instant.now().isBefore(tokenExpiry)) {
            return cachedToken;
        }
        return fetchNewToken();
    }

    @SuppressWarnings("unchecked")
    private String fetchNewToken() {
        String body = "grant_type=client_credentials"
                + "&client_id=" + props.getClientId()
                + "&client_secret=" + props.getClientSecret()
                + "&scope=icdapi_access";

        Map<String, Object> response = restClient.post()
                .uri(props.getTokenUrl())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(body)
                .retrieve()
                .body(Map.class);

        cachedToken = (String) response.get("access_token");
        int expiresIn = (int) response.getOrDefault("expires_in", 3600);
        tokenExpiry = Instant.now().plusSeconds(expiresIn - 60);
        return cachedToken;
    }
}

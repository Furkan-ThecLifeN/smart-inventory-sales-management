package com.company.inventory.desktop.client;

import com.company.inventory.desktop.util.UserSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (UserSession.getToken() != null) {
            headers.setBearerAuth(UserSession.getToken());
        }
        return headers;
    }

    public <T> ResponseEntity<T> post(String path, Object body, Class<T> responseType) {
        HttpEntity<Object> entity = new HttpEntity<>(body, getHeaders());
        return restTemplate.postForEntity(BASE_URL + path, entity, responseType);
    }

    public <T> ResponseEntity<T> get(String path, Class<T> responseType) {
        HttpEntity<Object> entity = new HttpEntity<>(getHeaders());
        return restTemplate.exchange(BASE_URL + path, HttpMethod.GET, entity, responseType);
    }

    public <T> ResponseEntity<T> get(String path, ParameterizedTypeReference<T> responseType) {
        HttpEntity<Object> entity = new HttpEntity<>(getHeaders());
        return restTemplate.exchange(BASE_URL + path, HttpMethod.GET, entity, responseType);
    }
}

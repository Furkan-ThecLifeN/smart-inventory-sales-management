package com.company.inventory.desktop.client;

import com.company.inventory.desktop.util.UserSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders getHeaders(String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (!path.startsWith("/auth")) {
            String token = UserSession.getToken();
            if (token != null && !token.isBlank()) {
                headers.setBearerAuth(token);
            }
        }

        return headers;
    }

    public <T> ResponseEntity<T> post(String path, Object body, Class<T> responseType) {
        try {
            HttpEntity<Object> entity = new HttpEntity<>(body, getHeaders(path));
            return restTemplate.postForEntity(BASE_URL + path, entity, responseType);
        } catch (HttpClientErrorException.Forbidden e) {
            System.err.println("Hata 403: Yetkisiz erişim! Token geçersiz olabilir veya bu işlem için yetkiniz yok.");
            return null;
        } catch (Exception e) {
            System.err.println("API Post Hatası (" + path + "): " + e.getMessage());
            throw e;
        }
    }

    public <T> ResponseEntity<T> get(String path, Class<T> responseType) {
        HttpEntity<Object> entity = new HttpEntity<>(getHeaders(path));
        return restTemplate.exchange(BASE_URL + path, HttpMethod.GET, entity, responseType);
    }

    public <T> ResponseEntity<T> get(String path, ParameterizedTypeReference<T> responseType) {
        HttpEntity<Object> entity = new HttpEntity<>(getHeaders(path));
        return restTemplate.exchange(BASE_URL + path, HttpMethod.GET, entity, responseType);
    }
}

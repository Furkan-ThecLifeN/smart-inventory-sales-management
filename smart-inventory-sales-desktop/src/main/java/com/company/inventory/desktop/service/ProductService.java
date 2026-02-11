package com.company.inventory.desktop.service;

import com.company.inventory.desktop.client.ApiClient;
import com.company.inventory.desktop.model.ProductDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import java.util.List;

public class ProductService {

    private final ApiClient apiClient = new ApiClient();

    public List<ProductDto> getAllProducts() {
        ResponseEntity<List<ProductDto>> response = apiClient.get(
                "/products",
                new ParameterizedTypeReference<List<ProductDto>>() {}
        );
        return response.getBody();
    }

    public void createProduct(ProductDto productData) {
        apiClient.post("/products", productData, Void.class);
    }
}

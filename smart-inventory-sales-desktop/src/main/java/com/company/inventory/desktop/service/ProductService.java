package com.company.inventory.desktop.service;

import com.company.inventory.desktop.client.ApiClient;
import com.company.inventory.desktop.model.LocalProductDto;
import com.company.inventory.desktop.model.ProductDto;
import com.company.inventory.desktop.repository.ProductLocalRepository;
import com.company.inventory.desktop.util.ConnectivityService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import java.util.List;

public class ProductService {

    private final ProductLocalRepository localRepo = new ProductLocalRepository();
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

    public void saveProduct(LocalProductDto product) throws Exception {
        product.setSyncStatus("PENDING");
        localRepo.save(product);

        if (ConnectivityService.isOnline()) {
            try {
                var response = apiClient.post("/api/products", product, LocalProductDto.class);
                if (response != null) {
                    product.setSyncStatus("SYNCED");
                    localRepo.save(product);
                }
            } catch (Exception ignored) {
            }
        }
    }
}

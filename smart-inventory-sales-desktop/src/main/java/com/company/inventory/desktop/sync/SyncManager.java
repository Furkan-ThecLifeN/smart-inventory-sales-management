package com.company.inventory.desktop.sync;

import com.company.inventory.desktop.client.ApiClient;
import com.company.inventory.desktop.repository.ProductLocalRepository;
import com.company.inventory.desktop.model.LocalProductDto;
import com.company.inventory.desktop.util.ConnectivityService;
import com.company.inventory.desktop.util.UserSession;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class SyncManager {
    private final ProductLocalRepository localRepo = new ProductLocalRepository();
    private final ApiClient apiClient = new ApiClient();

    public void performSync() {
        // İnternet ve Token yoksa işlem yapma 
        if (!ConnectivityService.isOnline() || UserSession.getToken() == null) {
            return;
        }

        try {
            List<LocalProductDto> pendingProducts = localRepo.getPendingSyncs();
            if (pendingProducts == null || pendingProducts.isEmpty()) return;

            for (LocalProductDto localProduct : pendingProducts) {
                try {
                    // Sadece Backend'in kabul ettiği alanları Map ile gönderiyoruz 
                    Map<String, Object> requestBody = new HashMap<>();
                    requestBody.put("name", localProduct.getName());
                    requestBody.put("sku", localProduct.getSku());
                    requestBody.put("price", localProduct.getPrice());
                    requestBody.put("stockQuantity", localProduct.getStockQuantity());
                    requestBody.put("active", true);
                    requestBody.put("description", "Senkronize Edildi");

                    // BASE_URL /api/v1 olduğu için yol "/products" 
                    ResponseEntity<LocalProductDto> response = 
                        apiClient.post("/products", requestBody, LocalProductDto.class);

                    if (response != null && response.getStatusCode().is2xxSuccessful()) {
                        localProduct.setSyncStatus("SYNCED");
                        localRepo.save(localProduct);
                        System.out.println("BAŞARILI: " + localProduct.getName() + " senkronize oldu!");
                    }
                } catch (Exception e) {
                    System.err.println("Tekil ürün hatası: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Genel Sync Hatası: " + e.getMessage());
        }
    }
}
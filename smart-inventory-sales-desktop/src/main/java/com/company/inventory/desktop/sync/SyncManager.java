package com.company.inventory.desktop.sync;

import com.company.inventory.desktop.client.ApiClient;
import com.company.inventory.desktop.repository.ProductLocalRepository;
import com.company.inventory.desktop.repository.CustomerLocalRepository;
import com.company.inventory.desktop.model.LocalProductDto;
import com.company.inventory.desktop.model.LocalCustomerDto;
import com.company.inventory.desktop.util.ConnectivityService;
import com.company.inventory.desktop.util.UserSession;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class SyncManager {
    private final ProductLocalRepository productRepo = new ProductLocalRepository();
    private final CustomerLocalRepository customerRepo = new CustomerLocalRepository();
    private final ApiClient apiClient = new ApiClient();

    /**
     * Ana senkronizasyon döngüsü.
     */
    public void performSync() {
        // 1. Güvenlik ve Bağlantı Kontrolleri
        if (!ConnectivityService.isOnline() || UserSession.getToken() == null) {
            return;
        }

        try {
            // 2. Bekleyen Veri Kontrolü
            List<LocalProductDto> pendingProducts = productRepo.getPendingSyncs();
            List<LocalCustomerDto> pendingCustomers = customerRepo.getPendingSyncs();

            boolean hasProducts = (pendingProducts != null && !pendingProducts.isEmpty());
            boolean hasCustomers = (pendingCustomers != null && !pendingCustomers.isEmpty());

            // Bekleyen bir şey yoksa sessizce çık (Konsol kirliliğini önler)
            if (!hasProducts && !hasCustomers) {
                return;
            }

            System.out.println("🔄 Bekleyen veriler bulundu. Senkronizasyon başlatılıyor...");

            if (hasProducts) syncProducts(pendingProducts);
            if (hasCustomers) syncCustomers(pendingCustomers);

        } catch (Exception e) {
            System.err.println("❌ Senkronizasyon döngüsünde hata:");
            e.printStackTrace();
        }
    }

    private void syncProducts(List<LocalProductDto> pending) {
        for (LocalProductDto p : pending) {
            try {
                Map<String, Object> body = new HashMap<>();
                body.put("name", p.getName());
                body.put("sku", p.getSku());
                body.put("price", p.getPrice());
                body.put("stockQuantity", p.getStockQuantity());
                body.put("active", true);

                ResponseEntity<LocalProductDto> res = apiClient.post("/products", body, LocalProductDto.class);
                if (res != null && res.getStatusCode().is2xxSuccessful()) {
                    p.setSyncStatus("SYNCED");
                    productRepo.save(p);
                    System.out.println("✅ Ürün Eşitlendi: " + p.getName());
                }
            } catch (Exception e) {
                System.err.println("❌ Ürün gönderilemedi (" + p.getName() + "): " + e.getMessage());
            }
        }
    }

    private void syncCustomers(List<LocalCustomerDto> pending) {
        for (LocalCustomerDto c : pending) {
            try {
                String fullName = c.getName().trim();
                String firstName, lastName;

                if (fullName.contains(" ")) {
                    firstName = fullName.substring(0, fullName.lastIndexOf(" "));
                    lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
                } else {
                    firstName = fullName;
                    lastName = "-";
                }

                Map<String, Object> body = new HashMap<>();
                body.put("firstName", firstName);
                body.put("lastName", lastName);
                body.put("email", c.getEmail());
                body.put("phone", c.getPhone());
                body.put("address", "Masaüstü Kaydı");

                ResponseEntity<LocalCustomerDto> res = apiClient.post("/customers", body, LocalCustomerDto.class);
                if (res != null && res.getStatusCode().is2xxSuccessful()) {
                    customerRepo.updateStatus(c.getId(), "SYNCED");
                    System.out.println("✅ Müşteri Eşitlendi: " + firstName + " " + lastName);
                }
            } catch (Exception e) {
                System.err.println("❌ Müşteri gönderilemedi (" + c.getName() + "): " + e.getMessage());
            }
        }
    }
}
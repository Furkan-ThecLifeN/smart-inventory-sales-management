package com.company.inventory.controller;

// Mevcut ve çalışan servislerini import ediyoruz
import com.company.inventory.service.ProductService;
import com.company.inventory.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor 
public class DashboardController {

    // Senin halihazırda yazdığın ProductService'i kullanıyoruz
    private final ProductService productService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // ProductService içindeki metodu çağırıp listeyi alıyoruz
        List<ProductResponseDto> allProducts = productService.getAllProducts();

        // İstatistikleri liste üzerinden anlık hesaplıyoruz
        long totalProducts = allProducts.size();
        long lowStockCount = allProducts.stream()
                .filter(p -> p.getStockQuantity() <= 5)
                .count();
        
        // Basit bir toplam değer hesaplaması (Opsiyonel)
        double totalStockValue = allProducts.stream()
                .mapToDouble(p -> p.getPrice().doubleValue() * p.getStockQuantity())
                .sum();

        stats.put("totalProducts", totalProducts);
        stats.put("lowStockCount", lowStockCount);
        stats.put("totalValue", totalStockValue);

        return ResponseEntity.ok(stats);
    }
}
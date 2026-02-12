package com.company.inventory.controller;

import com.company.inventory.dto.ProductRequestDto;
import com.company.inventory.dto.ProductResponseDto;
import com.company.inventory.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping
    // DÜZELTME: SecurityConfig ile uyumlu olması için SALES eklendi
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES', 'USER')")
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    @PutMapping("/{id}/decrease")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES', 'USER')")
    public ResponseEntity<String> decreaseStock(@PathVariable Long id, @RequestParam int quantity) {
        productService.decreaseStock(id, quantity);
        return ResponseEntity.ok("Stok düşürüldü, eğer eşik değerin altındaysa uyarı gönderildi.");
    }
}
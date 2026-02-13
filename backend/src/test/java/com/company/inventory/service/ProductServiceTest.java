package com.company.inventory.service;

import com.company.inventory.model.Product;
import com.company.inventory.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldThrowExceptionWhenStockIsInsufficient() {
        // Hazırlık (Arrange)
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Test Ürünü");
        mockProduct.setStockQuantity(5); // Mevcut stok 5
        
        // Mock davranışı: 1 ID'li ürün istendiğinde mockProduct döner
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));

        // Aksiyon ve Doğrulama (Act & Assert)
        // 10 adet düşmeye çalışıyoruz, stok 5 olduğu için RuntimeException fırlatmalı
        assertThrows(RuntimeException.class, () -> {
            productService.decreaseStock(1L, 10);
        });
    }
}
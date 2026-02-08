package com.company.inventory.service;

import com.company.inventory.dto.ProductRequestDto;
import com.company.inventory.dto.ProductResponseDto;
import com.company.inventory.event.StockLowEvent;
import com.company.inventory.mapper.ProductMapper;
import com.company.inventory.model.Product;
import com.company.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        productRepository.findBySku(dto.getSku())
                .ifPresent(s -> {
                    throw new RuntimeException("SKU already exists!");
                });

        Product product = productMapper.toEntity(dto);
        return productMapper.toDto(productRepository.save(product));
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void decreaseStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        product = productRepository.save(product);

        // decreaseStock metodu içindeki ilgili kısım:
        if (product.getStockQuantity() <= 5) {
            kafkaProducerService.sendStockLowEvent(
                    StockLowEvent.builder()
                            .productId(product.getId())
                            .productName(product.getName())
                            .sku(product.getSku())
                            .currentStock(product.getStockQuantity())
                            .message("Kritik stok seviyesi!") 
                            .eventTime(LocalDateTime.now())
                            .build());
        }
    }
}

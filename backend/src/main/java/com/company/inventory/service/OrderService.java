package com.company.inventory.service;

import com.company.inventory.dto.OrderRequestDto;
import com.company.inventory.model.Customer;
import com.company.inventory.model.Order;
import com.company.inventory.model.OrderStatus;
import com.company.inventory.model.Product;
import com.company.inventory.repository.CustomerRepository;
import com.company.inventory.repository.OrderRepository;
import com.company.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService; // Stok düşümü için
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Transactional
    public String createOrder(OrderRequestDto dto) {
        // 1. Müşteri kontrolü
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Order failed: Customer not found"));

        // 2. Sipariş objesi hazırlığı
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setCustomer(customer);
        order.setStatus(OrderStatus.CREATED);

        BigDecimal total = BigDecimal.ZERO;
        Set<Product> products = new HashSet<>();

        // 3. Ürün döngüsü ve Stok Kontrolü
        for (Long productId : dto.getProductIds()) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
            
            if (!product.isActive() || product.isDeleted()) {
                throw new RuntimeException("Product is not available for sale: " + product.getName());
            }

            // ProductService içindeki decreaseStock metodu burada Transactional koruması sağlar.
            // Eğer herhangi bir üründe stok yetmezse tüm sipariş iptal edilir (Rollback).
            productService.decreaseStock(productId, 1); 
            
            total = total.add(product.getPrice());
            products.add(product);
        }

        order.setProducts(products);
        order.setTotalAmount(total);
        
        // 4. Kaydet
        Order savedOrder = orderRepository.save(order);
        return savedOrder.getOrderNumber();
    }
}
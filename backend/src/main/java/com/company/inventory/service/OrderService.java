package com.company.inventory.service;

import com.company.inventory.dto.OrderRequestDto;
import com.company.inventory.event.OrderCreatedEvent;
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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    public String createOrder(OrderRequestDto dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Order failed: Customer not found"));

        Order order = new Order();
        String orderNumber = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        order.setOrderNumber(orderNumber);
        order.setCustomer(customer);
        order.setStatus(OrderStatus.CREATED);

        BigDecimal total = BigDecimal.ZERO;
        Set<Product> products = new HashSet<>();

        for (Long productId : dto.getProductIds()) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

            if (!product.isActive() || product.isDeleted()) {
                throw new RuntimeException("Product is not available for sale: " + product.getName());
            }

            productService.decreaseStock(productId, 1);

            Product updatedProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product refresh failed"));

            total = total.add(updatedProduct.getPrice());
            products.add(updatedProduct);
        }

        order.setProducts(products);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        kafkaProducerService.sendOrderCreatedEvent(
                new OrderCreatedEvent(
                        savedOrder.getOrderNumber(),
                        customer.getFirstName() + " " + customer.getLastName(),
                        savedOrder.getTotalAmount(),
                        LocalDateTime.now()
                )
        );

        return savedOrder.getOrderNumber();
    }
}

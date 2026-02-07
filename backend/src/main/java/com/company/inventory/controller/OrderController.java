package com.company.inventory.controller;

import com.company.inventory.dto.OrderRequestDto;
import com.company.inventory.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SALES')")
    public ResponseEntity<String> createOrder(@Valid @RequestBody OrderRequestDto dto) {
        String orderNumber = orderService.createOrder(dto);
        return new ResponseEntity<>("Sipariş başarıyla oluşturuldu. No: " + orderNumber, HttpStatus.CREATED);
    }

    // Gelecekte eklenecek GET /orders (ADMIN/MANAGER için) buraya gelecek.
}
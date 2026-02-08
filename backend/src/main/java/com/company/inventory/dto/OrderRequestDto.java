package com.company.inventory.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequestDto {
    private Long customerId;
    private List<Long> productIds;
    private int quantity;
}
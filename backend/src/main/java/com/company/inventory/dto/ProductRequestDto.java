package com.company.inventory.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequestDto {
    private String name;
    private String sku;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
}
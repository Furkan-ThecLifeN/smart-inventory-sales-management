package com.company.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDto {
    @NotBlank(message = "Ürün adı zorunludur")
    private String name;

    @NotBlank(message = "SKU (Barkod) zorunludur")
    private String sku;

    private String description;

    @Positive(message = "Fiyat 0'dan büyük olmalıdır")
    private BigDecimal price;

    @Min(value = 0, message = "Stok miktarı negatif olamaz")
    private Integer stockQuantity;
}

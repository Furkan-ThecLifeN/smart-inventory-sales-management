package com.company.inventory.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockLowEvent {
    private Long productId;
    private String productName;
    private String sku;
    private Integer currentStock;
    private String message; 
    private LocalDateTime eventTime;
}
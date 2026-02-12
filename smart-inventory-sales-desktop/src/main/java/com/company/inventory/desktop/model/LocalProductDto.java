package com.company.inventory.desktop.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocalProductDto extends ProductDto {
    
    private String syncStatus; 
    private LocalDateTime lastModifiedAt;
}
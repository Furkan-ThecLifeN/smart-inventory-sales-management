package com.company.inventory.desktop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalCustomerDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    
    private String syncStatus; 
    private LocalDateTime createdAt;
    
    private Long remoteId; 
}
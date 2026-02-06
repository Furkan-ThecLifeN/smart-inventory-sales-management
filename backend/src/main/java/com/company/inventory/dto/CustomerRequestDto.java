package com.company.inventory.dto;

import lombok.Data;

@Data
public class CustomerRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
}
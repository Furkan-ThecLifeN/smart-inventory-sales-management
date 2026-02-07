package com.company.inventory.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerRequestDto {

    @NotBlank(message = "İsim boş olamaz")
    private String firstName;

    @NotBlank(message = "Soyisim boş olamaz")
    private String lastName;

    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçersiz email formatı")
    private String email;

    private String phone;
    private String address;
}

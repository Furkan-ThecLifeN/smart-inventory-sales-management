package com.company.inventory.dto;
import lombok.Data;
import java.util.Set;
import jakarta.validation.constraints.*;

@Data
public class UserRequestDto {
    @NotBlank(message = "Kullanıcı adı boş olamaz")
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçersiz email formatı")
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 6, message = "Şifre en az 6 karakter olmalı")
    private String password;

    private Set<String> roles;
}
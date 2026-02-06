package com.company.inventory.dto;
import lombok.Data;
import java.util.Set;

@Data
public class UserRequestDto {
    private String username;
    private String email;
    private String password;
    private Set<String> roles; // ROLE_ADMIN vb. string olarak alacağız
}
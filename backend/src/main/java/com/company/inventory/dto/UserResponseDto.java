package com.company.inventory.dto;
import lombok.Data;
import java.util.Set;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
}
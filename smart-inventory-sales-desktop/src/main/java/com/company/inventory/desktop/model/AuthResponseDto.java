package com.company.inventory.desktop.model;

import lombok.Data;
import java.util.List;

@Data
public class AuthResponseDto {
    private String accessToken;
    private String username;
    private List<String> roles;
}
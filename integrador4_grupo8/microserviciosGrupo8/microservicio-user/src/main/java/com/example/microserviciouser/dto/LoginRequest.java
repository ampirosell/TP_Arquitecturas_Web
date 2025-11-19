package com.example.microserviciouser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private Long userId;
    private String email; // Opcional: puede usarse email o userId
}


package com.example.api.dto;

public record JwtResponseDTO(
    String token,
    String type,
    Long id,
    String email,
    String fristName,
){}
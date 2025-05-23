package com.backApi.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private String token;
    private Integer userId;
    private String email;
    private String nombre;
    private String telefono;
    private String direccion;
}
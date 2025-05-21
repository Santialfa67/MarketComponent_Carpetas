package com.backApi.demo.Dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String contraseña;
}

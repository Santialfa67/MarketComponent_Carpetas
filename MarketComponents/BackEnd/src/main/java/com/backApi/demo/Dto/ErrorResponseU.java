package com.backApi.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseU {
    private String message;
    private String timestamp; // Opcional: para la hora del error
    private int status; // Opcional: código de estado HTTP
}
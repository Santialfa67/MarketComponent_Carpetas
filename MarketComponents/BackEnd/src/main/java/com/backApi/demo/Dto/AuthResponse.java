package com.backApi.demo.Dto; // Asegúrate de que el paquete sea correcto, puedes crear 'Dto' si no existe

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private String token; // Opcional: para futuros tokens JWT
    private Integer userId; // Opcional: para devolver el ID del usuario logeado
    private String email; // Opcional: para devolver el email del usuario logeado
}
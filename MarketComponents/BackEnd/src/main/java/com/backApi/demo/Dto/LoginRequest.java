package com.backApi.demo.Dto;
//
//import lombok.Data;
//
//@Data
//public class LoginRequest {
//    private String email;
//    private String contraseña;
//}
// backend/com/backApi/demo/dto/LoginRequest.java (o donde guardes tus DTOs)

import lombok.Data; // Si usas Lombok
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
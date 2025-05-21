package com.backApi.demo.Dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer userId;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private String preferencias;
    private String rol;
}

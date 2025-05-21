package com.backApi.demo.Dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer categoriaId;  // Solo el ID, no el objeto completo
    private String imagen;
    private Integer stock;
    private Integer proveedorId;  // Cambiado de Proveedor a Integer (solo el ID)

}

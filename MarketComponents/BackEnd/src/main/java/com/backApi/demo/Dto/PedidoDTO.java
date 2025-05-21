package com.backApi.demo.Dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class PedidoDTO {

    private Integer usuarioId;
    private LocalDateTime fechaPedido;
    private BigDecimal total;
    private String estado;
    private String direccionEnvio;
    private String metodoPago;
}

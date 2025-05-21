package com.backApi.demo.Dto;

import lombok.Data;
import java.util.List;

@Data
public class PedidoRequest {
    private List<ItemPedido> items;

    @Data
    public static class ItemPedido {
        private Integer productoId;
        private Integer cantidad;
    }
}